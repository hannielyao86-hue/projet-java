package dao;

import exception.BusinessException;
import model.Coach;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de CoachDao.
 */
public class CoachDaoImpl implements CoachDao {

    @Override
    public Coach create(Coach coach) {
        // Validation métier
        if (coach == null) {
            throw new BusinessException("Le coach ne peut pas être null");
        }
        if (coach.getNomCoach() == null || coach.getNomCoach().isBlank()) {
            throw new BusinessException("Le nom du coach est obligatoire");
        }
        if (coach.getEmailCoach() == null || coach.getEmailCoach().isBlank()) {
            throw new BusinessException("L'email du coach est obligatoire");
        }

        String sql = "INSERT INTO coach (nom_coach, email_coach, specialite) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, coach.getNomCoach());
            pstmt.setString(2, coach.getEmailCoach());
            pstmt.setString(3, coach.getSpecialite());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        coach.setIdCoach(generatedKeys.getInt(1));
                        System.out.println("✅ Coach créé avec l'ID : " + coach.getIdCoach());
                    }
                }
            }

            return coach;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BusinessException("Email déjà utilisé : " + coach.getEmailCoach());
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la création du coach", e);
        }
    }

    @Override
    public Coach findById(int id) {
        String sql = "SELECT * FROM coach WHERE id_coach = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCoach(rs);
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche du coach ID " + id, e);
        }

        return null;
    }

    @Override
    public List<Coach> findAll() {
        List<Coach> coachs = new ArrayList<>();
        String sql = "SELECT * FROM coach ORDER BY nom_coach";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                coachs.add(mapResultSetToCoach(rs));
            }

            System.out.println("✅ " + coachs.size() + " coach(s) trouvé(s)");

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la récupération des coachs", e);
        }

        return coachs;
    }

    @Override
    public boolean update(Coach coach) {
        if (coach == null || coach.getIdCoach() <= 0) {
            throw new BusinessException("Coach invalide pour la mise à jour");
        }

        String sql = "UPDATE coach SET nom_coach = ?, email_coach = ?, specialite = ? WHERE id_coach = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, coach.getNomCoach());
            pstmt.setString(2, coach.getEmailCoach());
            pstmt.setString(3, coach.getSpecialite());
            pstmt.setInt(4, coach.getIdCoach());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Coach mis à jour : ID " + coach.getIdCoach());
                return true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BusinessException("Email déjà utilisé : " + coach.getEmailCoach());
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la mise à jour du coach", e);
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM coach WHERE id_coach = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Coach supprimé : ID " + id);
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la suppression du coach ID " + id, e);
        }

        return false;
    }

    @Override
    public List<Coach> findBySpecialite(String specialite) {
        List<Coach> coachs = new ArrayList<>();
        String sql = "SELECT * FROM coach WHERE specialite LIKE ? ORDER BY nom_coach";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + specialite + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    coachs.add(mapResultSetToCoach(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche par spécialité", e);
        }

        return coachs;
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet vers Coach.
     */
    private Coach mapResultSetToCoach(ResultSet rs) throws SQLException {
        Coach coach = new Coach();
        coach.setIdCoach(rs.getInt("id_coach"));
        coach.setNomCoach(rs.getString("nom_coach"));
        coach.setEmailCoach(rs.getString("email_coach"));
        coach.setSpecialite(rs.getString("specialite"));
        return coach;
    }
}
