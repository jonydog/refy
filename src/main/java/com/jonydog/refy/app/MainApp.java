package com.jonydog.refy.app;

import com.jonydog.refy.util.StageManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.Locale;

@SpringBootApplication
@EntityScan(basePackages={"com.jonydog.refy.model"})
@EnableJpaRepositories(basePackages = {"com.jonydog.refy.daos"})
@ComponentScan(
        basePackages = {
                "com.jonydog.refy.controllers",
                "com.jonydog.refy.business",
                "com.jonydog.refy.statesources",
                "com.jonydog.refy.configs"
        }
)
public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    @Override
    public void start(Stage stage) throws Exception {

        StageManager.getInstance().setPrimaryStage(stage);

        // TODO Auto-generated method stub
        Scene scene = new Scene((Parent) this.rootNode, 900, 900);
        stage.setTitle("refy");
        stage.setScene(scene);
        // stage.setMaximized(true);
        stage.show();


    }


    @Override
    public void init(){
        this.springContext = SpringApplication.run(MainApp.class);
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
}
