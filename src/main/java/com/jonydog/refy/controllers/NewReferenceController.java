package com.jonydog.refy.controllers;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.business.interfaces.SettingsService;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.statesources.ReferencesState;
import com.jonydog.refy.util.AlertUtils;
import com.jonydog.refy.util.RefyErrors;
import com.jonydog.refy.util.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class NewReferenceController implements Initializable{

    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private ReferencesState referencesState;


    @Autowired
    private StageManager stageManager;
    @FXML
    private TextArea titleField;
    @FXML
    private TextArea authorsField;
    @FXML
    private TextArea keywordsField;
    @FXML
    private TextField linkField;
    @FXML
    private TextArea observationsField;

    private String filePath;

    @FXML
    private void chooseFileButtonClicked(){

        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add a file");
        fileChooser.setSelectedExtensionFilter( pdfFilter);
        fileChooser.setInitialDirectory( new File( this.settingsService.get(null).getHomeFolder() ) );
        String fileName = fileChooser.showOpenDialog( this.stageManager.getModalStage() ).getAbsolutePath();
        this.filePath = fileName;
    }

    @FXML
    private void addButtonClicked(){

        Reference reference = new Reference();
        reference.setTitle( this.titleField.getText() );
        reference.setAuthorsNames( this.authorsField.getText() );
        reference.setFilePath( this.filePath );
        reference.setKeywords(  this.keywordsField.getText() );
        reference.setLink( this.linkField.getText() );
        reference.setObservations(this.observationsField.getText());

        RefyErrors errors = new RefyErrors();
        this.referenceService.addReference( reference, errors);

        if( errors.hasErrors() ){
            AlertUtils.popUpAlert(errors, Alert.AlertType.ERROR,this.stageManager.getModalStage());
        }
        else{
            this.referencesState.refreshState(errors);
            this.stageManager.getModalStage().hide();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
