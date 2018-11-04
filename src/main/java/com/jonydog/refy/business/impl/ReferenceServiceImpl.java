package com.jonydog.refy.business.impl;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.daos.ReferenceDAO;
import com.jonydog.refy.jobs.ReferenceKeeper;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import com.jonydog.refy.util.StageManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class ReferenceServiceImpl implements ReferenceService{

    @Autowired
    private ReferenceKeeper referenceKeeper;

    @Autowired
    private ReferenceDAO referenceDAO;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private Validator validator;


    private HashMap<String,List<Reference>> termToRefsMapping;
    private ArrayList<Reference> allRefs;
    private String previousHomeFolder="";

    @Getter
    private AtomicLong lastModified = new AtomicLong(0L);



    @Override
    public ArrayList<Reference> getAllReferences(String homeFolder, RefyErrors errors,boolean ...fromFile) {

        boolean readFromFile = fromFile.length>0 ? fromFile[0] : false;

        // if cached array is null or home folder changed => reload refs from file
        if(this.allRefs==null || !this.previousHomeFolder.equals(homeFolder) || readFromFile) {
            Reference[] refArray = this.referenceDAO.getAllReferences(homeFolder);
            if(refArray!=null){

                this.allRefs =  new ArrayList<>( Arrays.asList( refArray ) );
                this.previousHomeFolder = homeFolder;
                // init terms to references mapping
                Executors.newSingleThreadExecutor().submit(()->{this.initTermsToRefsMapping();}  );
                // init persistence job
                this.referenceKeeper.initPersistenceJob();
            }

        }

        if( this.allRefs==null ){
            errors.addError("Error reading references from file");
            return new ArrayList<>();
        }

        return( this.allRefs  );

    }


    @Override
    public List<Reference> searchReferences(String searchTerm) {

        return this.termToRefsMapping.get(searchTerm);

    }

    @Override
    public void addReference(Reference reference, RefyErrors errors) {

        Set<ConstraintViolation<Reference>> violations  = this.validator.validate(reference);
        if( violations.size() > 0 ){
            System.out.println( violations );
            errors.addError("Invalid Reference");
            return;
        }
        this.allRefs.add(reference);
        //update last modified
        this.lastModified.set( System.currentTimeMillis() );

        //update terms mapping
        Executors.newSingleThreadExecutor().submit(()->{this.updateTermsToRefsMapping(reference);  }  );
    }

    @Override
    public void updateReference(Reference reference, Reference  newVersion, RefyErrors errors){

        Set<ConstraintViolation<Reference>> violations  = this.validator.validate(newVersion);
        if( violations.size() > 0 ){
            System.out.println( violations );
            errors.addError("Invalid Reference");
            return;
        }

        reference.setTitle( newVersion.getTitle() );
        reference.setObservations( newVersion.getObservations() );
        reference.setLink( newVersion.getLink() );
        reference.setAuthorsNames( newVersion.getAuthorsNames() );
        reference.setFilePath( newVersion.getFilePath() );
        reference.setKeywords( newVersion.getKeywords() );

        //update last modified
        this.lastModified.set( System.currentTimeMillis() );

    }

    @Override
    public void removeReference(Reference reference,RefyErrors errors) {

        this.allRefs.remove( reference );

        //update last modified
        this.lastModified.set( System.currentTimeMillis() );
    }


    private void initTermsToRefsMapping(){
        System.out.println("Init terms to refs mapping");

        this.termToRefsMapping = new HashMap<>();
        for( Reference r : this.allRefs){

            List<String> terms = this.extractAllTermsFromReference(r);
            for(String s : terms) {

                if( this.termToRefsMapping.get( s )==null ){
                    ArrayList<Reference> refList = new ArrayList<>();
                    refList.add(r);
                    this.termToRefsMapping.put(s,refList);
                }
                else{
                    this.termToRefsMapping.get(s).add(r);
                }

            }
        }

        Thread.currentThread().interrupt();
    }

    private void updateTermsToRefsMapping(Reference newref){
        System.out.println("Init terms to refs mapping");
        Thread.currentThread().interrupt();

    }

    private  List<String> extractAllPrefixesFromString(String s){

        List<String> list = new ArrayList<>();
        for( int i=0; i<s.length(); i++  ){

            list.add( s.substring(0,s.length()-i) );
        }
        return  list;
    }

    private List<String> extractAllTermsFromReference(Reference ref){

        ArrayList<String> extractedTerms = new ArrayList<>();

        // title terms
        String title = ref.getTitle().replaceAll(",","");
        extractedTerms.addAll( Arrays.asList( title.split(" ") ) );

        // author terms
        String authors = ref.getAuthorsNames().replaceAll(",","");
        extractedTerms.addAll( Arrays.asList( authors.split(" ") ) );

        // keywords terms
        String keywords = ref.getKeywords().replaceAll(",", "");
        extractedTerms.addAll( Arrays.asList( keywords.split(" ") ) );

        return extractedTerms.stream()
                .map( (s)->s.toLowerCase() )
                .map( this::extractAllPrefixesFromString )
                .flatMap( l->l.stream() )
                .collect(Collectors.toList());
    }

}
