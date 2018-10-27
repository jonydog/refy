package com.jonydog.refy.controllers;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.statesources.ReferencesState;
import com.jonydog.refy.statesources.SettingsState;
import com.jonydog.refy.util.AlertUtils;
import com.jonydog.refy.util.RefyErrors;
import com.jonydog.refy.util.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

@Component
public class NewReferenceController implements Initializable{

    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private SettingsState settingsState;
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
    @FXML
    private Button addButton;
    @FXML
    private AnchorPane dropArea;
    @FXML
    private Label fileLabel;

    @FXML
    private void backButtonClicked(){

        this.stageManager.getModalStage().hide();
    }

    @FXML
    private void chooseFileButtonClicked(){

        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        String homeFolder = this.settingsState.getSettings().getHomeFolder();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add a file");
        fileChooser.getExtensionFilters().add(pdfFilter);
        fileChooser.setInitialDirectory( new File( homeFolder ) );
        File selectedFile = fileChooser.showOpenDialog( this.stageManager.getModalStage() );

        String relativeFileName= selectedFile.getAbsolutePath().replace( this.settingsState.getSettings().getHomeFolder(), "" );

        if( selectedFile.getAbsolutePath().startsWith(this.settingsState.getSettings().getHomeFolder()) ){
            this.fileLabel.setText( relativeFileName.replace("\\","/") );
            this.extractMetadataFromPdf(selectedFile);
        }
        else{
           File movedFile = this.promptFileMove(selectedFile);
           if( movedFile!=null ){
               String relativePath = movedFile.getAbsolutePath().replace( this.settingsState.getSettings().getHomeFolder(), "" );
               this.fileLabel.setText( relativePath.replace("\\","/") );
               this.extractMetadataFromPdf(movedFile);
           }
        }
    }

    @FXML
    private void addButtonClicked(){

        if(this.referencesState.getEditMode().get()){
            this.updateReference();
        }
        else{
            this.createNewReference();
        }
    }



    private void createNewReference(){

        Reference reference = new Reference();
        reference.setTitle( this.titleField.getText() );
        reference.setAuthorsNames( this.authorsField.getText() );
        reference.setFilePath( this.fileLabel.getText());
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

    private void updateReference(){

        Reference reference = new Reference();
        reference.setTitle( this.titleField.getText() );
        reference.setAuthorsNames( this.authorsField.getText() );
        reference.setFilePath( this.fileLabel.getText() );
        reference.setKeywords(  this.keywordsField.getText() );
        reference.setLink( this.linkField.getText() );
        reference.setObservations(this.observationsField.getText());

        RefyErrors errors = new RefyErrors();
        this.referenceService.updateReference(this.referencesState.getSelectedReference().get(),reference,errors);
        if( errors.hasErrors() ){
            AlertUtils.popUpAlert(errors, Alert.AlertType.ERROR,this.stageManager.getModalStage());
        }
        else{
            this.referencesState.refreshState(errors);
            this.stageManager.getModalStage().hide();
        }

    }


    public void prepareForm(Reference ref){

        this.authorsField.setText( ref.getAuthorsNames() );
        this.fileLabel.setText(ref.getFilePath() );
        this.keywordsField.setText( ref.getKeywords() );
        this.titleField.setText( ref.getTitle() );
        this.linkField.setText( ref.getLink() );
        this.observationsField.setText( ref.getObservations() );

        this.addButton.setText("Save");
    }

    public void clearForm(){
        this.authorsField.clear();
        this.fileLabel.setText("");
        this.linkField.clear();
        this.observationsField.clear();
        this.titleField.clear();
        this.keywordsField.clear();

        this.addButton.setText("Create");
    }

    private boolean isFileInsideHomeFolderWithPopUp(File f){

        RefyErrors errors = new RefyErrors();
            if (  f.getAbsolutePath().startsWith(this.settingsState.getSettings().getHomeFolder()) ) {
            return true;
        }
        else{
            errors.addError("File is not inside home folder.");
            AlertUtils.popUpAlert(
                    errors, Alert.AlertType.ERROR,this.stageManager.getModalStage()
            );
            return false;
        }
    }


    private File promptFileMove(File f){

        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Select destination folder");
        fileChooser.setInitialDirectory( new File( this.settingsState.getSettings().getHomeFolder() ) );
        File selectedDir = fileChooser.showDialog( this.stageManager.getModalStage() );

        if( ! this.isFileInsideHomeFolderWithPopUp(selectedDir) ){
            return null;
        }

        File destination = new File(selectedDir + "/"+ f.getName() );
        try {
            Files.copy( f.toPath(), destination.toPath() );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destination;

    }


    private void extractMetadataFromPdf(File f){

        PDDocument doc = null;
        try {
            doc = PDDocument.load(f);
            PDDocumentInformation info = doc.getDocumentInformation();

            AlertUtils.confirmationAlert(
                    (a)->{
                        this.titleField.setText( info.getTitle() );
                        this.authorsField.setText( info.getAuthor() );
                        this.keywordsField.setText( info.getKeywords() + " " + info.getSubject() );
                    },
                    "PDF Metadata",
                    "Accept metadata?",
                    "",
                    "Yes",
                    "No",
                    this.stageManager.getModalStage()
            );

            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        this.dropArea.setOnDragDropped(
                event -> {
                    Dragboard db = event.getDragboard();
                    if (db.hasFiles()) {
                        File selectedFile = db.getFiles().get(0);
                        if( selectedFile.getAbsolutePath().startsWith(this.settingsState.getSettings().getHomeFolder()) ){

                            this.fileLabel.setText(db.getFiles().get(0).getAbsolutePath().replace("\\","/") );
                            this.extractMetadataFromPdf(db.getFiles().get(0));
                        }
                        else{
                            File movedFile = this.promptFileMove(selectedFile);
                            if( movedFile!=null ){
                                String relativePath = movedFile.getAbsolutePath().replace( this.settingsState.getSettings().getHomeFolder(), "" );
                                this.fileLabel.setText( relativePath.replace("\\","/") );
                                this.extractMetadataFromPdf(movedFile);
                            }
                        }
                    }
                    /* let the source know whether the string was successfully
                     * transferred and used */
                    event.setDropCompleted(true);

                    event.consume();
                }
        );

        this.dropArea.setOnDragOver(
                (event)->{

                    if(event.getDragboard().hasFiles() && event.getDragboard().getFiles().get(0).getAbsolutePath().toLowerCase().endsWith(".pdf") ){
                        event.acceptTransferModes( TransferMode.ANY );
                    }

                    event.consume();
                }
        );
    }

}
