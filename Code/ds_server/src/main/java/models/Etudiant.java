package models;

public class Etudiant {

    private int nbPV;
    private int nbXP;
    private String name;
    private int level;

    public Etudiant(String name){this.name = name; this.nbPV = 100; this.nbXP = 0; this.level = 1;}

    // TODO: Implement real question asking
    public boolean askQuestion(){
        // TODO: give to the player a list of question to choice.
        // TODO: send to the server the choice question.
        boolean adversaryResponse = false;
    }

    // TODO: Implement real question respond
    public void respondQuestion(){
        // TODO: give to the player a list of 4 response.
        // TODO: send to the server his response.
        boolean respondIsRight = false;
        if(respondIsRight){
            nbXP += 10;
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

    public int getNbXP() {
        return nbXP;
    }

    public void setNbXP(int nbXP) {
        this.nbXP = nbXP;
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

}
