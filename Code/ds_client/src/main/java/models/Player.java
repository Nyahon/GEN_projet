package models;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Player extends Adversary{

    private int nbXP;
    private int annee;

    private Socket socket = null;

    public Player() {}

    public Player(int id, String name, int annee, int pv, int niveau, int xp, String image) {
        super(id, name, pv, niveau, image);
        this.nbXP = xp;
        this.annee = annee;

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
            nbXP += 10;
        }
        else{
            nbPV -= 20;
        }
    }


    // ==========================================================
    // GETTERS SETTERS
    // ==========================================================



    public int getNbXP() {
        return nbXP;
    }

    public void setNbXP(int nbXP) {
        this.nbXP = nbXP;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nbPV=" + nbPV +
                ", nbXP=" + nbXP +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", id=" + id +
                ", annee=" + annee +
                '}';
    }

}
