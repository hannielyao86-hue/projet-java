package dao;

import exception.BusinessException;
import model.Activiter;
import model.Inscription;
import model.Membre;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de InscriptionDao.
 */
public class InscriptionDaoImpl implements InscriptionDao {

    private final MembreDao membreDao = new MembreDaoImpl();
    private final ActiviterDao activiterDao = new ActiviterDaoImpl();

    @Override
    public Inscription create(Inscription inscription) {
        // Validation métier
        if (inscription == null) {
            throw new BusinessException("L'inscription ne peut pas être null");
        }
        if (inscription.getMembre() == null || inscription.getMembre().getIdMembre() <= 0) {
            throw new BusinessException("Un membre est obligatoire pour l'inscription");
        }
        if (inscription.getActivite() == null || inscription.getActivite().getIdActivite() <= 0) {
            throw new BusinessException("Une activité est obligatoire pour l'inscription");
        }

        String sql = "INSERT INTO inscription (date_inscription, statut, id_membre, id_activite) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Si pas de date, utiliser aujourd'hui
            LocalDate dateInscription = inscription.getDateInscription() != null
                    ? inscription.getDateInscription()
                    : LocalDate.now();

            pstmt.setDate(1, Date.valueOf(dateInscription));
            pstmt.setString(2, inscription.getStatut());
            pstmt.setInt(3, inscription.getMembre().getIdMembre());
            pstmt.setInt(4, inscription.getActivite().getIdActivite());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inscription.setIdInscription(generatedKeys.getInt(1));
                        inscription.setDateInscription(dateInscription);
                        System.out.println("✅ Inscription créée avec l'ID : " + inscription.getIdInscription());
                    }
                }
            }

            return inscription;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BusinessException("Ce membre est déjà inscrit à cette activité");
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la création de l'inscription", e);
        }
    }

    @Override
    public Inscription findById(int id) {
        String sql = "SELECT * FROM inscription WHERE id_inscription = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInscription(rs);
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche de l'inscription ID " + id, e);
        }

        return null;
    }

    @Override
    public List<Inscription> findAll() {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = "SELECT * FROM inscription ORDER BY date_inscription DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                inscriptions.add(mapResultSetToInscription(rs));
            }

            System.out.println("✅ " + inscriptions.size() + " inscription(s) trouvée(s)");

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la récupération des inscriptions", e);
        }

        return inscriptions;
    }

    @Override
    public boolean update(Inscription inscription) {
        if (inscription == null || inscription.getIdInscription() <= 0) {
            throw new BusinessException("Inscription invalide pour la mise à jour");
        }

        String sql = "UPDATE inscription SET date_inscription = ?, statut = ?, id_membre = ?, id_activite = ? WHERE id_inscription = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(inscription.getDateInscription()));
            pstmt.setString(2, inscription.getStatut());
            pstmt.setInt(3, inscription.getMembre().getIdMembre());
            pstmt.setInt(4, inscription.getActivite().getIdActivite());
            pstmt.setInt(5, inscription.getIdInscription());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Inscription mise à jour : ID " + inscription.getIdInscription());
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la mise à jour de l'inscription", e);
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM inscription WHERE id_inscription = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Inscription supprimée : ID " + id);
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la suppression de l'inscription ID " + id, e);
        }

        return false;
    }

    @Override
    public List<Inscription> findByMembre(int idMembre) {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = "SELECT * FROM inscription WHERE id_membre = ? ORDER BY date_inscription DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idMembre);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    inscriptions.add(mapResultSetToInscription(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche des inscriptions par membre", e);
        }

        return inscriptions;
    }

    @Override
    public List<Inscription> findByActivite(int idActivite) {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = "SELECT * FROM inscription WHERE id_activite = ? ORDER BY date_inscription DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idActivite);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    inscriptions.add(mapResultSetToInscription(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche des inscriptions par activité", e);
        }

        return inscriptions;
    }

    @Override
    public List<Inscription> findByStatut(String statut) {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = "SELECT * FROM inscription WHERE statut = ? ORDER BY date_inscription DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    inscriptions.add(mapResultSetToInscription(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche des inscriptions par statut", e);
        }

        return inscriptions;
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet vers Inscription.
     * Charge aussi les objets Membre et Activite associés.
     */
    private Inscription mapResultSetToInscription(ResultSet rs) throws SQLException {
        Inscription inscription = new Inscription();
        inscription.setIdInscription(rs.getInt("id_inscription"));
        inscription.setDateInscription(rs.getDate("date_inscription").toLocalDate());
        inscription.setStatut(rs.getString("statut"));

        // Chargement du membre
        int idMembre = rs.getInt("id_membre");
        Membre membre = membreDao.findById(idMembre);
        inscription.setMembre(membre);

        // Chargement de l'activité
        int idActivite = rs.getInt("id_activite");
        Activiter activite = activiterDao.findById(idActivite);
        inscription.setActivite(activite);
        return inscription;
    }
}