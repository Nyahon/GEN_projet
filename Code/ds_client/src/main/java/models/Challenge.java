package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Challenge {

    private Player player;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Scanner scanner = new Scanner(System.in);
    private boolean isInChallengeMode = true;
    private static final Logger LOG = Logger.getLogger(WaitChallenge.class.getName());

    public Challenge(Player player) {
        this.player = player;

        try {
            in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
            out = new PrintWriter(player.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launch() throws IOException, InterruptedException {
        while (isInChallengeMode) {
            System.out.println(in.readLine());
            sendCMD(scanner.nextLine().toUpperCase().trim());
        }

    }

    public void sendCMD (String cmd) throws IOException, InterruptedException {
        out.println(cmd);
        out.flush();
        switch (cmd) {
            case "EXIT":
                isInChallengeMode = false;
                break;

            case "LIST_PLAYERS":
                System.out.println(in.readLine());
                break;

            case "FIGHT":
                System.out.println("Enter the name of your challenger");
                out.println(scanner.nextLine());
                out.flush();
                if (in.readLine().equals("FAIL")) {
                    System.out.println("Player not connected or not waiting for a challenge");
                }
                else {
                    System.out.println("You Challenge a dude !");
                    Fight fight = new Fight(player);
                    fight.startFight();
                    isInChallengeMode = false;
                }
                break;


            default :

        }
    }

}

