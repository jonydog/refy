package com.jonydog.refy.controllers;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.model.Reference;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ViewReferenceController implements Initializable {

    @Autowired
    private StageManager stageManager;
    @Autowired
    private ReferencesState referencesState;
    @Autowired
    private SettingsState settingsState;
    @Autowired
    private ReferenceService referenceService;

    /// fxml elements
    @FXML
    private TextArea observationsField;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorsLabel;
    @FXML
    private Label keywordsLabel;

    @FXML
    private void backButtonClicked(){

        this.stageManager.switchScene( "MainWindow.fxml" );
    }

    @FXML
    private void linkButton(){

        String url = this.referencesState.getSelectedReference().get().getLink();

        this.openLinkInBrowser(url);
    }

    @FXML
    private void pdfButton(){



        String filePath = this.referencesState.getSelectedReference().get().getFilePath();
        if( filePath==null ){
            return;
        }


        File f = new File( this.settingsState.getSettings().getHomeFolder() + filePath);
        this.openPDFFile(f);

    }

    @FXML
    private void saveButtonClicked(){

        Reference reference = new Reference();
        reference.setTitle( this.referencesState.getSelectedReference().get().getTitle() );
        reference.setAuthorsNames( this.referencesState.getSelectedReference().get().getAuthorsNames());
        reference.setFilePath( this.referencesState.getSelectedReference().get().getFilePath() );
        reference.setKeywords(  this.referencesState.getSelectedReference().get().getKeywords() );
        reference.setLink( this.referencesState.getSelectedReference().get().getLink() );
        reference.setObservations(this.observationsField.getText()  );

        RefyErrors errors = new RefyErrors();
        this.referenceService.updateReference(this.referencesState.getSelectedReference().get(), reference,errors );

        if( errors.hasErrors() ){
            AlertUtils.popUpAlert(errors, Alert.AlertType.ERROR,this.stageManager.getModalStage());
        }
        else{
            this.referencesState.refreshState(errors);
            this.stageManager.switchScene("MainWindow.fxml");
        }

    }


    private void openPDFFile(File f){

        System.out.println("File to be opened:" + f.getAbsolutePath());

        if(PlatformUtil.isWindows()) {

            Settings s = this.settingsState.getSettings();
            if(s.getPdfReaderPath() != null && !s.getPdfReaderPath().isEmpty() && f.exists()) {
                String command = s.getPdfReaderPath() + " " + "\"" + f.getAbsolutePath() + "\"";
                try {
                    Runtime.getRuntime().exec(command);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(PlatformUtil.isMac() ){

            String command[] = {"bash", "./open.sh",  f.getAbsolutePath()};
            try {
                Runtime.getRuntime().exec(command);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void openLinkInBrowser(String url){

        if( url==null ){
            return;
        }
        Settings settings = this.settingsState.getSettings();

        if(PlatformUtil.isWindows() ){


            if( settings.getBrowserPath()!=null && !settings.getPdfReaderPath().isEmpty()    ){

                try {
                    Runtime.getRuntime().exec( settings.getBrowserPath() + " " + url  );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if( PlatformUtil.isMac() ){

            if(!url.startsWith("http")){
                url = "http://" + url;
            }


            String command[] = {"bash", "./open.sh", url};
            try {
                Runtime.getRuntime().exec(command);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void prepareView(Reference ref){

        Reference currentRef =  this.referencesState.getSelectedReference().get();
        this.titleLabel.setText( currentRef.getTitle() );
        this.authorsLabel.setText( currentRef.getAuthorsNames() );
        this.keywordsLabel.setText( currentRef.getKeywords() );
        this.observationsField.setText( currentRef.getObservations() );

    }



}
