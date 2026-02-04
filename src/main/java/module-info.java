module org.example.projetjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.projetjava to javafx.fxml;
    exports org.example.projetjava;
}