package com.mainpack.controller;

import com.mainpack.EmailManager;
import com.mainpack.controller.services.EmailSenderService;
import com.mainpack.model.EmailAccount;
import com.mainpack.view.ViewFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ComposeMessageController extends BaseController implements Initializable {

    @FXML
    private Label errorLabel;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private TextField recipientTextField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;

    @FXML
    void sentButtonAction() {
        System.out.println("Send Button pressed");
        EmailSenderService emailSenderService = new EmailSenderService(emailAccountChoice.getValue(), subjectTextField.getText(),
                                                                       recipientTextField.getText(), htmlEditor.getHtmlText());
        emailSenderService.start();
        emailSenderService.setOnSucceeded(e -> {
            EmailSendingResult result = emailSenderService.getValue();
            switch (result) {
                case SUCCESS:
                    Stage stage = (Stage) recipientTextField.getScene().getWindow();
                    viewFactory.closeStage(stage);
                    break;
                case FAILED_BY_PROVIDER:
                    errorLabel.setText("Provider error");
                    break;
                case FAILED_BY_UNEXPRECTED_ERROR:
                    errorLabel.setText("An unexpected error occured");
                    break;
            }
        });
    }

    public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailAccountChoice.setItems(emailManager.getEmailAccountObservable());
        emailAccountChoice.setValue(emailManager.getEmailAccountObservable().get(0));
    }
}
