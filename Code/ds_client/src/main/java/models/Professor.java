package models;

import java.util.LinkedList;

public class Professor extends Adversary{


    public Professor() {}

    public Professor(int id, String name, int pv, int niveau,  String image){
        super(id, name, pv, niveau, image);
    }

    @Override
    public boolean askQuestion(){
        boolean adversaryResponse = false;

        return adversaryResponse;
    }

    @Override
   public void respondQuestion(){
        boolean respondIsRight = false;
        if(respondIsRight){
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
