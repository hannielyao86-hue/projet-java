package model;

import java.time.LocalDate;   //pour le type date

/**
 * Représente l'inscription d'un membre à une activité.
 */
public class Inscription {
    private int idInscription;
    private LocalDate dateInscription;
    private String statut;
    private Membre membre;
    private Activiter activite;

    // Constructeur par défaut
    public Inscription() {
    }

    // Constructeur avec tous ses paramètres
    public Inscription(int idInscription, LocalDate dateInscription, String statut,
                       Membre membre, Activiter activite) {
        this.idInscription = idInscription;
        this.dateInscription = dateInscription;
        this.statut = statut;
        this.membre = membre;
        this.activite = activite;
    }

    // utilisations Getters et Setters
    public int getIdInscription() {
        return idInscription;
    }

    public void setIdInscription(int idInscription) {
        this.idInscription = idInscription;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Activiter getActivite() {
        return activite;
    }

    public void setActivite(Activiter activite) {
        this.activite = activite;
    }

    //permet de  comprendre ce qu'il y a dans l'objet
    @Override
    public String toString() {
        String nomMembre = (membre != null) ? membre.getNomMembre() : "N/A";
        String nomActivite = (activite != null) ? activite.getNomActivite() : "N/A";
        return nomMembre + " - " + nomActivite;
    }
}
