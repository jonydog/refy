package com.jonydog.refy.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class StageManager {
	
	final static Logger logger = Logger.getLogger(StageManager.class);

	private String workingMode; // "IDE" or "JAR"

    private ResourceLoader resourceLoader;
	
	//Main stage
	@Getter
	private Stage mainStage;

	@Getter
	private Stage modalStage;

	@Getter
	private Stage debugStage;

	// singleton instance
	private static StageManager instance;
	// hashmap containing the views
	private HashMap<String, Parent> nodeMap;
	
	//Spring application  context
	private ApplicationContext applicationContext;
	
	private StageManager(String workingMode,ResourceLoader resourceLoader) throws IOException{
		this.nodeMap = new HashMap<String, Parent>();
		this.workingMode = workingMode;
		this.resourceLoader = resourceLoader;
	}
	
	public static StageManager getInstance(String workingMode, ResourceLoader resourceLoader) throws IOException{
		if(instance==null){
			instance = new StageManager(workingMode, resourceLoader);
		}
		return instance;
	}
	
	
	public void setPrimaryStage(Stage stage){
		this.mainStage=stage;
		this.mainStage.setTitle("refy");
        // initialize modal stage
        this.modalStage = new Stage();
        this.modalStage.initModality(Modality.APPLICATION_MODAL);
        this.modalStage.setFullScreen(false);
        this.modalStage.setAlwaysOnTop(true);
        this.modalStage.initOwner(this.mainStage);

        // debug stage
		this.debugStage = new Stage();
		this.debugStage.initModality(Modality.NONE);
	}

	public Parent getView(String viewName) {
		return this.nodeMap.get(viewName);
	}
	
	/**
	 * Switches the main stage scene  
	 * 
	 * @param viewName : .fxml file name to switch scene to
	 */
	public void switchScene(String viewName){

		Parent rootNode = this.nodeMap.get(viewName);
		Scene scene = this.mainStage.getScene();
        if (scene == null) {
            scene = new Scene(rootNode);
        }
        scene.setRoot(rootNode);
        
        this.mainStage.setScene(scene);
        this.mainStage.show();
        return ;
	}
	
	
	public void changePaneContent(Pane parentNode, String  loadedViewName){

		parentNode.getChildren().clear();

		Parent newContent = this.nodeMap.get(loadedViewName);
		AnchorPane.setBottomAnchor(newContent,0.0);
		AnchorPane.setTopAnchor( newContent, 0.0 );
		AnchorPane.setLeftAnchor( newContent, 0.0 );
		AnchorPane.setRightAnchor( newContent, 0.0 );
		parentNode.getChildren().add( newContent );
	
	}
	
	public void changePaneContent(Pane parentNode, Node  node){

		parentNode.getChildren().clear();
		parentNode.getChildren().add( node );
	
	}
	
	
	public void setApplicationContext(ApplicationContext springContext){
		// set the fxmlloader controller factory
		this.applicationContext = springContext;
	}
	
	public void loadAllViews() throws IOException{



        List<File> allFiles = new LinkedList<>();
        if( this.workingMode.equals("IDE") ) {
            Files.walk(Paths.get((new File(this.getClass().getClassLoader().getResource("").getPath())).getAbsolutePath()))
                    .filter(Files::isRegularFile)
                    .forEach(
                            (p) -> {
                                allFiles.add(p.toFile());
                            }
                    );

            System.out.println( allFiles );

            List<File> fxmlFiles = new ArrayList<>();
            for(File f : allFiles){
                if( f.getAbsolutePath().endsWith(".fxml") ){
                    fxmlFiles.add( f );
                }
            }
            System.out.println( "fxml files" );
            System.out.println( fxmlFiles );

            // add the corresponding nodes to the nodeMap
            for(File f:fxmlFiles ){
                System.out.println(f.getAbsolutePath());
                String justName = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(FileSystems.getDefault().getSeparator()) + 1, f.getAbsolutePath().length());
                System.out.println("justName: " + justName);
                FXMLLoader fxmlLoader = new FXMLLoader(f.toURI().toURL()   );
                fxmlLoader.setControllerFactory(this.applicationContext::getBean);
                Parent n = fxmlLoader.load();

                if(n==null){
                    logger.debug("Node is null");
                }
                logger.debug("Loaded view: " + justName );
                this.nodeMap.put(justName,  n );
            }

        }
        else{

            String[] fxmlFileNames = {
                    "MainWindow.fxml",
                    "NewReference.fxml",
                    "SettingsDialog.fxml",
                    "ViewReference.fxml"
            };

            for( String s : fxmlFileNames) {

                File temp = new File(s);

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setControllerFactory(this.applicationContext::getBean);
                InputStream is = new FileInputStream(temp);
                Parent n = fxmlLoader.load(is);

                if(n==null){
                    logger.debug("Node is null");
                }
                logger.debug("Loaded view: " + s );
                this.nodeMap.put(s,  n );

            }

    }

	}

}
