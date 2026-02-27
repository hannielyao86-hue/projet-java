package model;

/**
 * Représente un membre de la salle de sport.
 */
public class Membre {
    private int idMembre;
    private String nomMembre;
    private String emailMembre;
    private String phone;
    private Abonnement abonnement;

    // Constructeur par défaut
    public Membre() {
    }

    // Constructeur avec tous ses paramètres
    public Membre(int idMembre, String nomMembre, String emailMembre, String phone) {
        this.idMembre = idMembre;
        this.nomMembre = nomMembre;
        this.emailMembre = emailMembre;
        this.phone = phone;
    }

    // utilisations Getters et Setters
    public int getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public String getNomMembre() {
        return nomMembre;
    }

    public void setNomMembre(String nomMembre) {
        this.nomMembre = nomMembre;
    }

    public String getEmailMembre() {
        return emailMembre;
    }

    public void setEmailMembre(String emailMembre) {
        this.emailMembre = emailMembre;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Abonnement getAbonnement() {
        return abonnement;
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
    }

    //permet de  comprendre ce qu'il y a dans l'objet
    @Override
    public String toString() {
        return "Membre{" +
                "idMembre=" + idMembre +
                ", nomMembre='" + nomMembre + '\'' +
                ", emailMembre='" + emailMembre + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
