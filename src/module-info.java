module EmailProject {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires activation;
    requires java.mail;

    opens com.mainpack;
    opens com.mainpack.view;
    opens com.mainpack.controller;
    opens com.mainpack.model;

}