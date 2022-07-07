package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.Loader;
import util.ValidationUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SignUpFormController implements Loader {
    public TextField txtFullName;
    public TextField txtEmail;
    public TextField txtUserName;
    public PasswordField pwdPassword;
    public AnchorPane context2;
    public JFXButton btnCreateAccount;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {

        btnCreateAccount.setDisable(true);
        //add pattern and text to the map
        //Create a pattern and compile it to use
        Pattern fullNamePattern = Pattern.compile("^[A-Z][A-z ]{3,30}$");
        Pattern emailPattern = Pattern.compile("^[A-z]{3,30}@gmail.com$");
        Pattern userNamePattern = Pattern.compile("^[A-Z][A-z ]{3,15}$");
        Pattern passwordPattern = Pattern.compile("^.*[A-z].*[0-9].*[!@#$%^&*()_]$");

        map.put(txtFullName, fullNamePattern);
        map.put(txtEmail, emailPattern);
        map.put(txtUserName, userNamePattern);
        map.put(pwdPassword, passwordPattern);
    }


    public void textFields_Key_Released(KeyEvent keyEvent) throws IOException {
        ValidationUtil.validate(map,btnCreateAccount);
        //TextField = error
        //boolean // validation ok

        //if the enter key pressed
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnCreateAccount);
            //if the response is a text field
            //that means there is a error
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();// if there is a error just focus it
            } else if (response instanceof Boolean) {
                //loadUi("DashBoardForm1");
            }
        }
    }



    public void createAccountOnAction(ActionEvent actionEvent) throws IOException {

        loadUi("DashBoardForm1");
        clearAllTexts();
    }

    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("LoginForm");
    }

    public void loadUi(String location) throws IOException {
        Stage stage = (Stage) context2.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void clearAllTexts() {
        txtFullName.clear();
        txtEmail.clear();
        txtUserName.clear();
        pwdPassword.clear();
        txtFullName.requestFocus();
        setBorders(txtFullName,txtEmail,txtUserName,pwdPassword);
    }


    //reset border colors to default color
    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.getParent().setStyle("-fx-border-color: #4c4949");
        }
    }
}
