package com.jonydog.refy.statesources;

import com.jonydog.refy.business.interfaces.SettingsService;
import com.jonydog.refy.model.Settings;
import com.jonydog.refy.util.RefyErrors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsState extends StateSource {

    @Autowired
    private SettingsService settingsService;

    @Getter
    private Settings settings;

    @Override
    public void refreshState(RefyErrors errors) {

        this.settings = this.settingsService.get(errors);
    }

    @Override
    public void init() {

        this.settings = this.settingsService.get(null);
    }
}
