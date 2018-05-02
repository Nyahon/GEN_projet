import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;

public class Fight implements Runnable {
    private Player player1;
    private Player player2;

    private BufferedReader inPlayer1;
    private PrintWriter outPlayer1;

    private BufferedReader inPlayer2;
    private PrintWriter outPlayer2;

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

        try {
            inPlayer1 = new BufferedReader(new InputStreamReader(player1.getClientSocket().getInputStream()));
            inPlayer2 = new BufferedReader(new InputStreamReader(player2.getClientSocket().getInputStream()));
            outPlayer1 = new PrintWriter(player1.getClientSocket().getOutputStream());
            outPlayer2 = new PrintWriter(player2.getClientSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        thread = new Thread(this);
        thread.start();
    }


    public void run() {
        outPlayer1.println(player2.getName());
        outPlayer1.println(player2.getNbPV());
        outPlayer1.flush();

        outPlayer2.println(player1.getName());
        outPlayer2.println(player1.getNbPV());
        outPlayer2.flush();

        inFight = true;
        player1.setInFight(true);
        player2.setInFight(true);
        round = 0;
        try {
            while (inFight) {

                // todo: function to avoid duplicated code

                if (round % 2 == 0) {

                    outPlayer1.println("ASK");
                    outPlayer1.flush();

                    outPlayer2.println("ANSWER");
                    outPlayer2.flush();

                    // transfer question to other player
                    outPlayer2.println(inPlayer1.readLine());
                    outPlayer2.flush();

                    String response = inPlayer2.readLine();

                } else {

                    outPlayer2.println("ASK");
                    outPlayer2.flush();

                    outPlayer1.println("ANSWER");
                    outPlayer1.flush();


                    // transfer question to other player
                    outPlayer1.println(inPlayer2.readLine());
                    outPlayer1.flush();

                    String response = inPlayer1.readLine();

                }
                // fixme: for the first sprint and for the demo answer is always defined as false;

                outPlayer1.println("FALSE");
                outPlayer1.println("40");
                outPlayer1.flush();

                outPlayer2.println("FALSE");
                outPlayer2.println("40");
                outPlayer2.flush();

                player2.loosePV(40);



                if(player1.getNbPV() <= 0){
                    outPlayer1.println("END");
                    outPlayer1.println("LOST");
                    outPlayer1.flush();

                    outPlayer2.println("END");
                    outPlayer2.println("WON");
                    outPlayer2.flush();

                    inFight = false;
                }
                if(player2.getNbPV() <= 0){
                    outPlayer1.println("END");
                    outPlayer1.println("WON");
                    outPlayer1.flush();

                    outPlayer2.println("END");
                    outPlayer2.println("LOST");
                    outPlayer2.flush();

                    inFight = false;
                }

                round++;
            } // end while inFight

        } catch (IOException e) {
            e.printStackTrace();
        }

        player1.setInFight(false);
        player2.setInFight(false);
    }
}
