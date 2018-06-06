package models;

import Protocol.Pfight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Fight {
    private Player player;
    private Player opponnent = new Player( );

    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private static final Logger LOG = Logger.getLogger(Fight.class.getName());

    private boolean inFight;


    public Fight(Player player) {
        this.player = player;

        try {
            in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
            out = new PrintWriter(player.getSocket().getOutputStream());
            scanner = new Scanner(System.in);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        inFight = true;
        try {
            // get oppenents infos

            opponnent.setName(in.readLine());
            opponnent.setNbPV(Integer.parseInt(in.readLine()));

            System.out.println("Votre adversaire: ---------------------------------------------------------------------");
            System.out.println(opponnent.getName().toUpperCase());
            System.out.println("PV: " + opponnent.getNbPV());
            System.out.println("---------------------------------------------------------------------------------------");


            // start loop for the fight until server tels one of the player is dead
            Player temp;
            String tmp = "";
            while (inFight) {

                tmp = in.readLine().toUpperCase();
                LOG.log(Level.INFO,"ATTEND DE RECEVOIR ASK OR ANSWER OR END : " + tmp);
                switch (tmp) {
                    case Pfight.ASK:
                        System.out.print("Ask a question for your opponent : ");
                        System.out.println("select a question between these by his id : ");
                        this.player.afficheQuestions();
                        out.println(scanner.nextLine());
                        out.flush();
                        System.out.println("Wait your opponent to answer your question.");

                        // result of opponent

                        tmp = in.readLine().toUpperCase();
                        LOG.log(Level.INFO,"ATTEND DE RECEVOIR RIGHT OR FALSE : " + tmp);
                        switch (tmp) {
                            case Pfight.RIGHT:
                                temp = JsonCreator.readPlayer(in.readLine());
                                opponnent.setNbPV(temp.getNbPV());
                                temp = JsonCreator.readPlayer(in.readLine());
                                player.setNbPV(temp.getNbPV());
                                break;
                            case Pfight.FALSE:
                                temp = JsonCreator.readPlayer(in.readLine());
                                opponnent.setNbPV(temp.getNbPV());
                                temp = JsonCreator.readPlayer(in.readLine());
                                player.setNbPV(temp.getNbPV());
                                break;
                        }

                        break;
                    case Pfight.ANSWER:

                        System.out.println("Wait for your opponent to ask you a quesiton.");
                        // Affiche la question
                        System.out.println(in.readLine());

                        //Affiche les choix de réponses
                        JsonCreator.AfficheReponses(in.readLine());

                        // Proposer l'utilisation d'un item.
                        System.out.println("Voulez vous utilisez un item ? (O/N)");
                        String rep = scanner.nextLine();
                        if(rep.toUpperCase().equals("O")){
                            out.println(Pfight.USE_ITEM);
                            out.flush();

                            // réception des items disponnible
                            JsonCreator.AfficherItems(in.readLine());

                            System.out.print("Choisir un des item possible :");

                            // choisir un item
                            switch(Integer.parseInt(scanner.nextLine())){
                                case 1:
                                    out.println(Pfight.ITEM_ANTISECHE);
                                    break;
                                case 2:
                                    out.println(Pfight.ITEM_LIVRE);
                                    break;
                                case 3:
                                    out.println(Pfight.ITEM_BIERE);
                                    break;
                            }
                            out.flush();

                            // réafficher les réponses possible
                            JsonCreator.AfficheReponses(in.readLine());

                        }
                        else{
                            out.println(Pfight.NO_USE_ITEM);
                        }

                        System.out.println("Type your answer select you answer between these by the letter: ");

                        // Envoie de la réponse choisie
                        out.println(scanner.nextLine());
                        out.flush();

                        // result
                        tmp = in.readLine().toUpperCase();
                        LOG.log(Level.INFO,"ATTEND DE RECEVOIR RIGHT OR FALSE : " + tmp);
                        switch (tmp) {
                            case Pfight.RIGHT:
                                temp = JsonCreator.readPlayer(in.readLine());
                                opponnent.setNbPV(temp.getNbPV());
                                temp = JsonCreator.readPlayer(in.readLine());
                                player.setNbPV(temp.getNbPV());
                                break;
                            case Pfight.FALSE:
                                temp = JsonCreator.readPlayer(in.readLine());
                                opponnent.setNbPV(temp.getNbPV());
                                temp = JsonCreator.readPlayer(in.readLine());
                                player.setNbPV(temp.getNbPV());
                                break;
                        }

                        break;

                    case Pfight.END:
                        switch (in.readLine().toUpperCase()) {
                            case Pfight.WIN:
                                System.out.println("You won the fight !");
                                break;
                            case Pfight.LOST:
                                System.out.println("You lost the fight !");
                                break;
                        }
                        inFight = false;
                        break;

                    default:
                        break;

                }
                System.out.println("PV: -------------------------------------------------------------------------------");
                System.out.println(opponnent.getName() + ": " + opponnent.getNbPV());
                System.out.println("Vous: " + player.getNbPV());
                System.out.println("-----------------------------------------------------------------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

