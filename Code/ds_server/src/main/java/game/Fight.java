package game;

import java.util.Random;

import models.ConnectionDB;
import models.Player;
import models.Question;
import server.JsonCreator;


public class Fight implements Runnable {
    private Player player1;
    private Player player2;

    private boolean inFight;

    private int round;

    private Thread thread;


    public Fight(Player player1, Player player2) {


        // put randomely player in first position to define random beginner
        Random rand = new Random();
        switch (rand.nextInt(2) + 1) {
            case 1:
                this.player1 = player1;
                this.player2 = player2;
                break;
            case 2:
                this.player2 = player1;
                this.player1 = player2;
                break;
        }

        thread = new Thread(this);
        thread.start();
    }


    public void run()  {
        try {
            //System.out.println(" Nom et PV Joueur 2 -------------------------");
            player1.setFightMessageIn(player2.getName());
            player1.setFightMessageIn(String.valueOf(player2.getNbPV()));
           // System.out.println(player1.getMessageIn());

           // System.out.println(" Nom et PV Joueur 1 -------------------------");
            player2.setFightMessageIn(player1.getName());
            player2.setFightMessageIn(String.valueOf(player1.getNbPV()));
           // System.out.println(player2.getMessageIn());

            inFight = true;
            player1.setInFight(true);
            player2.setInFight(true);
            round = 0;

                while (inFight) {

                    // todo: function to avoid duplicated code

                    if (round % 2 == 0) {


                        player1.setFightMessageIn("ASK");
                        //System.out.println("ASK OU ANSWER ? PLAYER 1 ---------------------------------");
                        //System.out.println(player1.getMessageIn());


                        player2.setFightMessageIn("ANSWER");
                       //System.out.println("ASK OU ANSWER ? PLAYER 2 ---------------------------------");
                       //System.out.println(player2.getMessageIn());

                        //System.out.println("QUESTION CHOISIE PLAYER 1 ---------------------------------");
                       // System.out.println(player1.getMessageOut());
                        Question question = ConnectionDB.getQuestionById(Integer.parseInt(player1.getFightMessageOut()));
                        player2.setFightMessageIn(question.getQuestion());
                        //System.out.println("QUESTION TRANSMISE PLAYER 2 ---------------------------------");
                       // System.out.println(player2.getMessageIn());

                        // transmet les choix de réponses
                        // fixme: TROUVER UN MOYEN DE METTRE DE l ALEATOIRE
                        String repPayloadJson = JsonCreator.sendReponses(question);
                       // System.out.println(repPayloadJson);
                        player2.setFightMessageIn(repPayloadJson);
                       // System.out.println("REPONSES TRANSMISE PLAYER 2 -----------------------------------");
                       // System.out.println(player2.getMessageIn());

                       // System.out.println("REPONSE CHOISIE PAR PLAYER 2 -----------------------------------");
                        //System.out.println(player2.getMessageOut());
                        String response = player2.getFightMessageOut();
                        String choixReponse = JsonCreator.parseReponseByLetter(repPayloadJson, response);
                        if(question.getReponseOK().equals(choixReponse)){
                            player2.setFightMessageIn("RIGHT");
                            //System.out.println("TRANSMISSION DU RIGHT PLAYER 2 ---------------------------------");
                            //System.out.println(player2.getMessageIn());
                            player1.loosePV(40);
                            // Envoi etat adversaire et joueur à player2 (RIGHT)
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            //System.out.println("ENVOI ETATS JOUEUR AU PLAYER 2 ------------------------------------");
                           // System.out.println(player2.getMessageIn());
                            // Envoi etat adversaire et joueur à player1 (ASK)
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            //System.out.println("ENVOI ETATS JOUEUR AU PLAYER 1 ------------------------------------");
                           // System.out.println(player1.getMessageIn());
                        }
                        else {
                            player2.setFightMessageIn("FALSE");

                            player2.loosePV(40);
                            // Envoi etat adversaire et joueur à player2 (FALSE)
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            // Envoi etat adversaire et joueur à player1 (ASK)
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player1));
                        }


                    } else {

                        player2.setFightMessageIn("ASK");

                        player1.setFightMessageIn("ANSWER");

                        Question question = ConnectionDB.getQuestionById(Integer.parseInt(player2.getFightMessageOut()));
                        player1.setFightMessageIn(question.getQuestion());

                        // transmet les choix de réponses
                        // fixme: TROUVER UN MOYEN DE METTRE DE l ALEATOIRE
                        String repPayloadJson = JsonCreator.sendReponses(question);
                        //System.out.println(repPayloadJson);
                        player1.setFightMessageIn(repPayloadJson);

                        String response = player1.getFightMessageOut();
                        String choixReponse = JsonCreator.parseReponseByLetter(repPayloadJson, response);
                        if(question.getReponseOK().equals(choixReponse)){
                            player1.setFightMessageIn("RIGHT");

                            player2.loosePV(40);
                            // Envoi etat adversaire et joueur à player1 (RIGHT)
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            // Envoi etat adversaire et joueur à player2 (ASK)
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player2));
                        }
                        else {
                            player1.setFightMessageIn("FALSE");

                            player1.loosePV(40);
                            // Envoi etat adversaire et joueur à player1 (RIGHT)
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            // Envoi etat adversaire et joueur à player2 (ASK)
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player2));
                        }

                    }



                    round++;

                    if(player1.getNbPV() <= 0 || player2.getNbPV() <= 0){
                        inFight = false;
                        player1.setInFight(false);
                        player2.setInFight(false);
                    }

                } // END OF FIGHT

                    if (player1.getNbPV() <= 0) {
                        player1.setFightMessageIn("END");
                        player1.setFightMessageIn("LOST");

                        player2.setFightMessageIn("END");
                        player2.setFightMessageIn("WON");
                    }
                    if (player2.getNbPV() <= 0) {
                        player1.setFightMessageIn("END");
                        player1.setFightMessageIn("WON");

                        player2.setFightMessageIn("END");
                        player2.setFightMessageIn("LOST");
                    }


        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        player1.setInFight(false);
        player2.setInFight(false);
    }
}
