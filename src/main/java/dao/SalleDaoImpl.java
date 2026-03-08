package dao;

import exception.BusinessException;
import model.Salle;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de SalleDao.
 */
public class SalleDaoImpl implements SalleDao {

    @Override
    public Salle create(Salle salle) {
        if (salle == null) {
            throw new BusinessException("La salle ne peut pas être null");
        }
        if (salle.getNomSalle() == null || salle.getNomSalle().isBlank()) {
            throw new BusinessException("Le nom de la salle est obligatoire");
        }
        if (salle.getCapacite() <= 0) {
            throw new BusinessException("La capacité doit être supérieure à 0");
        }

        String sql = "INSERT INTO salle (nom_salle, capacite) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, salle.getNomSalle());
            pstmt.setInt(2, salle.getCapacite());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        salle.setIdSalle(generatedKeys.getInt(1));
                        System.out.println("✅ Salle créée avec l'ID : " + salle.getIdSalle());
                    }
                }
            }

            return salle;

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la création de la salle", e);
        }
    }

    @Override
    public Salle findById(int id) {
        String sql = "SELECT * FROM salle WHERE id_salle = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSalle(rs);
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche de la salle ID " + id, e);
        }

        return null;
    }

    @Override
    public List<Salle> findAll() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle ORDER BY nom_salle";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salles.add(mapResultSetToSalle(rs));
            }

            System.out.println("✅ " + salles.size() + " salle(s) trouvée(s)");

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la récupération des salles", e);
        }

        return salles;
    }

    @Override
    public boolean update(Salle salle) {
        if (salle == null || salle.getIdSalle() <= 0) {
            throw new BusinessException("Salle invalide pour la mise à jour");
        }

        String sql = "UPDATE salle SET nom_salle = ?, capacite = ? WHERE id_salle = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, salle.getNomSalle());
            pstmt.setInt(2, salle.getCapacite());
            pstmt.setInt(3, salle.getIdSalle());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Salle mise à jour : ID " + salle.getIdSalle());
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la mise à jour de la salle", e);
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM salle WHERE id_salle = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Salle supprimée : ID " + id);
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la suppression de la salle ID " + id, e);
        }

        return false;
    }

    private Salle mapResultSetToSalle(ResultSet rs) throws SQLException {
        Salle salle = new Salle();
        salle.setIdSalle(rs.getInt("id_salle"));
        salle.setNomSalle(rs.getString("nom_salle"));
        salle.setCapacite(rs.getInt("capacite"));
        return salle;
    }
}
