package com.jonydog.refy.daos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ReferenceDAO {

    @Autowired
    private ObjectMapper objectMapper;

    public Reference[] getAllReferences(){

        File file = new File("refy.json");
        try{
            if( file.exists() ){
                Reference[] refs = this.objectMapper.readValue( file,Reference[].class);
                return refs;
            }else{
                return null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(Reference[] refs, RefyErrors errors) {

        File file = new File("refy.json");
        try {
            this.objectMapper.writeValue(file, refs);
        } catch (IOException e) {
            errors.addError(
                    "Could not save to file"
            );
        }

    }

}
