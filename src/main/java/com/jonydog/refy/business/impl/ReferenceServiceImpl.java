package com.jonydog.refy.business.impl;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.daos.ReferenceDAO;
import com.jonydog.refy.jobs.ReferenceKeeper;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.concurrent.Executors;

@Component
public class ReferenceServiceImpl implements ReferenceService{

    @Autowired
    private ReferenceKeeper referenceKeeper;

    @Autowired
    private ReferenceDAO referenceDAO;

    @Autowired
    private Validator validator;

    private HashMap<String,Reference> termToRefsMapping;

    private ArrayList<Reference> allRefs;

    @Override
    public ArrayList<Reference> getAllReferences(RefyErrors errors) {

        // if cached array is null
        if(this.allRefs==null) {
            Reference[] refArray = this.referenceDAO.getAllReferences();
            if(refArray!=null){

                this.allRefs =  new ArrayList<>( Arrays.asList( refArray ) );
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
    }

    @Override
    public void udpateReference(Reference reference, RefyErrors errors) {

    }

    @Override
    public void removeReference(Long referenceId, RefyErrors errors) {

    }

    private void initTermsToRefsMapping(){
        System.out.println("Init terms to refs mapping");
        Thread.currentThread().interrupt();

    }

}
