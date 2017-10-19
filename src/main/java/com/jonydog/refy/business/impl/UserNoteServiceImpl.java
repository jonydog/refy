package com.jonydog.refy.business.impl;


import com.jonydog.refy.business.interfaces.UserNoteService;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.model.UserNote;
import com.jonydog.refy.util.RefyErrors;
import org.springframework.stereotype.Component;

@Component
public class UserNoteServiceImpl implements UserNoteService{


    @Override
    public void updateNote(UserNote note, RefyErrors errors) {

    }

    @Override
    public void addUserNote(UserNote note, Reference reference, RefyErrors errors) {

    }
}
