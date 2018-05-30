package models.db_models;

import models.ConnectionDB;
import models.Question;

import java.util.LinkedList;

public class db_Professeur {
    private int Id;
    private String Nom;
    private int Pv;
    private int Niveau;
    private LinkedList<Question> questions = new LinkedList<>();



    public db_Professeur(int id, String nom, int pv, int niveau) {
        Id = id;
        Nom = nom;
        Pv = pv;
        Niveau = niveau;
        questions = ConnectionDB.getQuestionByProfesseur(id);
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public int getPv() {
        return Pv;
    }

    public void setPv(int pv) {
        Pv = pv;
    }

    public int getNiveau() {
        return Niveau;
    }

    public void setNiveau(int niveau) {
        Niveau = niveau;
    }

    public void loosePv(int pv) { this.Pv -= pv; }

    public LinkedList<Question> getQuestions() {
        return questions;
    }



    @Override
    public String toString() {
        return "db_Professeur{" +
                "Id=" + Id +
                ", Nom='" + Nom + '\'' +
                ", Pv=" + Pv +
                ", Niveau=" + Niveau +
                '}';
    }

}