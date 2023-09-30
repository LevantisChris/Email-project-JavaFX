package com.mainpack.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailTreeItem<String> extends TreeItem<String> {

    private String name;
    private ObservableList<EmailMessage> emailMassages;
    private int unreadMessagesCount;

    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
        this.emailMassages = FXCollections.observableArrayList(); // generate the List
    }

    public ObservableList<EmailMessage> getEmailMassages() {
        return emailMassages;
    }

    public void addEmail(Message message) throws MessagingException {
        EmailMessage emailMessage = fetchMessage(message);
        emailMassages.add(emailMessage);
    }

    public void addEmailToTop(Message message) throws MessagingException {
        EmailMessage emailMessage = fetchMessage(message);
        emailMassages.add(0, emailMessage);
    }

    private EmailMessage fetchMessage(Message message) throws MessagingException {
        boolean messageIsRead = message.getFlags().contains(Flags.Flag.SEEN);
        EmailMessage emailMessage = new EmailMessage(
                message.getSubject(),
                message.getFrom()[0].toString(),
                message.getRecipients(MimeMessage.RecipientType.TO)[0].toString(),
                message.getSize(),
                message.getSentDate(),
                messageIsRead,
                message
        );
        if(!messageIsRead) {
            incrementMessagesCount();
        }
        return emailMessage;
    }

    public void incrementMessagesCount() {
        unreadMessagesCount++;
        updateName();
    }

    public void decreamentMessageCount() {
        unreadMessagesCount--;
        updateName();
    }

    private void updateName() {
        if(unreadMessagesCount > 0) {
            this.setValue((String) (name + "(" + unreadMessagesCount + ")"));
        } else {
            this.setValue(name);
        }
    }
}
