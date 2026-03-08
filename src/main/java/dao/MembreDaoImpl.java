package dao;

import exception.BusinessException;
import model.Membre;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface MembreDao.
 * Gère les opérations CRUD sur les membres via JDBC.
 */
public class MembreDaoImpl implements MembreDao {

    /**
     * Crée un nouveau membre dans la base de données.
     *
     * @param membre Le membre à créer
     * @return Le membre créé avec son ID généré
     */
    @Override
    public Membre create(Membre membre) {
        // Validation métier
        if (membre == null) {
            throw new BusinessException("Le membre ne peut pas être null");
        }
        if (membre.getNomMembre() == null || membre.getNomMembre().isBlank()) {
            throw new BusinessException("Le nom du membre est obligatoire");
        }
        if (membre.getEmailMembre() == null || membre.getEmailMembre().isBlank()) {
            throw new BusinessException("L'email du membre est obligatoire");
        }

        // Requête SQL préparée (SÉCURITÉ)
        String sql = "INSERT INTO membre (nom_membre, email_membre, phone, id_abonnement) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Paramétrage de la requête
            pstmt.setString(1, membre.getNomMembre());
            pstmt.setString(2, membre.getEmailMembre());
            pstmt.setString(3, membre.getPhone());

            // Gestion du NULL pour id_abonnement
            if (membre.getIdAbonnement() != null) {
                pstmt.setInt(4, membre.getIdAbonnement());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }

            // Exécution
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Récupération de l'ID généré
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        membre.setIdMembre(generatedKeys.getInt(1));
                        System.out.println("✅ Membre créé avec l'ID : " + membre.getIdMembre());
                    }
                }
            }

            return membre;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BusinessException("Email déjà utilisé : " + membre.getEmailMembre());
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la création du membre", e);
        }
    }

    /**
     * Récupère un membre par son ID.
     *
     * @param id L'identifiant du membre
     * @return Le membre trouvé, ou null si inexistant
     */
    @Override
    public Membre findById(int id) {
        String sql = "SELECT * FROM membre WHERE id_membre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMembre(rs);
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche du membre ID " + id, e);
        }

        return null;
    }

    /**
     * Récupère tous les membres.
     *
     * @return Liste de tous les membres
     */
    @Override
    public List<Membre> findAll() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membre ORDER BY nom_membre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }

            System.out.println("✅ " + membres.size() + " membre(s) trouvé(s)");

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la récupération des membres", e);
        }

        return membres;
    }

    /**
     * Met à jour un membre existant.
     *
     * @param membre Le membre avec les nouvelles données
     * @return true si la mise à jour a réussi
     */
    @Override
    public boolean update(Membre membre) {
        // Validation métier
        if (membre == null || membre.getIdMembre() <= 0) {
            throw new BusinessException("Membre invalide pour la mise à jour");
        }

        String sql = "UPDATE membre SET nom_membre = ?, email_membre = ?, phone = ?, id_abonnement = ? WHERE id_membre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membre.getNomMembre());
            pstmt.setString(2, membre.getEmailMembre());
            pstmt.setString(3, membre.getPhone());

            if (membre.getIdAbonnement() != null) {
                pstmt.setInt(4, membre.getIdAbonnement());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }

            pstmt.setInt(5, membre.getIdMembre());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Membre mis à jour : ID " + membre.getIdMembre());
                return true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BusinessException("Email déjà utilisé : " + membre.getEmailMembre());
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la mise à jour du membre", e);
        }

        return false;
    }

    /**
     * Supprime un membre par son ID.
     *
     * @param id L'identifiant du membre à supprimer
     * @return true si la suppression a réussi
     */
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM membre WHERE id_membre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Membre supprimé : ID " + id);
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la suppression du membre ID " + id, e);
        }

        return false;
    }

    /**
     * Recherche des membres par nom (recherche partielle).
     *
     * @param nom Le nom à rechercher
     * @return Liste des membres correspondants
     */
    @Override
    public List<Membre> findByNom(String nom) {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membre WHERE nom_membre LIKE ? ORDER BY nom_membre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    membres.add(mapResultSetToMembre(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche par nom", e);
        }

        return membres;
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet vers un objet Membre.
     *
     * @param rs Le ResultSet à mapper
     * @return Un objet Membre
     * @throws SQLException si erreur lors de la lecture
     */
    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        Membre membre = new Membre();
        membre.setIdMembre(rs.getInt("id_membre"));
        membre.setNomMembre(rs.getString("nom_membre"));
        membre.setEmailMembre(rs.getString("email_membre"));
        membre.setPhone(rs.getString("phone"));

        // Gestion du NULL pour id_abonnement
        int idAbonnement = rs.getInt("id_abonnement");
        if (!rs.wasNull()) {
            membre.setIdAbonnement(idAbonnement);
        }

        return membre;
    }
}
