package com.jonydog.refy.controllers;

import com.jonydog.refy.business.interfaces.SettingsService;
import com.jonydog.refy.model.Settings;
import com.jonydog.refy.statesources.ReferencesState;
import com.jonydog.refy.statesources.SettingsState;
import com.jonydog.refy.util.AlertUtils;
import com.jonydog.refy.util.RefyErrors;
import com.jonydog.refy.util.StageManager;
import com.sun.javafx.PlatformUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
    private ReferencesState referenceState;

    @Autowired
    private StageManager stageManager;

    @FXML
    private TextField homeFolderField;
    @FXML
    private TextField browserField;
    @FXML
    private TextField pdfReaderField;

    @FXML
    public void selectFileButtonClicked(){

        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Add a file");
        String fileName = fileChooser.showDialog( this.stageManager.getModalStage() ).getAbsolutePath();
        this.homeFolderField.setText( fileName );
    }

    @FXML
    public void browserClicked(){

        FileChooser fileChooser = new FileChooser();
        this.assignAproppriateExtension(fileChooser);

        fileChooser.setTitle("Choose favorite Web browser");
        String browserFileName = fileChooser.showOpenDialog( this.stageManager.getModalStage() ).getAbsolutePath();
        this.browserField.setText( browserFileName );
    }

    @FXML
    private void pdfReaderClicked(){

        FileChooser fileChooser = new FileChooser();
        this.assignAproppriateExtension(fileChooser);

        fileChooser.setTitle("Choose favorite PDF reader");
        String browserFileName = fileChooser.showOpenDialog( this.stageManager.getModalStage() ).getAbsolutePath();
        this.pdfReaderField.setText( browserFileName );
    }

    @FXML
    public void cancelButtonClicked(){

        this.stageManager.getModalStage().hide();
    }

    @FXML
    public void saveButtonClicked(){

        Settings s = new Settings();
        s.setHomeFolder(this.homeFolderField.getText());
        s.setBrowserPath( this.browserField.getText() );
        s.setPdfReaderPath( this.pdfReaderField.getText() );

        RefyErrors errors = new RefyErrors();
        this.settingsService.save( s, errors );
        if( errors.hasErrors()  ){
            AlertUtils.popUpAlert(errors, Alert.AlertType.ERROR,this.stageManager.getModalStage());
        }
        else{
            AlertUtils.popInformation("Settings saved with successs!", "Success",this.stageManager.getMainStage() );
            this.stageManager.getModalStage().hide();
        }

        this.settingsState.refreshState(errors);
        this.referenceState.refreshState(errors);
    }

    private void assignAproppriateExtension(FileChooser fileChooser){
        FileChooser.ExtensionFilter pdfFilter;
        if(PlatformUtil.isWindows()){
            pdfFilter = new FileChooser.ExtensionFilter("EXE files (*.exe)", "*.exe");
            fileChooser.setSelectedExtensionFilter( pdfFilter);
        }
        else if(PlatformUtil.isMac()){
            pdfFilter = new FileChooser.ExtensionFilter("APP files (*.app)", "*.app");
            fileChooser.setSelectedExtensionFilter( pdfFilter);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.homeFolderField.setText( this.settingsState.getSettings().getHomeFolder() );
        this.browserField.setText( this.settingsState.getSettings().getBrowserPath()  );
        this.pdfReaderField.setText( this.settingsState.getSettings().getPdfReaderPath() );
    }

}
