package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class createAccount {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Label errorUsername;

    @FXML
    private Label errorPassword;

    @FXML
    private RadioButton cartesien;

    @FXML
    private RadioButton hedoniste;

    @FXML
    private RadioButton cynique;

    @FXML
    private Button valider;

    @FXML
    protected void initialize() {
    }

    @FXML
    public void validateData(){

    }
}
