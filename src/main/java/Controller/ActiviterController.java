
package Controller;

import dao.ActiviterDao;
import dao.ActiviterDaoImpl;
import dao.CoachDao;
import dao.CoachDaoImpl;
import dao.SalleDao;
import dao.SalleDaoImpl;
import exception.BusinessException;
import model.Activiter;
import model.Coach;
import model.Salle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Controller pour la gestion des activités.
 */
public class ActiviterController {

    private final ActiviterDao activiteDao;
    private final CoachDao coachDao;
    private final SalleDao salleDao;

    public ActiviterController() {
        // real implementation instead of anonymous stub
        this.activiteDao = new ActiviterDaoImpl();
        this.coachDao = new CoachDaoImpl();
        this.salleDao = new SalleDaoImpl();
    }

    /**
     * Crée une nouvelle activité.
     */
    public Activiter creerActivite(String nomActivite, LocalDate dateActivite, LocalTime heureDebut, LocalTime heureFin, int idCoach, Integer idSalle) {

        // Validation de l'activiter
        if (nomActivite == null || nomActivite.trim().isEmpty()) {
            throw new BusinessException("Le nom de l'activité est obligatoire");
        }

        if (dateActivite.isBefore(LocalDate.now())) {
            throw new BusinessException("La date ne peut pas être dans le passé");
        }

        // Récupération du coach
        Coach coach = coachDao.findById(idCoach);
        if (coach == null) {
            throw new BusinessException("Coach non trouvé avec l'ID : " + idCoach);
        }

        // Récupération de la salle (optionnel)
        Salle salle = null;
        if (idSalle != null && idSalle > 0) {
            salle = salleDao.findById(idSalle);
            if (salle == null) {
                throw new BusinessException("Salle non trouvée avec l'ID : " + idSalle);
            }
        }

        Activiter activite = new Activiter();
        activite.setNomActivite(nomActivite.trim());
        activite.setDateActivite(dateActivite);
        activite.setHeureDebut(heureDebut);
        activite.setHeureFin(heureFin);
        activite.setCoach(coach);
        activite.setSalle(salle);

        return activiteDao.create(activite);
    }

    /**
     * Récupère une activité par son ID.
     */
    public Activiter getActivite(int id) {
        if (id <= 0) {
            throw new BusinessException("ID invalide");
        }

        Activiter activite = activiteDao.findById(id);

        if (activite == null) {
            throw new BusinessException("Activité non trouvée avec l'ID : " + id);
        }

        return activite;
    }

    /**
     * Récupère toutes les activités.
     */
    public List<Activiter> getToutesActivites() {
        return activiteDao.findAll();
    }

    /**
     * Récupère les activités d'un coach.
     */
    public List<Activiter> getActivitesParCoach(int idCoach) {
        return activiteDao.findByCoach(idCoach);
    }

    /**
     * Récupère les activités d'une date donnée.
     */
    public List<Activiter> getActivitesParDate(LocalDate date) {
        return activiteDao.findByDate(date);
    }

    /**
     * Met à jour une activité.
     */
    public boolean modifierActivite(int id, String nomActivite, LocalDate dateActivite, LocalTime heureDebut, LocalTime heureFin, int idCoach, Integer idSalle) {

        Activiter activite = getActivite(id);

        Coach coach = coachDao.findById(idCoach);
        if (coach == null) {
            throw new BusinessException("Coach non trouvé");
        }

        Salle salle = null;
        if (idSalle != null && idSalle > 0) {
            salle = salleDao.findById(idSalle);
        }

        activite.setNomActivite(nomActivite.trim());
        activite.setDateActivite(dateActivite);
        activite.setHeureDebut(heureDebut);
        activite.setHeureFin(heureFin);
        activite.setCoach(coach);
        activite.setSalle(salle);
        return activiteDao.update(activite);
    }

    /**
     * Supprime une activité.
     */
    public boolean supprimerActivite(int id) {
        return activiteDao.delete(id);
    }
}
