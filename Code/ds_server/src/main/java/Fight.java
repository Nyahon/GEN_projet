import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;

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
            player1.setFightMessageIn(player2.getName());
            player1.setFightMessageIn(String.valueOf(player2.getNbPV()));

            player2.setFightMessageIn(player1.getName());
            player2.setFightMessageIn(String.valueOf(player1.getNbPV()));

            inFight = true;
            player1.setInFight(true);
            player2.setInFight(true);
            round = 0;

                while (inFight) {

                    // todo: function to avoid duplicated code

                    if (round % 2 == 0) {

                        player1.setFightMessageIn("ASK");

                        player2.setFightMessageIn("ANSWER");

                        player2.setFightMessageIn(player1.getFightMessageOut());

                        String response = player2.getFightMessageOut();


                    } else {

                        player2.setFightMessageIn("ASK");

                        player1.setFightMessageIn("ANSWER");

                        player1.setFightMessageIn(player2.getFightMessageOut());

                        String response = player1.getFightMessageOut();

                    }
                    // fixme: for the first sprint and for the demo answer is always defined as false;

                    player1.setFightMessageIn("FALSE");
                    player1.setFightMessageIn("40");
                    player2.setFightMessageIn("FALSE");
                    player2.setFightMessageIn("40");

                    player2.loosePV(40);

                    round++;

                    if(player1.getNbPV() <= 0 || player2.getNbPV() <= 0){
                        inFight = false;
                        player1.setInFight(false);
                        player2.setInFight(false);
                    }


                    //player1.notifyWaitingConnection();
                   // player2.notifyWaitingConnection();
                } // end while inFight

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
