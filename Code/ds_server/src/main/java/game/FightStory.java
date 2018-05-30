package game;

import java.util.List;
import java.util.Random;

import Protocol.Pcmd;
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
            // Recuperation Nom et PV adversaire
            player.setFightMessageIn(professeur.getNom());
            player.setFightMessageIn(String.valueOf(professeur.getPv()));

            inFight = true;
            player.setInFight(true);
            round = 0;

            while (inFight) {

                // todo: function to avoid duplicated code
                // Mode question ou Réponse ?
                player.setFightMessageIn(Pfight.ANSWER);

                Random generator = new Random();
                //Question question = ConnectionDB.getQuestionById(generator.nextInt(3) + 1);
                List<Question> questions = professeur.getQuestions();
                Question question = questions.get(generator.nextInt(questions.size()));

                // envoie la question du prof au player.
                player.setFightMessageIn(question.getQuestion());

                // transmet les choix de réponses au joueur
                // fixme: TROUVER UN MOYEN DE METTRE DE l ALEATOIRE
                String repPayloadJson = JsonCreator.sendReponses(question);
                player.setFightMessageIn(repPayloadJson);

                // envoie des items disponnible au joueur
                String itemsPayloadJson = JsonCreator.sendItems(player.getItems());
                player.setFightMessageIn(itemsPayloadJson);

                boolean hasAnswer = false;
                while (!hasAnswer) {

                    switch (player.getFightMessageOut()) {
                        case Pfight.USE_ITEM:

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

                            // envoi du choix de réponse possible au handler
                            repPayloadJson = JsonCreator.sendReponses(question);
                            System.out.println(repPayloadJson);
                            player.setFightMessageIn(repPayloadJson);

                            // envoie de l'état du joueur au handler
                            player.setFightMessageIn(JsonCreator.SendPlayer(player));

                            break;

                        case Pfight.ANSWER:

                            String response = player.getFightMessageOut();

                            String choixReponse = JsonCreator.parseReponseByLetter(repPayloadJson, response);

                            if(question.getReponseOK().equals(choixReponse)){
                                player.setFightMessageIn("RIGHT");
                                professeur.loosePv(40);
                            }

                            else {
                                player.setFightMessageIn("FALSE");
                                player.loosePV(40);
                            }

                            hasAnswer = true;

                            break;
                    }
                }

                // Envoi etat adversaire et joueur à player1
                player.setFightMessageIn(JsonCreator.SendProfesseur(professeur));
                player.setFightMessageIn(JsonCreator.SendPlayer(player));

                if(player.getNbPV() <= 0 || professeur.getPv() <= 0){
                    inFight = false;
                    player.setInFight(false);
                }

            } // END OF FIGHT

            if (player.getNbPV() <= 0) {
                player.setFightMessageIn(Pfight.END);
                player.setFightMessageIn(Pfight.LOST);
            }
            if (professeur.getPv() <= 0) {
                player.setFightMessageIn(Pfight.END);
                player.setFightMessageIn(Pfight.WIN);

            }


        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        player.setInFight(false);
    }
}