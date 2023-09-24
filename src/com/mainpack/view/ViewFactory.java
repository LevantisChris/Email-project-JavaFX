package com.mainpack.view;

import com.mainpack.EmailManager;

public class ViewFactory {

    private EmailManager emailManager;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public void showLoginWindow() {
        System.out.println("Show Login window called ...");
    }

}
