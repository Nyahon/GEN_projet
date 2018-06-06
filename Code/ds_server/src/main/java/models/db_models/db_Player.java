package models.db_models;

public class db_Player {

    private String nom;
    private int id;
    private int annee;
    private int pv;
    private int xp;
    private int niveau;
    private String image;


    public db_Player(int id, String nom, int annee, int pv, int niveau, int xp, String image) {
        this.nom = nom;
        this.id = id;
        this.annee = annee;
        this.pv = pv;
        this.xp = xp;
        this.niveau = niveau;
        this.image = image;
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


    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "db_Player{" +
                "nom='" + nom + '\'' +
                ", id=" + id +
                ", annee=" + annee +
                ", pv=" + pv +
                ", xp=" + xp +
                ", niveau=" + niveau +
                '}';
    }
}
