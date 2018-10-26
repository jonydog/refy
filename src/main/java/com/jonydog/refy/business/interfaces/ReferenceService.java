package com.jonydog.refy.business.interfaces;

import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public interface ReferenceService {

    ArrayList<Reference> getAllReferences(String homeFolder, RefyErrors errors,boolean ...fromFile);

    ArrayList<Reference> searchReferences(String searchTerm);

    void addReference(Reference reference, RefyErrors errors);

    void updateReference(Reference reference, Reference  newVersion, RefyErrors errors);

    void removeReference(Reference toRemove,RefyErrors errors);

    AtomicLong getLastModified();
}
