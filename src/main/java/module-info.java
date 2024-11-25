module org.example.jawa {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires annotations;
    requires org.apache.logging.log4j;

    opens org.example.jawa to javafx.fxml;
    exports org.example.jawa;
    exports org.example.jawa.Login;
    opens org.example.jawa.Login to javafx.fxml;
    opens org.example.jawa.face to javafx.fxml;
    opens org.example.jawa.chat to javafx.fxml;
}