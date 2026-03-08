package dao;

import model.Coach;
import java.util.List;

/**
 * Interface DAO pour la gestion des coachs.
 */
public interface CoachDao {

    /**
     * Crée un nouveau coach.
     */
    Coach create(Coach coach);

    /**
     * Récupère un coach par son ID.
     */
    Coach findById(int id);

    /**
     * Récupère tous les coachs.
     */
    List<Coach> findAll();

    /**
     * Met à jour un coach.
     */
    boolean update(Coach coach);

    /**
     * Supprime un coach.
     */
    boolean delete(int id);

    /**Recherche des coachs par spécialité.
     */
    List<Coach> findBySpecialite(String specialite);
}
