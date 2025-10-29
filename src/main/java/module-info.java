module com.globemed.healthcare {
    // JavaFX Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // UI Libraries from POM
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.kordamp.ikonli.fontawesome5;


    	// JDBC / SQL
	requires java.sql;

    // Export the main package to be launched
    exports com.globemed.healthcare;

    // Open specific packages for reflection
    opens com.globemed.healthcare.controller to javafx.fxml;
    opens com.globemed.healthcare.model to javafx.base;
    opens com.globemed.healthcare.util;
}