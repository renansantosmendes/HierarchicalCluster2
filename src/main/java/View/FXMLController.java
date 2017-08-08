/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author renansantos
 */
public class FXMLController implements Initializable {
@FXML
    private Label label;
    
    @FXML
    private TextField login;
    
    @FXML
    private PasswordField password;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        //label.setText("Hello World!");
    }

    @FXML
    private void login(ActionEvent event) {
        if (login.getText().equals("admin") && password.getText().equals("admin")) {
            JOptionPane.showMessageDialog(null, "Fez login", "", JOptionPane.INFORMATION_MESSAGE);
             try {
                new Principal().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Login e/ou senha inválidos", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        //JOptionPane.showMessageDialog(null, "Fez login", "", JOptionPane.INFORMATION_MESSAGE);
        //label.setText("Hello World!");
    }

    @FXML
    private void exitLogin(ActionEvent event) {
        System.exit(0);
        //label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
}
