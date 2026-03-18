package model;

/**
  Représente un coach de la salle de sport.
 */
public class Coach {
    private int idCoach;
    private String nomCoach;
    private String emailCoach;
    private String specialite;

    // le Constructeur par défaut
    public Coach() {
    }

    // Constructeur avec tous ses paramètres
    public Coach(int idCoach, String nomCoach, String emailCoach, String specialite) {
        this.idCoach = idCoach;
        this.nomCoach = nomCoach;
        this.emailCoach = emailCoach;
        this.specialite = specialite;
    }

    // utilisation des methodes Getters et Setters
    public int getIdCoach() {
        return idCoach;
    }

    public void setIdCoach(int idCoach) {
        this.idCoach = idCoach;
    }

    public String getNomCoach() {
        return nomCoach;
    }

    public void setNomCoach(String nomCoach) {
        this.nomCoach = nomCoach;
    }

    public String getEmailCoach() {
        return emailCoach;
    }

    public void setEmailCoach(String emailCoach) {
        this.emailCoach = emailCoach;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    //permet de  comprendre ce qu'il y a dans l'objet
    @Override
    public String toString() {
        return nomCoach;
    }
}