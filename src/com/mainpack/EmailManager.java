package com.mainpack;

import com.mainpack.controller.services.FetchFolderService;
import com.mainpack.controller.services.FolderUpdaterService;
import com.mainpack.model.EmailAccount;
import com.mainpack.model.EmailTreeItem;
import javafx.scene.control.TreeItem;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

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
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        treeItem.setExpanded(true);
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, folderList);
        fetchFolderService.start();
        foldersRoot.getChildren().add(treeItem);
    }
}
