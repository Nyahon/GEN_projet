package game;

import java.util.Random;

import Protocol.Pfight;
import models.ConnectionDB;
import models.ItemType;
import models.Player;
import models.Question;
import models.db_models.db_Professeur;
import server.JsonCreator;


public class FightStory implements Runnable {
    private Player player;
    private db_Professeur professeur;
    private boolean inFight;

    private int round;

    private Thread thread;


    public FightStory(Player player, db_Professeur professeur) {

        this.player = player;
        this.professeur = professeur;

        thread = new Thread(this);
        thread.start();
    }


    public void run()  {
        try {
            player.setFightMessageIn(professeur.getNom());
            player.setFightMessageIn(String.valueOf(professeur.getPv()));

            inFight = true;
            player.setInFight(true);
            round = 0;

            while (inFight) {

                // todo: function to avoid duplicated code

                player.setFightMessageIn("ANSWER");

                Question question = ConnectionDB.getQuestionById(1);
                player.setFightMessageIn(question.getQuestion());

                // transmet les choix de réponses
                // fixme: TROUVER UN MOYEN DE METTRE DE l ALEATOIRE
                String repPayloadJson = JsonCreator.sendReponses(question);
                player.setFightMessageIn(repPayloadJson);

                // désire de choisir un item.
                String rep = player.getFightMessageOut();
                if(rep.equals(Pfight.USE_ITEM)){

                    // envoie des items disponnible
                    String itemsPayloadJson = JsonCreator.sendItems(player.getItems());
                    player.setFightMessageIn(itemsPayloadJson);

                    // récupérer le choix d'item à utiliser et utiliser l'item
                    switch(player.getFightMessageOut()){
                        case Pfight.ITEM_BIERE:
                            if(player.getNbItem(ItemType.Biere) > 0){
                                player.UseItem(player.getItem(ItemType.Biere), question);
                            }
                            break;
                        case Pfight.ITEM_LIVRE:
                            if(player.getNbItem(ItemType.Livre) > 0) {
                                player.UseItem(player.getItem(ItemType.Livre), question);
                            }
                            break;
                        case Pfight.ITEM_ANTISECHE:
                            if(player.getNbItem(ItemType.AntiSeche) > 0) {
                                player.UseItem(player.getItem(ItemType.AntiSeche), question);
                            }
                            break;
                    }

                    // r'envoi du choix de réponse possible
                    repPayloadJson = JsonCreator.sendReponses(question);
                    System.out.println(repPayloadJson);
                    player.setFightMessageIn(repPayloadJson);
                }

                String response = player.getFightMessageOut();
                String choixReponse = JsonCreator.parseReponseByLetter(repPayloadJson, response);
                if(question.getReponseOK().equals(choixReponse)){
                    player.setFightMessageIn("RIGHT");
                    professeur.loosePv(40);
                    // Envoi etat adversaire et joueur à player1
                    player.setFightMessageIn(JsonCreator.SendProfesseur(professeur));
                    player.setFightMessageIn(JsonCreator.SendPlayer(player));
                }
                else {
                    player.setFightMessageIn("FALSE");
                    player.loosePV(40);
                    // Envoi etat adversaire et joueur à player1
                    player.setFightMessageIn(JsonCreator.SendProfesseur(professeur));
                    player.setFightMessageIn(JsonCreator.SendPlayer(player));
                }


                if(player.getNbPV() <= 0 || professeur.getPv() <= 0){
                    inFight = false;
                    player.setInFight(false);
                }

            } // END OF FIGHT

            if (player.getNbPV() <= 0) {
                player.setFightMessageIn("END");
                player.setFightMessageIn("LOST");
            }
            if (professeur.getPv() <= 0) {
                player.setFightMessageIn("END");
                player.setFightMessageIn("WON");

            }


        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        player.setInFight(false);
    }
}