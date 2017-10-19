package com.jonydog.refy.business.impl;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.daos.ReferenceDAO;
import com.jonydog.refy.daos.UserNoteDAO;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReferenceServiceImpl implements ReferenceService{

    @Autowired
    private ReferenceDAO referenceDAO;

    @Autowired
    private UserNoteDAO userNoteDAO;


    @Override
    public List<Reference> getAllReferences(RefyErrors errors) {

        return this.referenceDAO.findAll();
    }

    @Override
    public List<Reference> searchReferences(String searchTerm) {

        return this.referenceDAO.searchReferences(searchTerm);
    }

    @Override
    public void addReference(Reference reference, RefyErrors errors) {

        this.referenceDAO.save( reference );
    }

    @Override
    public void udpateReference(Reference reference, RefyErrors errors) {

        this.referenceDAO.save( reference );
    }

    @Override
    public void removeReference(Long referenceId, RefyErrors errors) {

        this.referenceDAO.delete(referenceId);
    }
}
