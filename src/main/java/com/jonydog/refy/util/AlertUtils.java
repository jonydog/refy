package com.jonydog.refy.util;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertUtils {

    private static String NEW_LINE = System.getProperty("line.separator");

    public static void popUpAlert(RefyErrors errors, Alert.AlertType type, Stage ownerStage) {

        Alert alert = new Alert(type);
        alert.setTitle( "Error" );
        //alert.setHeaderText( errors.getErrorsTitle() );
        alert.setResizable(true);
        alert.initOwner(ownerStage);
        alert.initModality(Modality.APPLICATION_MODAL);


        StringBuilder sb = new StringBuilder();
        for(String message: errors.getErrorMessages() ){
            sb.append( message + NEW_LINE );
        }
        alert.setContentText(sb.toString());
        //finally show the alert
        alert.show();
    }

    public static void popInformation(String title, String message, Stage ownerStage){

        Alert alert = new Alert( Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.initOwner(ownerStage);
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.show();
    }


}