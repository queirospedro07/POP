module com.sporthub {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.sporthub to javafx.fxml;
    opens com.sporthub.model to javafx.base;
    opens com.sporthub.ui to javafx.fxml;

    exports com.sporthub;
    exports com.sporthub.model;
    exports com.sporthub.service;
    exports com.sporthub.data;
    exports com.sporthub.ui;
}
