package model;

/**
 * Représente un membre de la salle de sport, Un membre a 0 ou 1 abonnement.
 */
public class Membre {
    private int idMembre;
    private String nomMembre;
    private String emailMembre;
    private String phone;
    private Integer idAbonnement;  // ← Peut être NULL

    // Constructeur par défaut
    public Membre() {
    }

    // Constructeur avec paramètres (sans abonnement)
    public Membre(int idMembre, String nomMembre, String emailMembre, String phone) {
        this.idMembre = idMembre;
        this.nomMembre = nomMembre;
        this.emailMembre = emailMembre;
        this.phone = phone;
    }

    // Constructeur avec paramètres (avec abonnement)
    public Membre(int idMembre, String nomMembre, String emailMembre, String phone, Integer idAbonnement) {
        this.idMembre = idMembre;
        this.nomMembre = nomMembre;
        this.emailMembre = emailMembre;
        this.phone = phone;
        this.idAbonnement = idAbonnement;
    }

    // Getters et Setters
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

    public Integer getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(Integer idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    @Override
    public String toString() {
        return "Membre{" +
                "idMembre=" + idMembre +
                ", nomMembre='" + nomMembre + '\'' +
                ", emailMembre='" + emailMembre + '\'' +
                ", phone='" + phone + '\'' +
                ", idAbonnement=" + idAbonnement +
                '}';
    }
}