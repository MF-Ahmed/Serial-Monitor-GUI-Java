/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Farhan
 */

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class FX_Test_GUI extends Application {
    @Override
    public void start (Stage PrimaryStage)
    {
        Button btok = new Button("ok");
        Scene scene = new Scene(btok, 200, 250);
        PrimaryStage.setTitle("JavaFX Application");
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
        
    
    
    }
    public static void main (String[] args)
    {
        Application.launch();
    }
            
}

