package models.db_models;

public class db_Player {

    private String nom;
    private int id;
    private int annee;


    public db_Player(int id, String nom, int annee) {
        this.nom = nom;
        this.id = id;
        this.annee = annee;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    @Override
    public String toString() {
        return "db_Player{" +
                "nom='" + nom + '\'' +
                ", id=" + id +
                ", annee=" + annee +
                '}';
    }
}
