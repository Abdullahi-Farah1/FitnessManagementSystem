module edu.metrostate.fitnessmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires jdk.compiler;
    requires org.mariadb.jdbc;


    opens edu.metrostate.fitnessmanagementsystem to javafx.fxml;
    exports edu.metrostate.fitnessmanagementsystem;
}