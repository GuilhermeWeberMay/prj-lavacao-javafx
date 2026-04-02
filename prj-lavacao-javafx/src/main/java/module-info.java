module br.edu.ifsc.fln {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires static lombok;
    requires java.logging;

    opens br.edu.ifsc.fln to javafx.fxml;
    opens br.edu.ifsc.fln.model.domain to javafx.base;
    exports br.edu.ifsc.fln;
}