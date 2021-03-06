package game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import Protocol.Pcmd;
import Protocol.Pfight;
import models.*;
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
        this.player.setNbPV(100);

        thread = new Thread(this);
        thread.start();
    }


    public void run()  {
        try {
            // Recuperation Nom et PV adversaire
            //player.setFightMessageIn(professeur.getNom());
            //player.setFightMessageIn(String.valueOf(professeur.getPv()));
            player.setFightMessageIn(JsonCreator.SendProfesseur(professeur));

            inFight = true;
            player.setInFight(true);
            round = 0;
            int currentQuestion = 0;
            int nbrQuestions = professeur.getQuestions().size();

            while (inFight) {

                // todo: function to avoid duplicated code
                // Mode question ou Réponse ?
                player.setFightMessageIn(Pfight.ANSWER);

                List<Question> questions = professeur.getQuestions();
                Collections.shuffle(questions);
                Question question = questions.get(currentQuestion % nbrQuestions);
                // envoie la question du prof au player.
                player.setFightMessageIn(question.getQuestion());

                // transmet les choix de réponses au joueur les réponses sont randomizées dans la génération du payload json
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
                                player.addQuestions(question);
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
                winItem(player);

            }


        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        player.setInFight(false);
    }

    private void winItem(Player winner){
        Random random = new Random();
        int number = random.nextInt(3 - 1 + 1) + 1;
        Item item = null;
        switch (number){

            case 1:
                item = new Item(ItemType.Livre);
                break;
            case 2:
                item = new Item(ItemType.Biere);
                break;
            case 3:
                item = new Item(ItemType.AntiSeche);
                break;
        }
        winner.addItem(item);
        LinkedList<Item> items= new LinkedList<>();
        items.add(item);
        ConnectionDB.assignItems(items,winner.getId());
    }
}