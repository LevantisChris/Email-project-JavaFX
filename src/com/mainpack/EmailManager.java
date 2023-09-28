package com.mainpack;

import com.mainpack.controller.services.FetchFolderService;
import com.mainpack.model.EmailAccount;
import com.mainpack.model.EmailTreeItem;
import javafx.scene.control.TreeItem;

public class EmailManager {

    ///Folder hanging:
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");

    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        treeItem.setExpanded(true);
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem);
        fetchFolderService.start();
        foldersRoot.getChildren().add(treeItem);
    }
}
