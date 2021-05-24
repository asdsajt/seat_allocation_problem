module seat.allocation.problem {
    requires javafx.fxml;
    requires javafx.controls;
    requires controlsfx;
    requires lombok;
    requires json.simple;
    requires json;
    requires mongodb.driver;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens controller;
    opens database;
    opens globalControls;
    opens model;
    opens popup_window;
    opens solver;
    opens view;

}