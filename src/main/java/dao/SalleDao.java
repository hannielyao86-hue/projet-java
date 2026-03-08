package dao;

import model.Salle;
import java.util.List;

/**
 * Interface DAO pour la gestion des salles.
 */
public interface SalleDao {

    Salle create(Salle salle);
    Salle findById(int id);
    List<Salle> findAll();
    boolean update(Salle salle);
    boolean delete(int id);
}
