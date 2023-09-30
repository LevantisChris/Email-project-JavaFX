package com.mainpack.controller.services;

import com.mainpack.model.EmailMessage;
import com.mainpack.model.EmailTreeItem;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.List;

public class FetchFolderService extends Service<Void> {

    private Store store;
    private EmailTreeItem<String> foldersRoot;
    private List<Folder> folderList;

    public FetchFolderService(Store store, EmailTreeItem<String> foldersRoot, List<Folder> folderList) {
        this.store = store;
        this.foldersRoot = foldersRoot;
        this.folderList = folderList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    private void fetchFolders() throws MessagingException {
        Folder[] folders = store.getDefaultFolder().list();
        handleFolders(folders, foldersRoot);
    }

    private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        for (Folder folder: folders) {
            folderList.add(folder);
            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<String>(folder.getName());
            foldersRoot.getChildren().add((emailTreeItem));
            foldersRoot.setExpanded(true);
            fetchMassagesOnFolder(folder, emailTreeItem);
            addMessageListenerToFolder(folder, emailTreeItem);
            if(folder.getType() == Folder.HOLDS_FOLDERS) {
                Folder[] subFolders = folder.list();
                handleFolders(subFolders, emailTreeItem); //we call it again for the subfolders, recursion
            }
        }
    }

    private void addMessageListenerToFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                System.out.println("Message added event: " + e);
                for(int i = 0;i < e.getMessages().length;i++) {
                    try {
                        Message message = folder.getMessage(folder.getMessageCount() - i);
                        emailTreeItem.addEmailToTop(message); // because the default func addEmail add the message to the end
                    } catch (MessagingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            @Override
            public void messagesRemoved(MessageCountEvent e) {
                System.out.println("Message deleted event: " + e);
            }
        });
    }

    /* Get all the massages that are in the folders */
    private void fetchMassagesOnFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        Service fetchMassagesService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        if(folder.getType() != Folder.HOLDS_FOLDERS) {
                            folder.open(Folder.READ_WRITE);
                            int folderSize = folder.getMessageCount();
                            for(int i = folderSize; i > 0;i--) {
                                emailTreeItem.addEmail(folder.getMessage(i));
                            }
                        }
                        return null;
                    }
                };
            }
        };
        fetchMassagesService.start();
    }
}
