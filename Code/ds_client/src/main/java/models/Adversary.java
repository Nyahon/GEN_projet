package models;

import java.util.LinkedList;

public abstract class Adversary {

    protected int nbPV;
    protected String name;
    protected int level;
    protected int id;
    protected LinkedList<Question> questions;

    private String image;


    public Adversary(){}


    public Adversary(int id, String name, int pv, int niveau, String image){
        this.name = name;
        this.nbPV = pv;
        this.level = niveau;
        this.id = id;
        this.questions = new LinkedList<>();
        this.image = image;
    }


    public abstract boolean askQuestion();

    public abstract void respondQuestion();


    public boolean IsLive(){
        return nbPV>0;
    }

    // ==========================================================
    // GETTERS SETTERS
    // ==========================================================
    public int getNbPV() {
        return nbPV;
    }

    public void setNbPV(int nbPV) {
        this.nbPV = nbPV;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setQuestions(LinkedList<Question> questions){
        this.questions = questions;
    }

    public void addQuestions(Question question){
        this.questions.add(question);
    }

    public int getId() {
        return id;
    }

    public LinkedList<Question> getQuestions(){
        return this.questions;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void afficheQuestions(){
        for(Question q : questions){
            System.out.println("id : " + q.getId() + " : " + q.getQuestion());
        }
    }

    @Override
    public abstract String toString();


}
