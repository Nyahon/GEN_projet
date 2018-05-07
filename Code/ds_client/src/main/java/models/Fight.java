package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.Scanner;

public class Fight {
    private Player player;

    // TODO will be changed to an Object to manage opponent information
    private int opponentLife;
    private String opponentName;

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
            opponentName = in.readLine();
            opponentLife = Integer.parseInt(in.readLine());

            System.out.println("Votre adversaire: ---------------------------------------------------------------------");
            System.out.println(opponentName.toUpperCase());
            System.out.println("PV: " + opponentLife);
            System.out.println("---------------------------------------------------------------------------------------");


            // start loop for the fight until server tels one of the player is dead
            while (inFight) {
                switch (in.readLine().toUpperCase()) {
                    case "ASK":
                        System.out.print("Ask a question for your opponent : ");
                        System.out.println("select a question between these by his id : ");
                        this.player.afficheQuestions();
                        out.println(scanner.nextLine());
                        out.flush();
                        System.out.println("Wait your opponent to answer your question.");

                        // result of opponent
                        switch (in.readLine().toUpperCase()) {
                            case "RIGHT":
                                player.setNbPV(Integer.parseInt(in.readLine()));
                                break;
                            case "FALSE":
                                opponentLife -= Integer.parseInt(in.readLine());
                                break;
                        }

                        break;
                    case "ANSWER":
                        System.out.println("Wait for your opponent to ask you a quesiton.");
                        System.out.println(in.readLine());
                        System.out.println("Type your answer select you answer between these : ");

                        //Affiche les choix de réponses
                        //System.out.println(in.readLine());
                        //System.out.println(in.readLine());
                        //System.out.println(in.readLine());
                        //System.out.println(in.readLine());

                        out.println(scanner.nextLine());
                        out.flush();

                        // result
                        switch (in.readLine().toUpperCase()) {
                            case "RIGHT":
                                opponentLife -= Integer.parseInt(in.readLine());
                                break;
                            case "FALSE":
                                player.setNbPV(Integer.parseInt(in.readLine()));
                                break;
                        }

                        break;

                    case "END":
                        switch (in.readLine().toUpperCase()) {
                            case "WON":
                                System.out.println("You won the fight !");
                                break;
                            case "LOST":
                                System.out.println("You lost the fight !");
                                break;
                        }
                        inFight = false;
                        break;

                    default:
                        break;

                }
                System.out.println("PV: -------------------------------------------------------------------------------");
                System.out.println(opponentName + ": " + opponentLife);
                System.out.println("Vous: " + player.getNbPV());
                System.out.println("-----------------------------------------------------------------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

