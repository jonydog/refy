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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.*;


public class StageManager {
	
	final static Logger logger = Logger.getLogger(StageManager.class);
	
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
	
	private StageManager() throws IOException{
		this.nodeMap = new HashMap<String, Parent>();
	}
	
	public static StageManager getInstance() throws IOException{
		if(instance==null){
			instance = new StageManager();
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
				
		Enumeration<URL> l = this.getClass().getClassLoader().getResources("");	
		boolean found = false;
		List<File> packageDirs = new LinkedList<>();
		while( l.hasMoreElements() && (!found) ){
			URL u = l.nextElement();
			logger.debug(u);
			// get the filepath of this packa
            System.out.println("Path: "+u.getPath());
			if( u.getPath().contains("refy") ){
				packageDirs.add( new  File(  u.getPath() + "/fxml" ) );
			}
		}
		
		// get only the .fxml files
		List<File> fxmlFiles = new LinkedList<>();
		for( File packageDir : packageDirs ){
			fxmlFiles.addAll( Arrays.asList( packageDir.listFiles( f -> f.getAbsolutePath().endsWith(".fxml"))) );
		}
		
		// add the corresponding nodes to the nodeMap
		for(File f:fxmlFiles ){
			System.out.println(f.getAbsolutePath());
			String justName = f.getAbsolutePath().substring( f.getAbsolutePath().lastIndexOf(FileSystems.getDefault().getSeparator())+1 , f.getAbsolutePath().length() );
			System.out.println("justName: " + justName);
			FXMLLoader fxmlLoader = new FXMLLoader(f.toURI().toURL());
			fxmlLoader.setControllerFactory(this.applicationContext::getBean);
			Parent n = fxmlLoader.load();
			if(n==null){
				logger.debug("Node is null");
			}
			logger.debug("Loaded view: " + justName );
			this.nodeMap.put(justName,  n );
		}
	}

}
