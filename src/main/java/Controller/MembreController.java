package Controller;

import dao.MembreDao;
import dao.MembreDaoImpl;
import exception.BusinessException;
import model.Membre;

import java.util.List;

/**
 * Controller pour la gestion des membres.
 * Fait le lien entre la vue et le DAO.
 */
public class MembreController {

    private final MembreDao membreDao;

    public MembreController() {
        this.membreDao = new MembreDaoImpl();
    }

    /**
     * Crée un nouveau membre.
     * @param nomMembre Nom du membre
     * @param emailMembre Email du membre
     * @param phone Téléphone
     * @param idAbonnement ID de l'abonnement (peut être null)
     * @return Le membre créé
     * @throws BusinessException si validation échoue
     */
    public Membre creerMembre(String nomMembre, String emailMembre, String phone, Integer idAbonnement) {
        // Validation supplémentaire au niveau controller
        if (nomMembre == null || nomMembre.trim().isEmpty()) {
            throw new BusinessException("Le nom ne peut pas être vide");
        }

        if (emailMembre == null || !emailMembre.contains("@")) {
            throw new BusinessException("L'email doit être valide");
        }

        Membre membre = new Membre();
        membre.setNomMembre(nomMembre.trim());
        membre.setEmailMembre(emailMembre.trim().toLowerCase());
        membre.setPhone(phone != null ? phone.trim() : null);
        membre.setIdAbonnement(idAbonnement);

        return membreDao.create(membre);
    }

    /**Récupère un membre par son ID.
     */
    public Membre getMembre(int id) {
        if (id <= 0) {
            throw new BusinessException("ID invalide");
        }

        Membre membre = membreDao.findById(id);

        if (membre == null) {
            throw new BusinessException("Membre non trouvé avec l'ID : " + id);
        }

        return membre;
    }

    /**
     * Récupère tous les membres.
     */
    public List<Membre> getTousMembres() {
        return membreDao.findAll();
    }

    /**
     * Met à jour un membre.
     */
    public boolean modifierMembre(int id, String nomMembre, String emailMembre, String phone, Integer idAbonnement) {
        Membre membre = getMembre(id); // Vérifie que le membre existe

        membre.setNomMembre(nomMembre.trim());
        membre.setEmailMembre(emailMembre.trim().toLowerCase());
        membre.setPhone(phone != null ? phone.trim() : null);
        membre.setIdAbonnement(idAbonnement);

        return membreDao.update(membre);
    }

    /**
     * Supprime un membre.
     */
    public boolean supprimerMembre(int id) {
        if (id <= 0) {
            throw new BusinessException("ID invalide");
        }

        return membreDao.delete(id);
    }

    /**
     * Recherche des membres par nom.
     */
    public List<Membre> rechercherParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return getTousMembres();
        }

        return membreDao.findByNom(nom.trim());
    }
}