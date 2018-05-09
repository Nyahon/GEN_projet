package game;

import Protocol.Pfight;
import models.ConnectionDB;
import models.Player;
import models.Question;
import models.db_models.db_Professeur;
import server.JsonCreator;

import java.util.LinkedList;
import java.util.Random;

import static models.ConnectionDB.getQuestionByProfesseur;


public class FightStory implements Runnable {
    private Player player;

    private db_Professeur professeur;

    private boolean inFight;

    private int round;

    private Thread thread;


    public FightStory(Player player1) {

        thread = new Thread(this);
        thread.start();
    }


    public void run() {
        try {
            player.setFightMessageIn(professeur.getNom());
            player.setFightMessageIn(String.valueOf(professeur.getPv()));

            inFight = true;
            player.setInFight(true);
            round = 0;

            while (inFight) {
                player.setFightMessageIn(Pfight.ANSWER);


                LinkedList<Question> questions = getQuestionByProfesseur(1);
                //fixme: PRENDRE UNE ALEATOIREMENT
                Question question = questions.get(0);
                player.setFightMessageIn(question.getQuestion());

                // transmet les choix de réponses
                // fixme: TROUVER UN MOYEN DE METTRE DE l ALEATOIRE
                String repPayloadJson = JsonCreator.sendReponses(question);
                System.out.println(repPayloadJson);
                player.setFightMessageIn(repPayloadJson);

                String response = player.getFightMessageOut();
                String choixReponse = JsonCreator.parseReponseByLetter(repPayloadJson, response);
                if(question.getReponseOK().equals(choixReponse)){
                    //repondu juste
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
