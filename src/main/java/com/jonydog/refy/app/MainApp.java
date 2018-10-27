package com.jonydog.refy.app;

import com.jonydog.refy.jobs.ReferenceKeeper;
import com.jonydog.refy.util.AlertUtils;
import com.jonydog.refy.util.StageManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;

@SpringBootApplication
@ComponentScan(
        basePackages = {
                "com.jonydog.refy.controllers",
                "com.jonydog.refy.business",
                "com.jonydog.refy.daos",
                "com.jonydog.refy.statesources",
                "com.jonydog.refy.configs",
                "com.jonydog.refy.jobs"
        }
)
public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(String[] args){

        MainApp.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        StageManager.getInstance().setPrimaryStage(stage);

        // TODO Auto-generated method stub
        Scene scene = new Scene((Parent) this.rootNode, 900, 900);
        stage.setTitle("Refy");
        stage.setScene(scene);
        // stage.setMaximized(true);
        stage.show();


        stage.setOnCloseRequest(event -> {

            AlertUtils.confirmationAlert(
                    (dummy)->{},
                    (dummy)->{event.consume();},
                    "Exi",
                    "Exit Refy",
                    "Do you confirm exit from Refy?",
                    "Exit",
                    "Cancel",
                    stage
            );

        });

    }


    @Override
    public void init(){
        this.springContext = SpringApplication.run(MainApp.class);

        //init state sources
        /*for( StateSource s : this.springContext.getBeansOfType(StateSource.class).values() ){
            s.initStateSource();
        }*/

        try {
            StageManager.getInstance().setApplicationContext(springContext);
            StageManager.getInstance().loadAllViews();
            this.rootNode = StageManager.getInstance().getView("MainWindow.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set default language
        LocaleContextHolder.setDefaultLocale( new Locale("pt"));
    }


    @Override
    public void stop(){

        ReferenceKeeper referenceKeeper = this.springContext.getBean(ReferenceKeeper.class);

        Executors.newSingleThreadExecutor().submit(  referenceKeeper::writeReferencesToFile  );

        this.springContext.getBean(ReferenceKeeper.class).getIsAppClosed().set(true);

    }
}
