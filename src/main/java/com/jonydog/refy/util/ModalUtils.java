package com.jonydog.refy.util;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModalUtils {

    public static void showModal(Stage modalStage, Scene modalScene, String title){

        modalStage.setScene(modalScene);
        modalStage.setAlwaysOnTop(false);
        modalStage.setFullScreen(false);
        modalStage.setTitle(title);
        modalStage.showAndWait();
    }


}