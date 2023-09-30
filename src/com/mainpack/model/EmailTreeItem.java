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
        emailMassages.add(emailMessage);
        if(!messageIsRead) {
            incrementMessagesCount();
        }
        System.out.println("MESSAGE added to " + name + " " + message.getSubject());
    }

    public void incrementMessagesCount() {
        unreadMessagesCount++;
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
