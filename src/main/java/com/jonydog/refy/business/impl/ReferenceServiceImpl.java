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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

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


    private HashMap<String,Reference> termToRefsMapping;
    private ArrayList<Reference> allRefs;
    private String previousHomeFolder="";

    @Getter
    private AtomicLong lastModified = new AtomicLong(0L);


    @Override
    public ArrayList<Reference> getAllReferences(String homeFolder, RefyErrors errors) {

        // if cached array is null or home folder changed => reload refs from file
        if(this.allRefs==null || !this.previousHomeFolder.equals(homeFolder) ) {
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
    public ArrayList<Reference> searchReferences(String searchTerm) {
        return null;
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
        Thread.currentThread().interrupt();

    }

    private void updateTermsToRefsMapping(Reference newref){
        System.out.println("Init terms to refs mapping");
        Thread.currentThread().interrupt();

    }

}
