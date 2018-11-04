package com.jonydog.refy.controllers;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.statesources.ReferencesState;
import com.jonydog.refy.util.AlertUtils;
import com.jonydog.refy.util.RefyErrors;
import com.jonydog.refy.util.StageManager;
import com.jonydog.refy.util.TableViewUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @Autowired
    private NewReferenceController newReferenceController;
    @Autowired
    private ViewReferenceController viewReferenceController;


    // state sources
    @Autowired
    private ReferencesState referencesState;
    @Autowired
    private ReferenceService referenceService;

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
    private Button refreshButton;

    @FXML
    private void settingsButtonClicked() {

        if (this.settingsScene == null) {
            this.settingsScene = new Scene(this.stageManager.getView("SettingsDialog.fxml"));
        }
        this.stageManager.getModalStage().setScene(this.settingsScene);
        this.stageManager.getModalStage().setTitle("Settings");
        this.stageManager.getModalStage().showAndWait();
    }


    @FXML
    private void refreshButtonClicked(){

        RefyErrors errors = new RefyErrors();
        this.referencesState.refreshFromFile(errors);

        if( errors.hasErrors() ){
            AlertUtils.popUpAlert(errors, Alert.AlertType.ERROR,this.stageManager.getMainStage());
        }
    }

    @FXML
    private void newButtonClicked() {

        if (this.newReferenceScene == null) {
            this.newReferenceScene = new Scene(this.stageManager.getView("NewReference.fxml"));
        }

        this.referencesState.getEditMode().set(false);
        this.newReferenceController.clearForm();
        this.stageManager.getModalStage().setScene(this.newReferenceScene);
        this.stageManager.getModalStage().setTitle("New reference");
        this.stageManager.getModalStage().showAndWait();
    }

    @FXML
    private void deleteButtonClicked() {

        Reference selectedItem = this.mainTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        RefyErrors errors = new RefyErrors();
        AlertUtils.confirmationAlert((a) -> {
                    this.referenceService.removeReference(selectedItem, errors);
                    this.referencesState.refreshState(errors);
                },
                (d)->{},
                "Delete reference",
                "Do you confirm?",
                "Do you want to remove '" + selectedItem.getTitle(),
                "Delete",
                "Cancel",
                this.stageManager.getMainStage()
        );

        if (errors.hasErrors()) {
            AlertUtils.popUpAlert(errors, Alert.AlertType.ERROR, this.stageManager.getMainStage());
        }

    }

    @FXML
    private void editButtonClicked() {

        Reference selectedItem = this.mainTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        this.referencesState.getSelectedReference().set(selectedItem);
        this.referencesState.getEditMode().set(true);
        this.newReferenceController.prepareForm(selectedItem);

        //show edit modal
        if (this.newReferenceScene == null) {
            this.newReferenceScene = new Scene(this.stageManager.getView("NewReference.fxml"));
        }
        this.stageManager.getModalStage().setScene(this.newReferenceScene);
        this.stageManager.getModalStage().setTitle("Edit reference");
        this.stageManager.getModalStage().showAndWait();

    }

    @FXML
    private void searchButtonClicked(){

        this.referencesState.refreshState(new RefyErrors());

    }

    @FXML
    private void viewButtonClicked() {

        Reference selectedItem = this.mainTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        this.viewReferenceController.prepareView( selectedItem );
        this.stageManager.switchScene("ViewReference.fxml");
    }

    private void styleSomeElements() {

        File imageFile = new File("images/tools.png");
        Image image = new Image(imageFile.getPath());
        ImageView imageView = new ImageView(image);
        this.settingsButton.setGraphic(imageView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.styleSomeElements();

        this.mainTable.getColumns().get(0).prefWidthProperty().bind(this.mainTable.widthProperty().divide(2));
        this.mainTable.getColumns().get(1).prefWidthProperty().bind(this.mainTable.widthProperty().divide(2));

        TableViewUtils.fillTableView(this.mainTable, this.referencesState.getCurrentReferences(), "title", "authorsNames");

        // listener on table click
        this.mainTable.setRowFactory(tv -> {
            TableRow<Reference> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                Reference ref = row.getItem();
                this.referencesState.getSelectedReference().set(ref);

                if (event.getClickCount() == 2 && (!row.isEmpty())) {

                    this.viewButtonClicked();
                }
            });
            return row;
        });

        // bind search text to state property
        this.referencesState.getCurrentSearchText().bind( this.searchField.textProperty()    );

    }
}
