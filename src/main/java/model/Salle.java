package model;

/**
 * Représente une salle de sport.
 */
public class Salle {
    private int idSalle;
    private String nomSalle;
    private int capacite;

    // le Constructeur par défaut
    public Salle() {
    }

    // Constructeur avec tous ses paramètres
    public Salle(int idSalle, String nomSalle, int capacite) {
        this.idSalle = idSalle;
        this.nomSalle = nomSalle;
        this.capacite = capacite;
    }

    // utilisation des Getters et Setters
    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public int getCapacite() {

        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    //permet de  comprendre ce qu'il y a dans l'objet
    @Override
    public String toString() {
        return "Salle{" +
                "idSalle=" + idSalle +
                ", nomSalle='" + nomSalle + '\'' +
                ", capacite=" + capacite +
                '}';
    }
}