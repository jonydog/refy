package com.jonydog.refy.business.interfaces;


import com.jonydog.refy.model.Settings;
import com.jonydog.refy.util.RefyErrors;

public interface SettingsService {


    Settings get(RefyErrors errors);


    void save(Settings settings,RefyErrors errors);


}
