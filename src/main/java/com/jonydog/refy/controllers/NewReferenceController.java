package com.jonydog.refy.controllers;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import com.jonydog.refy.util.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class NewReferenceController implements Initializable{

    @Autowired
    private ReferenceService referenceService;


    @Autowired
    private StageManager stageManager;
    @FXML
    private TextArea titleField;
    @FXML
    private TextArea authorsField;
    @FXML
    private TextArea keywordsField;

    private String filePath;

    @FXML
    private void chooseFileButtonClicked(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add a file");
        String fileName = fileChooser.showOpenDialog( this.stageManager.getMainStage() ).getAbsolutePath();
        this.filePath = fileName;
    }

    @FXML
    private void addButtonClicked(){

        Reference reference = new Reference();
        reference.setTitle( this.titleField.getText() );
        reference.setAuthorsNames( this.authorsField.getText() );
        reference.setFilePath( this.filePath );
        reference.setKeywords(  this.keywordsField.getText() );

        this.referenceService.addReference( reference, new RefyErrors());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
