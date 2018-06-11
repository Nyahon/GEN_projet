package controllers;

import Protocol.Pfight;
import Protocol.Pinfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.JsonCreator;
import models.Player;
import models.Question;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import sun.awt.image.ImageWatched;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fight extends mainController {

    @FXML
    private Label ennemisLabel; // name of the adversary

    @FXML
    private Label itemChoisis;

    @FXML
    private Label ennemisName;

    @FXML
    private Label myName;

    @FXML
    private Label winOrLost;

    @FXML
    private Label rightOrFalse;

    @FXML
    private ProgressBar myLifeBar;

    @FXML
    private ProgressBar hisLifeBar;

    @FXML
    private Label hisLife;

    @FXML
    private Label myLife;

    @FXML
    private ImageView imgMe;

    @FXML
    private ImageView imgAdversary;

    // ITEM PANE

    @FXML
    private Pane item;

    ToggleGroup chooseItem;
    @FXML
    private RadioButton antiseche;

    @FXML
    private RadioButton livre;

    @FXML
    private RadioButton biere;

    @FXML
    private Button choiceItem;

    @FXML
    private ImageView bitBier;

    @FXML
    private ImageView bitBook;

    @FXML
    private ImageView bitAntiseche;

    // ASKING PANE

    @FXML
    private Pane asking;

    @FXML
    private ChoiceBox<String> choiceQuestion;

    @FXML
    private Button askQuestion; // clic to ask your question


    // RESPOND PANE

    @FXML
    private Pane respond;

    @FXML
    private Label question;

    ToggleGroup chooseResponse;
    @FXML
    private RadioButton firstRespond;
    @FXML
    private RadioButton SecondRespond;
    @FXML
    private RadioButton thirstRespond;
    @FXML
    private RadioButton fourthRespond;

    @FXML
    private Button respondQuestion; // clic to ask your question


    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private int myInitialPV;
    private int hisInitialPV;

    private static final Logger LOG = Logger.getLogger(models.Fight.class.getName());

    private boolean inFight;
    private Player player;
    private Player opponnent = new Player();
    private boolean itemUse = false;

    @FXML
    protected void initialize(Player player) {

        chooseResponse = new ToggleGroup();

        firstRespond.setToggleGroup(chooseResponse);
        SecondRespond.setToggleGroup(chooseResponse);
        thirstRespond.setToggleGroup(chooseResponse);
        fourthRespond.setToggleGroup(chooseResponse);

        chooseItem = new ToggleGroup();

        livre.setToggleGroup(chooseItem);
        antiseche.setToggleGroup(chooseItem);
        biere.setToggleGroup(chooseItem);

        try {
            in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
            out = new PrintWriter(player.getSocket().getOutputStream());

            this.player = player;

            // get oppenents infos
            // opponnent.setName(in.readLine());
            //opponnent.setNbPV(Integer.parseInt(in.readLine()));
            Player temp = JsonCreator.readPlayer(in.readLine());
            opponnent.setName(temp.getName());
            opponnent.setNbPV(temp.getNbPV());
            opponnent.setImage(temp.getImage());


            myInitialPV = player.getNbPV();
            hisInitialPV = opponnent.getNbPV();

            // Set labels Name Value
            ennemisLabel.setText(opponnent.getName());
            ennemisName.setText(opponnent.getName());
            myName.setText(player.getName());

            // disable un-use Pane
            respond.setVisible(false);
            asking.setVisible(false);
            item.setVisible(false);
            winOrLost.setVisible(false);

            // TODO : METTRE UNE IMAGE DIFFERENTE SELON LE TYPE DE CLASSE

            Image meImg = new Image("/images/" + player.getImage());
            imgMe.setImage(meImg);

            Image HisImg = new Image("/images/" + opponnent.getImage());
            imgAdversary.setImage(HisImg);

            Image imgKey = new Image("/images/bitKey.jpg");
            bitAntiseche.setImage(imgKey);

            Image imgBook = new Image("/images/bitBook.png");
            bitBook.setImage(imgBook);

            Image imgBier = new Image("/images/bitBiere.png");
            bitBier.setImage(imgBier);

            // Set labels life Value
            setLifeLabel(player, opponnent);

            inFight = true;
            //start the fight
            beginRound();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void beginRound() {
        // start loop for the fight until server tels one of the player is dead
        Player temp;
        String tmp = "";
        if (inFight) {

            itemChoisis.setVisible(false);
            respond.setVisible(false);
            asking.setVisible(false);

            try {

                tmp = in.readLine().toUpperCase();
                LOG.log(Level.INFO, "ATTEND DE RECEVOIR ASK OR ANSWER OR END : " + tmp);

                switch (tmp) {
                    case Pfight.ASK:
                        askPart();
                        break;

                    case Pfight.ANSWER:
                        respondPart();
                        break;

                    case Pfight.END:
                        switch (in.readLine().toUpperCase()) {
                            case Pfight.WIN:
                                System.out.println("You won the fight !");
                                winOrLost.setVisible(true);
                                winOrLost.setTextFill(Color.GREEN);
                                winOrLost.setText("You won the fight !");
                                break;
                            case Pfight.LOST:
                                System.out.println("You lost the fight !");
                                winOrLost.setVisible(true);
                                winOrLost.setTextFill(Color.RED);
                                winOrLost.setText("You lost the fight !");
                                break;
                        }
                        Thread.sleep(2);
                        if (in.readLine().equals("CONTINUE")) {
                            nextFight();
                        } else {
                            returnHome();
                        }
                        break;

                    default:
                        break;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setLifeLabel(Player player, Player opponnent) {
        // Set labels life Value
        myLife.setText(Integer.toString(player.getNbPV()));
        hisLife.setText(Integer.toString(opponnent.getNbPV()));
        myLifeBar.setProgress((double) player.getNbPV() / myInitialPV);
        hisLifeBar.setProgress((double) opponnent.getNbPV() / hisInitialPV);
    }

    private void askPart() {
        try {
            //Activate ASK Pane
            Interface_Fight_Question();

            System.out.print("Ask a question for your opponent : ");
            System.out.println("select a question between these by his id : ");

            this.player.afficheQuestions();
            ObservableList<String> questString = FXCollections.observableArrayList();
            for (Question q : player.getQuestions())
                questString.add(q.getQuestion());

            choiceQuestion.setItems(questString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void respondPart() {
        try {
            LOG.log(Level.INFO, "doit charger la nouvelle interface");
            //Activate Respond Pane
            Interface_Fight_Respond();
            LOG.log(Level.INFO, "nouvelle interface chargée");

            antiseche.setDisable(true);
            antiseche.setText(String.valueOf("0 : Antisèches"));
            livre.setDisable(true);
            livre.setText(String.valueOf("0 : Livre"));
            biere.setDisable(true);
            biere.setText(String.valueOf("0 : Bières"));

            // Affiche la question
            String receiveQuestion = in.readLine();
            LOG.log(Level.INFO, "affiche la question suivante : " + receiveQuestion);
            question.setText(receiveQuestion);

            //récupère les choix de réponses
            String tmp = in.readLine();
            LinkedList<String> reponses = JsonCreator.GetReponsesList(tmp);
            firstRespond.setText(reponses.get(0));
            SecondRespond.setText(reponses.get(1));
            thirstRespond.setText(reponses.get(2));
            fourthRespond.setText(reponses.get(3));

            // récupère la liste des objets.
            // TODO récupère les objets
            String itemsPayload = in.readLine();
            int[] items = JsonCreator.GetNumberOfEveryItem(itemsPayload);
            if (items[0] > 0) {
                antiseche.setDisable(false);
                antiseche.setText(String.valueOf(items[0]) + ": Antisèches");
            }
            if (items[1] > 0) {
                livre.setDisable(false);
                livre.setText(String.valueOf(items[1]) + ": Livre");
            }
            if (items[2] > 0) {
                biere.setDisable(false);
                biere.setText(String.valueOf(items[2]) + ": Bières");
            }

            //ATTENT ACTION SUR BOUTON.

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void askTheQuestion() {
        for (Question q : player.getQuestions()) {
            if (q.getQuestion().equals(choiceQuestion.getValue())) {
                //FIXME COMMENTE POUR TESTs
                out.println(q.getId());
                out.flush();
            }
        }

        System.out.println("Wait your opponent to answer your question.");

        // result of opponent

        rightOrFalse.setTextFill(Color.GREEN);
        rightOrFalse.setText("question ask");

        getRightORFalse();
    }

    @FXML
    private void respondToTheQuestion() {

        out.println(Pfight.ANSWER);
        System.out.println("Bouton répondre appuié ");
        try {

            // Envoie de la réponse choisie
            if (firstRespond.isSelected()) {
                out.println("A");
            }

            if (SecondRespond.isSelected()) {
                out.println("B");
            }

            if (thirstRespond.isSelected()) {
                out.println("C");
            }

            if (fourthRespond.isSelected()) {
                out.println("D");
            }
            out.flush();

            getRightORFalse();

        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }

    @FXML
    private void useTheItem() {

        out.println(Pfight.USE_ITEM);

        try {

            if (antiseche.isSelected()) {
                out.println(Pfight.ITEM_ANTISECHE);
                out.flush();
            }

            if (livre.isSelected()) {
                out.println(Pfight.ITEM_LIVRE);
                out.flush();
            }

            if (biere.isSelected()) {
                out.println(Pfight.ITEM_BIERE);
                out.flush();
            }

            itemChoisis.setVisible(true);

            //récupère les choix de réponses
            LinkedList<String> reponses = JsonCreator.GetReponsesList(in.readLine());
            firstRespond.setText(reponses.get(0));
            SecondRespond.setText(reponses.get(1));
            thirstRespond.setText(reponses.get(2));
            fourthRespond.setText(reponses.get(3));

            //récupère l'etat de notre joueur.
            Player temp = JsonCreator.readPlayer(in.readLine());
            player.setNbPV(temp.getNbPV());
            setLifeLabel(player, opponnent);

        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }

    private void getRightORFalse() {
        try {

            String tmp = in.readLine().toUpperCase();
            Player temp;
            LOG.log(Level.INFO, "ATTEND DE RECEVOIR RIGHT OR FALSE : " + tmp);

            switch (tmp) {
                case Pfight.RIGHT:
                    rightOrFalse.setTextFill(Color.GREEN);
                    rightOrFalse.setText("Right");

                    break;

                case Pfight.FALSE:
                    rightOrFalse.setTextFill(Color.RED);
                    rightOrFalse.setText("False");

                    break;
            }

            //Mise a jour des points de vie généraux.
            temp = JsonCreator.readPlayer(in.readLine());
            opponnent.setNbPV(temp.getNbPV());
            temp = JsonCreator.readPlayer(in.readLine());
            player.setNbPV(temp.getNbPV());

            setLifeLabel(player, opponnent);
            beginRound();

        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }

    @FXML
    private void returnHome() {
        Stage stage = (Stage) respond.getScene().getWindow();
        stage.close();
    }


    private void nextFight() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fight.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        Fight fight = fxmlLoader.<Fight>getController();
        fight.initialize(player);
        stage.show();
        //Stage stage = (Stage) close.getScene().getWindow();
        //stage.close();
    }

    @FXML
    private void Interface_Fight_Question() {
        respond.setVisible(false);
        item.setVisible(false);
        asking.setVisible(true);
    }

    @FXML
    private void Interface_Item() {
        respond.setVisible(false);
        item.setVisible(true);
        asking.setVisible(false);
    }

    @FXML
    private void Interface_Fight_Respond() {
        respond.setVisible(true);
        LOG.log(Level.INFO, "respond visible TRUE");
        item.setVisible(false);
        LOG.log(Level.INFO, "item visible FALSE");
        asking.setVisible(false);
        LOG.log(Level.INFO, "asking visible FALSE");
    }

}
