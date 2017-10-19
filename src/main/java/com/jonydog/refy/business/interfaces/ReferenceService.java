package com.jonydog.refy.business.interfaces;

import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;

import java.util.List;

public interface ReferenceService {

    List<Reference> getAllReferences(RefyErrors errors);

    List<Reference> searchReferences(String searchTerm);

    void addReference(Reference reference, RefyErrors errors);

    void udpateReference(Reference  reference,RefyErrors errors);

    void removeReference(Long referenceId,RefyErrors errors);
}
