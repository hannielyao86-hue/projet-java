package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Représente une activité sportive.
 */
public class Activiter {
    private int idActivite;
    private String nomActivite;
    private LocalDate dateActivite;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Coach coach;
    private Salle salle;

    // Constructeur par défaut
    public Activiter() {
    }

    // Constructeur avec tous ses paramètres
    public Activiter(int idActivite, String nomActivite, LocalDate dateActivite,
                    LocalTime heureDebut, LocalTime heureFin, Coach coach, Salle salle) {
        this.idActivite = idActivite;
        this.nomActivite = nomActivite;
        this.dateActivite = dateActivite;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.coach = coach;
        this.salle = salle;
    }

    //utilisations des Getters et Setters
    public int getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(int idActivite) {
        this.idActivite = idActivite;
    }

    public String getNomActivite() {
        return nomActivite;
    }

    public void setNomActivite(String nomActivite) {
        this.nomActivite = nomActivite;
    }

    public LocalDate getDateActivite() {
        return dateActivite;
    }

    public void setDateActivite(LocalDate dateActivite) {
        this.dateActivite = dateActivite;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    //permet de  comprendre ce qu'il y a dans l'objet
    @Override
    public String toString() {
        return "Activite{" +
                "idActivite=" + idActivite +
                ", nomActivite='" + nomActivite + '\'' +
                ", dateActivite=" + dateActivite +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", coach=" + (coach != null ? coach.getNomCoach() : "null") +
                ", salle=" + (salle != null ? salle.getNomSalle() : "null") +
                '}';
    }
}