package com.jonydog.refy.daos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonydog.refy.model.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class SettingsDAO {

    @Autowired
    private ObjectMapper objectMapper;

    public Settings get(){

        File file = new File("refySettings.json");
        try{
            if( file.exists() ){
                Settings settings = this.objectMapper.readValue( file,Settings.class);
                return settings;
            }else{
                return null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean save(Settings settings){

        File file = new File("refySettings.json");
        try {
            this.objectMapper.writeValue(file,settings);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

}
