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

    private static final String REFY_FILENAME = "refy.json";

    public Reference[] getAllReferences(String homeFolder){

        File file = new File(homeFolder+"/"+REFY_FILENAME);
        try{
            if( file.exists() ){
                Reference[] refs = this.objectMapper.readValue( file,Reference[].class);
                return refs;
            }else{
                Reference[] refs = new Reference[0];
                return refs;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(Reference[] refs, String homeFolder, RefyErrors errors) {

        File file = new File(homeFolder+"/"+REFY_FILENAME);
        try {
            System.out.println("File written:"+file.getAbsolutePath());
            this.objectMapper.writeValue(file, refs);
        } catch (IOException e) {
            errors.addError(
                    "Could not save to file"
            );
        }

    }

}
