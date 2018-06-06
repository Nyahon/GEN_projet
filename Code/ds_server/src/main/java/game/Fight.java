package game;

import java.util.Random;
import java.util.prefs.PreferencesFactory;

import Protocol.Pfight;
import models.ConnectionDB;
import models.ItemType;
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
            //player1.setFightMessageIn(player2.getName());
            //player1.setFightMessageIn(String.valueOf(player2.getNbPV()));
            player1.setFightMessageIn(JsonCreator.SendPlayer(player2));


            //player2.setFightMessageIn(player1.getName());
            //player2.setFightMessageIn(String.valueOf(player1.getNbPV()));
            player2.setFightMessageIn(JsonCreator.SendPlayer(player1));


            inFight = true;
            player1.setInFight(true);
            player2.setInFight(true);
            round = 0;

                while (inFight) {

                    // todo: function to avoid duplicated code

                    if (round % 2 == 0) {


                        player1.setFightMessageIn(Pfight.ASK);

                        player2.setFightMessageIn(Pfight.ANSWER);

                        Question question = ConnectionDB.getQuestionById(Integer.parseInt(player1.getFightMessageOut()));
                        player2.setFightMessageIn(question.getQuestion());

                        // transmet les choix de réponses
                        // fixme: TROUVER UN MOYEN DE METTRE DE l ALEATOIRE
                        String repPayloadJson = JsonCreator.sendReponses(question);
                        player2.setFightMessageIn(repPayloadJson);

                        // désire de choisir un item.
                        String rep = player2.getFightMessageOut();
                        if(rep.equals(Pfight.USE_ITEM)){

                            // envoie des items disponnible
                            String itemsPayloadJson = JsonCreator.sendItems(player2.getItems());
                            player2.setFightMessageIn(itemsPayloadJson);

                            // récupérer le choix d'item à utiliser et utiliser l'item
                            switch(player2.getFightMessageOut()){
                                case Pfight.ITEM_BIERE:
                                    if(player2.getNbItem(ItemType.Biere) > 0){
                                        player2.UseItem(player2.getItem(ItemType.Biere), question);
                                    }
                                    break;
                                case Pfight.ITEM_LIVRE:
                                    if(player2.getNbItem(ItemType.Livre) > 0) {
                                        player2.UseItem(player2.getItem(ItemType.Livre), question);
                                    }
                                    break;
                                case Pfight.ITEM_ANTISECHE:
                                    if(player2.getNbItem(ItemType.AntiSeche) > 0) {
                                        player2.UseItem(player2.getItem(ItemType.AntiSeche), question);
                                    }
                                    break;
                            }

                            // r'envoi du choix de réponse possible
                            repPayloadJson = JsonCreator.sendReponses(question);
                            System.out.println(repPayloadJson);
                            player2.setFightMessageIn(repPayloadJson);
                        }

                        String response = player2.getFightMessageOut();
                        String choixReponse = JsonCreator.parseReponseByLetter(repPayloadJson, response);
                        if(question.getReponseOK().equals(choixReponse)){
                            player2.setFightMessageIn(Pfight.RIGHT);
                            player1.setFightMessageIn(Pfight.RIGHT);
                            player1.loosePV(40);
                            // Envoi etat adversaire et joueur à player2 (RIGHT)
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            // Envoi etat adversaire et joueur à player1 (ASK)
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player1));
                        }
                        else {
                            player2.setFightMessageIn(Pfight.FALSE);
                            player1.setFightMessageIn(Pfight.FALSE);

                            player2.loosePV(40);
                            // Envoi etat adversaire et joueur à player2 (FALSE)
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            // Envoi etat adversaire et joueur à player1 (ASK)
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player1));
                        }


                    } else {

                        player2.setFightMessageIn(Pfight.ASK);

                        player1.setFightMessageIn(Pfight.ANSWER);

                        Question question = ConnectionDB.getQuestionById(Integer.parseInt(player2.getFightMessageOut()));
                        player1.setFightMessageIn(question.getQuestion());

                        // transmet les choix de réponses
                        // fixme: TROUVER UN MOYEN DE METTRE DE l ALEATOIRE
                        String repPayloadJson = JsonCreator.sendReponses(question);
                        //System.out.println(repPayloadJson);
                        player1.setFightMessageIn(repPayloadJson);

                        // désire de choisir un item.
                        String rep = player1.getFightMessageOut();
                        if(rep.equals(Pfight.USE_ITEM)){

                            // envoie des items disponnible
                            String itemsPayloadJson = JsonCreator.sendItems(player1.getItems());
                            player1.setFightMessageIn(itemsPayloadJson);

                            // récupérer le choix d'item à utiliser et utiliser l'item
                            switch(player1.getFightMessageOut()){
                                case Pfight.ITEM_BIERE:
                                    if(player1.getNbItem(ItemType.Biere) > 0){
                                        player1.UseItem(player1.getItem(ItemType.Biere), question);
                                    }
                                    break;
                                case Pfight.ITEM_LIVRE:
                                    if(player1.getNbItem(ItemType.Livre) > 0) {
                                        player1.UseItem(player1.getItem(ItemType.Livre), question);
                                    }
                                    break;
                                case Pfight.ITEM_ANTISECHE:
                                    if(player1.getNbItem(ItemType.AntiSeche) > 0) {
                                        player1.UseItem(player1.getItem(ItemType.AntiSeche), question);
                                    }
                                    break;
                            }

                            // r'envoi du choix de réponse possible
                            repPayloadJson = JsonCreator.sendReponses(question);
                            System.out.println(repPayloadJson);
                            player1.setFightMessageIn(repPayloadJson);
                        }


                        String response = player1.getFightMessageOut();
                        String choixReponse = JsonCreator.parseReponseByLetter(repPayloadJson, response);
                        if(question.getReponseOK().equals(choixReponse)){
                            player1.setFightMessageIn(Pfight.RIGHT);
                            player2.setFightMessageIn(Pfight.RIGHT);

                            player2.loosePV(40);
                            // Envoi etat adversaire et joueur à player1 (RIGHT)
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player2));
                            player1.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            // Envoi etat adversaire et joueur à player2 (ASK)
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player1));
                            player2.setFightMessageIn(JsonCreator.SendPlayer(player2));
                        }
                        else {
                            player1.setFightMessageIn(Pfight.FALSE);
                            player2.setFightMessageIn(Pfight.FALSE);

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
                        player1.setFightMessageIn(Pfight.END);
                        player1.setFightMessageIn(Pfight.LOST);

                        player2.setFightMessageIn(Pfight.END);
                        player2.setFightMessageIn(Pfight.LOST);
                    }
                    if (player2.getNbPV() <= 0) {
                        player1.setFightMessageIn(Pfight.END);
                        player1.setFightMessageIn(Pfight.WIN);

                        player2.setFightMessageIn(Pfight.END);
                        player2.setFightMessageIn(Pfight.LOST);
                    }


        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        player1.setInFight(false);
        player2.setInFight(false);
    }
}
