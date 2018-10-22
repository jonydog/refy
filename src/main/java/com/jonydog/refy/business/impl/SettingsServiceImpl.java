package com.jonydog.refy.business.impl;

import com.jonydog.refy.business.interfaces.SettingsService;
import com.jonydog.refy.daos.SettingsDAO;
import com.jonydog.refy.model.Settings;
import com.jonydog.refy.util.RefyErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    private SettingsDAO settingsDAO;

    @Autowired
    private Validator validator;

    @Override
    public Settings get(RefyErrors errors) {

        Settings s = this.settingsDAO.get();
        if(s!=null){
            return s;
        }
        else{
            errors.addError("No settings");
            return null;
        }
    }


    @Override
    public void save(Settings settings, RefyErrors errors) {

        Set<ConstraintViolation<Settings>> violations  = this.validator.validate(settings);

        if( violations.size() > 0 ){
            errors.addError("Invalid settings");
        }

       if( this.settingsDAO.save(settings)  ){
           return;
       }
        else{
            errors.addError("Error saving settings to file.");
       }
    }
}
