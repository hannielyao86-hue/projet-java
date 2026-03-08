package dao;

import model.Inscription;
import java.util.List;

/**
 * Interface DAO pour la gestion des inscriptions.
 */
public interface InscriptionDao {

    Inscription create(Inscription inscription);
    Inscription findById(int id);
    List<Inscription> findAll();
    boolean update(Inscription inscription);
    boolean delete(int id);

    /**
     * Recherche les inscriptions d'un membre.
     */
    List<Inscription> findByMembre(int idMembre);

    /**
     * Recherche les inscriptions à une activité.
     */
    List<Inscription> findByActivite(int idActivite);

    /**
     * Recherche les inscriptions par statut.
     */
    List<Inscription> findByStatut(String statut);
}
