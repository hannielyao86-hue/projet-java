package dao;

import model.Activiter;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface DAO pour la gestion des activités.
 */
public interface ActiviterDao {

    Activiter create(Activiter activite);
    Activiter findById(int id);
    List<Activiter> findAll();
    boolean update(Activiter activite);
    boolean delete(int id);

    /**
     * Recherche les activités par coach.
     */
    List<Activiter> findByCoach(int idCoach);

    /**
     * Recherche les activités par date.
     */
    List<Activiter> findByDate(LocalDate date);

    /**
     * Recherche les activités par salle.
     */
    List<Activiter> findBySalle(int idSalle);
}
