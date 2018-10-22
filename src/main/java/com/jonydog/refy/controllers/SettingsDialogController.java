package com.jonydog.refy.controllers;

import com.jonydog.refy.business.interfaces.SettingsService;
import com.jonydog.refy.model.Settings;
import com.jonydog.refy.statesources.SettingsState;
import com.jonydog.refy.util.AlertUtils;
import com.jonydog.refy.util.RefyErrors;
import com.jonydog.refy.util.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class SettingsDialogController implements Initializable {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private SettingsState settingsState;

    @Autowired
    private StageManager stageManager;

    @FXML
    private TextField homeFolderField;


    @FXML
    public void selectFileButtonClicked(){

        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Add a file");
        String fileName = fileChooser.showDialog( this.stageManager.getModalStage() ).getAbsolutePath();
        this.homeFolderField.setText( fileName );
    }

    @FXML
    public void cancelButtonClicked(){

        this.stageManager.getModalStage().hide();
    }

    @FXML
    public void saveButtonClicked(){

        Settings s = new Settings();
        s.setHomeFolder(this.homeFolderField.getText());

        RefyErrors errors = new RefyErrors();
        this.settingsService.save( s, errors );
        if( errors.hasErrors()  ){
            AlertUtils.popUpAlert(errors, Alert.AlertType.ERROR,this.stageManager.getModalStage());
        }
        else{
            AlertUtils.popInformation("Settings saved with successs!", "Success",this.stageManager.getMainStage() );
            this.stageManager.getModalStage().hide();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.homeFolderField.setText( this.settingsState.getSettings().getHomeFolder() );
    }
}
