package com.jonydog.refy.controllers;

import com.jonydog.refy.util.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ViewReferenceController implements Initializable {

    @Autowired
    private StageManager stageManager;


    @FXML
    private void backButtonClicked(){

        this.stageManager.switchScene( "MainWindow.fxml" );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
