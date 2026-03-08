package dao;

import model.Membre;
import java.util.List;

/**
 * Interface DAO pour la gestion des membres et Définit les opérations CRUD.
 */
public interface MembreDao {

    /**
     * Crée un nouveau membre dans la base de données.
     * @param membre Le membre à créer et @return Le membre créé avec son ID généré
     */
    Membre create(Membre membre);

    /**
     * Récupère un membre par son ID.
     * @param id L'identifiant du membre et @return Le membre trouvé, ou null si inexistant
     */
    Membre findById(int id);

    /**
     * Récupère tous les membres et @return Liste de tous les membres
     */
    List<Membre> findAll();

    /**
     * Met à jour un membre existant.
     * @param membre Le membre avec les nouvelles données et @return true si la mise à jour a réussi
     */
    boolean update(Membre membre);

    /**
     * Supprime un membre par son ID.
     * @param id L'identifiant du membre à supprimer et @return true si la suppression a réussi
     */
    boolean delete(int id);

    /**
     * Recherche des membres par nom (recherche partielle).
     * @param nom Le nom à rechercher et @return Liste des membres correspondants
     */
    List<Membre> findByNom(String nom);
}
