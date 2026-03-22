module br.edu.ifsc.fln.prjlavacaojavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens br.edu.ifsc.fln to javafx.fxml;
    exports br.edu.ifsc.fln;
}