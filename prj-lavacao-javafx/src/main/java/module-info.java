module br.edu.ifsc.fln.prjlavacaojavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires static lombok;

    opens br.edu.ifsc.fln to javafx.fxml;
    exports br.edu.ifsc.fln;
}