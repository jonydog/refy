package com.jonydog.refy.daos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonydog.refy.model.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;



@Component
public class SettingsDAO {

    public static final String SETTINGS_FILE_NAME = "refySettings.json";

    @Autowired
    private ObjectMapper objectMapper;

    public Settings get(){

        File file = new File(SETTINGS_FILE_NAME);
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

        File file = new File(SETTINGS_FILE_NAME);
        try {
            this.objectMapper.writeValue(file,settings);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

}
