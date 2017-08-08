/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author renansantos
 */
public class JavaFXML extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        System.out.println(getClass().toString());
//        URL url = this.getClass().getResource("FXMLDocument2.fxml");
//        System.out.println(getClass().getResource("file.txt"));
//        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument2.fxml"));
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();

        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(400, 300);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
