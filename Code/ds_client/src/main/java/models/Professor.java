package models;

import java.util.LinkedList;

public class Professor {

    private int nbPV;
    private String name;
    private int level;
    private int id;
    private String image;
    private LinkedList<Question> questions;

    public Professor() {}

    public Professor(int id, String name, int annee, int pv, int niveau, int xp){
        this.name = name; this.nbPV = pv; this.level = niveau; this.id = id;
        questions = new LinkedList<>();}


    // TODO: Implement real question asking
    public boolean askQuestion(){
        // TODO: give to the player a list of question to choice.
        // TODO: send to the server the choice question, then the question will be send to the other player.
        // TODO: Then we receive from the server is the response of the adversery is true or false.
        boolean adversaryResponse = false;

        return adversaryResponse;
    }

    // TODO: Implement real question respond
    public void respondQuestion(){
        // TODO: give to the player a list of 4 responses to ask the question of the adversary.
        // TODO: send to the server his response.
        // TODO: Then the server respond if the response is true or false;
        boolean respondIsRight = false;
        if(respondIsRight){
            //TODO: test if we are in server vs server or vs player
            //TODO: if we are versus server we win the question asking.
        }
        else{
            nbPV -= 20;
        }
    }

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

    public void afficheQuestions(){
        for(Question q : questions){
            System.out.println("id : " + q.getId() + " : " + q.getQuestion());
        }
    }

    @Override
    public String toString() {
        return "Professor{" +
                "nbPV=" + nbPV +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", id=" + id +
                '}';
    }

}
