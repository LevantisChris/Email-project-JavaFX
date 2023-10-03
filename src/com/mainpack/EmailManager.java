package com.mainpack;

import com.mainpack.controller.services.FetchFolderService;
import com.mainpack.controller.services.FolderUpdaterService;
import com.mainpack.model.EmailAccount;
import com.mainpack.model.EmailMessage;
import com.mainpack.model.EmailTreeItem;
import com.mainpack.view.IconResolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private IconResolver iconResolver = new IconResolver();

    private ObservableList<EmailAccount> emailAccountObservable = FXCollections.observableArrayList();

    public ObservableList<EmailAccount> getEmailAccountObservable() {
        return emailAccountObservable;
    }

    private EmailMessage selectedMessage;

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    private EmailTreeItem<String> selectedFolder;

    private FolderUpdaterService folderUpdaterService;
    ///Folder hanging:
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");

    private List<Folder> folderList = new ArrayList<Folder>();

    public List<Folder> getFolderList() {
        return this.folderList;
    }

    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        emailAccountObservable.add(emailAccount);
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        treeItem.setGraphic(iconResolver.getIconForFolder(emailAccount.getAddress()));
        treeItem.setExpanded(true);
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, folderList);
        fetchFolderService.start();
        foldersRoot.getChildren().add(treeItem);
    }

    public void setRead() {
        try {
            selectedMessage.setIsRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decreamentMessageCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUnRead() {
        try {
            selectedMessage.setIsRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
            selectedFolder.incrementMessagesCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSelectedMessage() {
        try {
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
            selectedFolder.getEmailMassages().remove(selectedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
