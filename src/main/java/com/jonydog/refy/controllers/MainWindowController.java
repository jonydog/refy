package com.jonydog.refy.controllers;

import com.jonydog.refy.model.Reference;
import com.jonydog.refy.statesources.ReferencesState;
import com.jonydog.refy.util.StageManager;
import com.jonydog.refy.util.TableViewUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainWindowController implements Initializable {

    @Autowired
    private StageManager stageManager;


    // state sources
    @Autowired
    private ReferencesState referencesState;


    // fxml related
    private Scene newReferenceScene;
    private Scene settingsScene;
    @FXML
    private TableView<Reference> mainTable;
    @FXML
    private TextField searchField;

    @FXML
    private Button settingsButton;


    @FXML
    private void settingsButtonClicked(){

        if(this.settingsScene==null) {
            this.settingsScene = new Scene(this.stageManager.getView("SettingsDialog.fxml"));
        }
        this.stageManager.getModalStage().setScene( this.settingsScene );
        this.stageManager.getModalStage().setTitle("Settings");
        this.stageManager.getModalStage().showAndWait();
    }

    @FXML
    private void newButtonClicked(){

        if(this.newReferenceScene==null) {
            this.newReferenceScene = new Scene(this.stageManager.getView("NewReference.fxml"));
        }
        this.stageManager.getModalStage().setScene( this.newReferenceScene );
        this.stageManager.getModalStage().setTitle("New reference");
        this.stageManager.getModalStage().showAndWait();
    }

    @FXML
    private void editButtonClicked(){

        this.stageManager.switchScene( "EditReference.fxml" );
    }
    @FXML
    private void viewButtonClicked(){

        this.stageManager.switchScene( "ViewReference.fxml" );
    }

    private void styleSomeElements(){

        File imageFile = new File( "images/tools.png" );
        Image image = new Image( imageFile.getPath() );
        ImageView imageView = new ImageView(image);
        this.settingsButton.setGraphic(imageView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.styleSomeElements();

        this.mainTable.getColumns().get(0).prefWidthProperty().bind( this.mainTable.widthProperty().divide(2) );
        this.mainTable.getColumns().get(1).prefWidthProperty().bind( this.mainTable.widthProperty().divide(2) );

        TableViewUtils.fillTableView(this.mainTable,this.referencesState.getCurrentReferences(),"title","authorsNames");


    }
}
