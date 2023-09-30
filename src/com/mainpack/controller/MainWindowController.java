package com.mainpack.controller;

import com.mainpack.EmailManager;
import com.mainpack.controller.services.MessageRendererService;
import com.mainpack.model.EmailMessage;
import com.mainpack.model.EmailTreeItem;
import com.mainpack.model.SizeInteger;
import com.mainpack.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

    @FXML
    private WebView emailWebView;

    @FXML
    private TableView<EmailMessage> emailsTableView;

    @FXML
    private TreeView<String> emailsTreeView;

    /////
    @FXML
    private TableColumn<EmailMessage, Date> dateCol;

    @FXML
    private TableColumn<EmailMessage, String> recipientCol;

    @FXML
    private TableColumn<EmailMessage, String> senderCol;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeCol;

    @FXML
    private TableColumn<EmailMessage, String> subjectCol;
    /////

    private MessageRendererService messageRendererService;

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void optionsAction() {
        viewFactory.showOptionsWindow();
    }

    @FXML
    void addAccountAction() {
        System.out.println("Login Window Reopen (Add account action) ...");
        viewFactory.showLoginWindow();
    }


    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(e -> {
            EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();
            if(item != null) {
                emailsTableView.setItems(item.getEmailMassages());
            }
        });
    }

    /* This func is called when the class is used, immediately */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpEmailsTreeView(); // set up what happens when we press in the folder ...
        setUpEmailsTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRendererService();
        setUpMessageSelection();
    }

    private void setUpMessageRendererService() {
        messageRendererService = new MessageRendererService(emailWebView.getEngine());
    }

    private void setUpMessageSelection() {
        System.out.println("\n\n\nKALEESSTIKE\n\n\n");
        emailWebView.setOnMouseClicked(event -> {
            EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();
            if(emailMessage != null) {
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart();
            }
        });
    }

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFoldersRoot());
        emailsTreeView.setShowRoot(false);
    }

    private void setUpEmailsTableView() {
        senderCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("Sender")));
        subjectCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("subject")));
        recipientCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("recipient")));
        sizeCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, SizeInteger>("size")));
        dateCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, Date>("date")));
    }

    private void setUpBoldRows() {
        emailsTableView.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> param) {
                return new TableRow<EmailMessage>() {
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null) {
                            if(item.getIsRead()) {
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };
            }
        });
    }
}