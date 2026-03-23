module br.edu.ifsc.fln {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires static lombok;
    requires java.logging;

    opens br.edu.ifsc.fln to javafx.fxml;
    exports br.edu.ifsc.fln;
}