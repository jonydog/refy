package com.jonydog.refy.business.interfaces;

import com.jonydog.refy.model.Reference;
import com.jonydog.refy.model.UserNote;
import com.jonydog.refy.util.RefyErrors;

public interface UserNoteService {

    void updateNote(UserNote note, RefyErrors errors);

    void addUserNote(UserNote note, Reference reference, RefyErrors errors);
}
