package com.jonydog.refy.util;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class RefyErrors {

    @Getter
    private List<String> errorMessages;

    public RefyErrors(){
        this.errorMessages = new LinkedList<>();
    }

    public void addError(String error){
        this.errorMessages.add(error);
    }

    public boolean hasErrors(){
        return ! this.errorMessages.isEmpty();
    }
}
