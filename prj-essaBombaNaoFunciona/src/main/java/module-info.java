module br.edu.ifsc.fln.prjessabombanaofunciona {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires java.sql;
    requires static lombok;
    requires javafx.base;

    opens br.edu.ifsc.fln to javafx.fxml;
    exports br.edu.ifsc.fln;
    exports br.edu.ifsc.fln.controller;
    opens br.edu.ifsc.fln.controller to javafx.fxml;
    exports br.edu.ifsc.fln.model.domain;
    opens br.edu.ifsc.fln.model.domain to javafx.fxml;
}