package dao;

import model.Abonnement;
import java.util.List;

/**
 * Interface DAO pour la gestion des abonnements.
 */
public interface AbonnementDao {

    Abonnement create(Abonnement abonnement);
    Abonnement findById(int id);
    List<Abonnement> findAll();
    boolean update(Abonnement abonnement);
    boolean delete(int id);
    List<Abonnement> findByStatut(String statut);
}
