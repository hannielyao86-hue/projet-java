package dao;

import exception.BusinessException;
import model.Abonnement;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de AbonnementDao.
 */
public class AbonnementDaoImpl implements AbonnementDao {

    @Override
    public Abonnement create(Abonnement abonnement) {
        if (abonnement == null) {
            throw new BusinessException("L'abonnement ne peut pas être null");
        }
        if (abonnement.getNomAbonnement() == null || abonnement.getNomAbonnement().isBlank()) {
            throw new BusinessException("Le nom de l'abonnement est obligatoire");
        }
        if (abonnement.getDateDebut() == null || abonnement.getDateFin() == null) {
            throw new BusinessException("Les dates de début et fin sont obligatoires");
        }
        if (abonnement.getDateDebut().isAfter(abonnement.getDateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }

        String sql = "INSERT INTO abonnement (nom_abonnement, date_debut, date_fin, statut_abonnement) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, abonnement.getNomAbonnement());
            pstmt.setDate(2, Date.valueOf(abonnement.getDateDebut()));
            pstmt.setDate(3, Date.valueOf(abonnement.getDateFin()));
            pstmt.setString(4, abonnement.getStatutAbonnement());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        abonnement.setIdAbonnement(generatedKeys.getInt(1));
                        System.out.println("✅ Abonnement créé avec l'ID : " + abonnement.getIdAbonnement());
                    }
                }
            }

            return abonnement;

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la création de l'abonnement", e);
        }
    }

    @Override
    public Abonnement findById(int id) {
        String sql = "SELECT * FROM abonnement WHERE id_abonnement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAbonnement(rs);
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche de l'abonnement ID " + id, e);
        }

        return null;
    }

    @Override
    public List<Abonnement> findAll() {
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM abonnement ORDER BY date_debut DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                abonnements.add(mapResultSetToAbonnement(rs));
            }

            System.out.println("✅ " + abonnements.size() + " abonnement(s) trouvé(s)");

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la récupération des abonnements", e);
        }

        return abonnements;
    }

    @Override
    public boolean update(Abonnement abonnement) {
        if (abonnement == null || abonnement.getIdAbonnement() <= 0) {
            throw new BusinessException("Abonnement invalide pour la mise à jour");
        }

        String sql = "UPDATE abonnement SET nom_abonnement = ?, date_debut = ?, date_fin = ?, statut_abonnement = ? WHERE id_abonnement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, abonnement.getNomAbonnement());
            pstmt.setDate(2, Date.valueOf(abonnement.getDateDebut()));
            pstmt.setDate(3, Date.valueOf(abonnement.getDateFin()));
            pstmt.setString(4, abonnement.getStatutAbonnement());
            pstmt.setInt(5, abonnement.getIdAbonnement());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Abonnement mis à jour : ID " + abonnement.getIdAbonnement());
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la mise à jour de l'abonnement", e);
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM abonnement WHERE id_abonnement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Abonnement supprimé : ID " + id);
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la suppression de l'abonnement ID " + id, e);
        }

        return false;
    }

    @Override
    public List<Abonnement> findByStatut(String statut) {
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM abonnement WHERE statut_abonnement = ? ORDER BY date_debut DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    abonnements.add(mapResultSetToAbonnement(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche par statut", e);
        }

        return abonnements;
    }

    private Abonnement mapResultSetToAbonnement(ResultSet rs) throws SQLException {
        Abonnement abonnement = new Abonnement();
        abonnement.setIdAbonnement(rs.getInt("id_abonnement"));
        abonnement.setNomAbonnement(rs.getString("nom_abonnement"));
        abonnement.setDateDebut(rs.getDate("date_debut").toLocalDate());
        abonnement.setDateFin(rs.getDate("date_fin").toLocalDate());
        abonnement.setStatutAbonnement(rs.getString("statut_abonnement"));
        return abonnement;
    }
}
