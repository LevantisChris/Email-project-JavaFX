package com.mainpack.controller;

import com.mainpack.EmailManager;
import com.mainpack.controller.services.LoginService;
import com.mainpack.model.EmailAccount;
import com.mainpack.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController extends BaseController {

    @FXML
    private TextField emailAddressField;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField passwordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void loginButtonAction() {
        if(fieldsAreValid()) {
            EmailAccount emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);
            loginService.start();
            loginService.setOnSucceeded(event -> {

                EmailLoginResult loginResult = loginService.getValue();

                switch (loginResult) {
                    case SUCCESS:
                        System.out.println("SUCCESS ...");
                        System.out.println("Log in button Clicked ...");
                        viewFactory.showMainWindow();
                        Stage stage = (Stage) errorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage); // We need a reference to the stage to close it ...
                        return;
                    case FAILED_BY_CREDENTIALS:
                        System.out.println("FAILED_BY_CREDENTIALS ...");
                        errorLabel.setText("Wrong credentials");
                        return;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        System.out.println("FAILED_BY_UNEXPECTED_ERROR ...");
                        errorLabel.setText("An unexpected error occured");
                        return;
                    case FAILED_BY_NETWORK:
                        System.out.println("FAILED_BY_NETWORK ...");
                        errorLabel.setText("No internet conection");
                        return;
                }

            });
        }
    }

    private boolean fieldsAreValid() {
        if (emailAddressField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText("Please fill all the credentials");
            return false;
        } else
            return true;
    }
}