package dao;

import exception.BusinessException;
import model.Activiter;
import model.Coach;
import model.Salle;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de ActiviteDao.
 */
public class ActiviterDaoImpl implements ActiviterDao {

    private final CoachDao coachDao = new CoachDaoImpl();
    private final SalleDao salleDao = new SalleDaoImpl();

    @Override
    public Activiter create(Activiter activite) {
        // Validation métier
        if (activite == null) {
            throw new BusinessException("L'activité ne peut pas être null");
        }
        if (activite.getNomActivite() == null || activite.getNomActivite().isBlank()) {
            throw new BusinessException("Le nom de l'activité est obligatoire");
        }
        if (activite.getDateActivite() == null) {
            throw new BusinessException("La date de l'activité est obligatoire");
        }
        if (activite.getHeureDebut() == null || activite.getHeureFin() == null) {
            throw new BusinessException("Les heures de début et fin sont obligatoires");
        }
        if (activite.getHeureDebut().isAfter(activite.getHeureFin())) {
            throw new BusinessException("L'heure de début doit être avant l'heure de fin");
        }
        if (activite.getCoach() == null || activite.getCoach().getIdCoach() <= 0) {
            throw new BusinessException("Un coach est obligatoire pour l'activité");
        }

        String sql = "INSERT INTO activite (nom_activite, date_activite, heure_debut, heure_fin, id_coach, id_salle) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, activite.getNomActivite());
            pstmt.setDate(2, Date.valueOf(activite.getDateActivite()));
            pstmt.setTime(3, Time.valueOf(activite.getHeureDebut()));
            pstmt.setTime(4, Time.valueOf(activite.getHeureFin()));
            pstmt.setInt(5, activite.getCoach().getIdCoach());

            // Gestion salle optionnelle
            if (activite.getSalle() != null && activite.getSalle().getIdSalle() > 0) {
                pstmt.setInt(6, activite.getSalle().getIdSalle());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        activite.setIdActivite(generatedKeys.getInt(1));
                        System.out.println("✅ Activité créée avec l'ID : " + activite.getIdActivite());
                    }
                }
            }

            return activite;

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la création de l'activité", e);
        }
    }

    @Override
    public Activiter findById(int id) {
        String sql = "SELECT * FROM activite WHERE id_activite = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToActivite(rs);
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche de l'activité ID " + id, e);
        }

        return null;
    }

    @Override
    public List<Activiter> findAll() {
        List<Activiter> activites = new ArrayList<>();
        String sql = "SELECT * FROM activite ORDER BY date_activite DESC, heure_debut ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                activites.add(mapResultSetToActivite(rs));
            }

            System.out.println("✅ " + activites.size() + " activité(s) trouvée(s)");

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la récupération des activités", e);
        }

        return activites;
    }

    @Override
    public boolean update(Activiter activite) {
        if (activite == null || activite.getIdActivite() <= 0) {
            throw new BusinessException("Activité invalide pour la mise à jour");
        }

        String sql = "UPDATE activite SET nom_activite = ?, date_activite = ?, heure_debut = ?, heure_fin = ?, id_coach = ?, id_salle = ? WHERE id_activite = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, activite.getNomActivite());
            pstmt.setDate(2, Date.valueOf(activite.getDateActivite()));
            pstmt.setTime(3, Time.valueOf(activite.getHeureDebut()));
            pstmt.setTime(4, Time.valueOf(activite.getHeureFin()));
            pstmt.setInt(5, activite.getCoach().getIdCoach());

            if (activite.getSalle() != null && activite.getSalle().getIdSalle() > 0) {
                pstmt.setInt(6, activite.getSalle().getIdSalle());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            pstmt.setInt(7, activite.getIdActivite());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Activité mise à jour : ID " + activite.getIdActivite());
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la mise à jour de l'activité", e);
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM activite WHERE id_activite = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Activité supprimée : ID " + id);
                return true;
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la suppression de l'activité ID " + id, e);
        }

        return false;
    }

    @Override
    public List<Activiter> findByCoach(int idCoach) {
        List<Activiter> activites = new ArrayList<>();
        String sql = "SELECT * FROM activite WHERE id_coach = ? ORDER BY date_activite DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCoach);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    activites.add(mapResultSetToActivite(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche des activités par coach", e);
        }

        return activites;
    }

    @Override
    public List<Activiter> findByDate(LocalDate date) {
        List<Activiter> activites = new ArrayList<>();
        String sql = "SELECT * FROM activite WHERE date_activite = ? ORDER BY heure_debut ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    activites.add(mapResultSetToActivite(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche des activités par date", e);
        }

        return activites;
    }

    @Override
    public List<Activiter> findBySalle(int idSalle) {
        List<Activiter> activites = new ArrayList<>();
        String sql = "SELECT * FROM activite WHERE id_salle = ? ORDER BY date_activite DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idSalle);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    activites.add(mapResultSetToActivite(rs));
                }
            }

        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche des activités par salle", e);
        }

        return activites;
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet vers Activite.
     * Charge aussi les objets Coach et Salle associés.
     */
    private Activiter mapResultSetToActivite(ResultSet rs) throws SQLException {
        Activiter activite = new Activiter();
        activite.setIdActivite(rs.getInt("id_activite"));
        activite.setNomActivite(rs.getString("nom_activite"));
        activite.setDateActivite(rs.getDate("date_activite").toLocalDate());
        activite.setHeureDebut(rs.getTime("heure_debut").toLocalTime());
        activite.setHeureFin(rs.getTime("heure_fin").toLocalTime());

        // Chargement du coach
        int idCoach = rs.getInt("id_coach");
        Coach coach = coachDao.findById(idCoach);
        activite.setCoach(coach);

        // Chargement de la salle (optionnel)
        int idSalle = rs.getInt("id_salle");
        if (!rs.wasNull() && idSalle > 0) {
            Salle salle = salleDao.findById(idSalle);
            activite.setSalle(salle);
        }

        return activite;
    }
}
