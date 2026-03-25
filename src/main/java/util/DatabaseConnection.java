package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour gérer la connexion à MySQL.
 * Utilise le pattern Singleton.
 */
public class DatabaseConnection {

    // Configuration  de MySQL
    private static final String DB_URL = System.getProperty("test.env") != null ?
        "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1" : "jdbc:mysql://localhost:3306/gym_manager";
    private static final String DB_USER = System.getProperty("test.env") != null ? "sa" : "root";
    private static final String DB_PASSWORD = System.getProperty("test.env") != null ? "" : ""; // Vide par défaut pour WAMP

    // Cette classe ne conserve plus de connexion singleton.
    // Chaque appel à getConnection() crée et retourne une nouvelle connexion.

    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private DatabaseConnection() {
    }

    /**
     * Retourne une nouvelle connexion à MySQL.
     *
     * @return Connection active
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (System.getProperty("test.env") != null) {
                Class.forName("org.h2.Driver");
            } else {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver de base de données non trouvé !");
            throw new SQLException("Driver non disponible", e);
        }

        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        if (System.getProperty("test.env") != null) {
            System.out.println("✅ Connexion H2 réussie : testdb");
        } else {
            System.out.println("✅ Connexion MySQL réussie : gym_manager");
        }
        return conn;
    }

    /**
     * Ferme la connexion à MySQL.
     * Méthode sans effet maintenant car les connexions sont gérées par try-with-resources.
     * À appeler à la fin de l'application.
     */
    public static void closeConnection() {
        // Les connexions sont maintenant fermées automatiquement par try-with-resources
        // dans chaque DAO. Cette méthode n'a plus besoin de faire quoi que ce soit.
        System.out.println("✅ Gestion des connexions MySQL terminée");
    }

    /**
     * Teste la connexion à MySQL.
     */
    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Test de connexion MySQL : SUCCÈS");
            }
        } catch (SQLException e) {
            System.err.println("❌ Test de connexion MySQL : ÉCHEC");
            e.printStackTrace();
        }
    }
}