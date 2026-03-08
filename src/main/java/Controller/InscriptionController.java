package Controller;

import dao.InscriptionDao;
import dao.InscriptionDaoImpl;
import dao.MembreDao;
import dao.MembreDaoImpl;
import dao.ActiviterDao;
import dao.ActiviterDaoImpl;
import exception.BusinessException;
import model.Activiter;
import model.Inscription;
import model.Membre;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller pour la gestion des inscriptions.
 */
public class InscriptionController {

    private final InscriptionDao inscriptionDao;
    private final MembreDao membreDao;
    private final ActiviterDao activiteDao;

    public InscriptionController() {
        this.inscriptionDao = new InscriptionDaoImpl();
        this.membreDao = new MembreDaoImpl();
        this.activiteDao = new ActiviterDaoImpl();
    }

    /**
     * Inscrit un membre à une activité.
     */
    public Inscription inscrireMembre(int idMembre, int idActivite, String statut) {
        // Vérification membre
        Membre membre = membreDao.findById(idMembre);
        if (membre == null) {
            throw new BusinessException("Membre non trouvé");
        }

        // Vérification activité
        Activiter activite = activiteDao.findById(idActivite);
        if (activite == null) {
            throw new BusinessException("Activité non trouvée");
        }

        // Vérification que l'activité n'est pas passée
        if (activite.getDateActivite().isBefore(LocalDate.now())) {
            throw new BusinessException("Impossible de s'inscrire à une activité passée");
        }

        Inscription inscription = new Inscription();
        inscription.setMembre(membre);
        inscription.setActivite(activite);
        inscription.setStatut(statut != null ? statut : "en_attente");
        inscription.setDateInscription(LocalDate.now());

        return inscriptionDao.create(inscription);
    }

    /**
     * Récupère une inscription par son ID.
     */
    public Inscription getInscription(int id) {
        Inscription inscription = inscriptionDao.findById(id);

        if (inscription == null) {
            throw new BusinessException("Inscription non trouvée");
        }

        return inscription;
    }

    /**
     * Récupère toutes les inscriptions.
     */
    public List<Inscription> getToutesInscriptions() {
        return inscriptionDao.findAll();
    }

    /**
     * Récupère les inscriptions d'un membre.
     */
    public List<Inscription> getInscriptionsParMembre(int idMembre) {
        return inscriptionDao.findByMembre(idMembre);
    }

    /**
     * Récupère les inscriptions à une activité.
     */
    public List<Inscription> getInscriptionsParActivite(int idActivite) {
        return inscriptionDao.findByActivite(idActivite);
    }

    /**
     * Change le statut d'une inscription.
     */
    public boolean changerStatut(int id, String nouveauStatut) {
        Inscription inscription = getInscription(id);

        if (!List.of("confirmee", "en_attente", "annulee").contains(nouveauStatut)) {
            throw new BusinessException("Statut invalide : " + nouveauStatut);
        }

        inscription.setStatut(nouveauStatut);
        return inscriptionDao.update(inscription);
    }

    /**
     * Annule une inscription.
     */
    public boolean annulerInscription(int id) {
        return changerStatut(id, "annulee");
    }

    /**
     * Confirme une inscription.
     */
    public boolean confirmerInscription(int id) {
        return changerStatut(id, "confirmee");
    }

    /**
     * Supprime une inscription.
     */
    public boolean supprimerInscription(int id) {
        return inscriptionDao.delete(id);
    }
}
