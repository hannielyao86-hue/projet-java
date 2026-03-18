package model;

import java.time.LocalDate;

/**
 * Représente l'abonnement d'un membre.
 */
public class Abonnement {
    private int idAbonnement;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statutAbonnement;
    private String nomAbonnement;

    // Constructeur par défaut
    public Abonnement() {
    }

    // Constructeur avec tous ses paramètres
    public Abonnement(int idAbonnement, LocalDate dateDebut, LocalDate dateFin, String statutAbonnement, String nomAbonnement) {
        this.idAbonnement = idAbonnement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statutAbonnement = statutAbonnement;
        this.nomAbonnement = nomAbonnement;
    }

    //utilisations des Getters et Setters
    public int getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatutAbonnement() {
        return statutAbonnement;
    }

    public void setStatutAbonnement(String statutAbonnement) {
        this.statutAbonnement = statutAbonnement;
    }

    public String getNomAbonnement() {
        return nomAbonnement;
    }

    public void setNomAbonnement(String nomAbonnement) {
        this.nomAbonnement = nomAbonnement;
    }

    //permet de comprendre ce qu'il y a dans l'objet
    @Override
    public String toString() {
        return  nomAbonnement;
    }
}