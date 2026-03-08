import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.DatabaseConnection;

import java.io.IOException;

/**
 * Point d'entrée de l'application Gym Manager.
 * Lance l'interface JavaFX.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println(" ");
        System.out.println("   DÉMARRAGE DE GYM MANAGER");
        System.out.println(" \n");

        // Test de connexion à la base de données
        try {
            DatabaseConnection.testConnection();
            System.out.println();
        } catch (Exception e) {
            System.err.println("❌ ERREUR : Impossible de se connecter à la base de données !");
            System.err.println("Vérifiez que WAMP/XAMPP est démarré et que la base 'gym_manager' existe.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // Chargement du fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/fxml/main-view.fxml")
            );

            Scene scene = new Scene(fxmlLoader.load(), 900, 600);

            // Configuration de la fenêtre principale
            primaryStage.setTitle("Gym Manager - Système de gestion");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(500);

            // Gestion de la fermeture de l'application
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("\n ");
                System.out.println("   FERMETURE DE L'APPLICATION");
                System.out.println(" ");
                DatabaseConnection.closeConnection();
                System.out.println("✅ Application fermée proprement");
            });

            // Affichage de la fenêtre
            primaryStage.show();

            System.out.println("✅ Interface JavaFX chargée avec succès");
            System.out.println(" \n");

        } catch (IOException e) {
            System.err.println("❌ ERREUR : Impossible de charger l'interface !");
            e.printStackTrace();

            // Affichage d'une erreur à l'utilisateur
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle("Erreur de chargement");
            alert.setHeaderText("Impossible de charger l'interface");
            alert.setContentText("Détails : " + e.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
    }

    /**
     * Méthode principale.
     * Lance l'application JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }
}