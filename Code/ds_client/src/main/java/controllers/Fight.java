package controllers;

import Protocol.Pfight;
import Protocol.Pinfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fight extends mainController {

    @FXML
    private Label ennemisLabel; // name of the adversary

    // ASKING PANE

    @FXML
    private Pane asking;

    @FXML
    private ChoiceBox<String> choiceQuestion;

    @FXML
    private Button askQuestion; // clic to ask your question

    @FXML
    private Label ennemisLabelAsking; // name of the adversary

    @FXML
    private Label askHisLife; // life of the adversary

    @FXML
    private Label askMyLife; // my life


    // RESPOND PANE

    @FXML
    private Pane respond;

    @FXML
    private RadioButton firstRespond;
    @FXML
    private RadioButton SecondRespond;
    @FXML
    private RadioButton thirstRespond;
    @FXML
    private RadioButton fourthRespond;

    @FXML
    private RadioButton antiseche;

    @FXML
    private RadioButton livre;

    @FXML
    private RadioButton biere;

    @FXML
    private RadioButton noItems;

    @FXML
    private Button choiceItem;

    @FXML
    private Button respondQuestion; // clic to ask your question

    @FXML
    private Label ennemisLabelRespond; // name of the adversary

    @FXML
    private Label respHisLife; // life of the adversary

    @FXML
    private Label respMyLife; // my life

    @FXML
    private Label question; // my life


    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private static final Logger LOG = Logger.getLogger(models.Fight.class.getName());

    private boolean inFight;
    private Player player;
    private Player opponnent = new Player();
    private boolean itemUse = false;

    @FXML
    protected void initialize(Player player) {
        try {
            in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
            out = new PrintWriter(player.getSocket().getOutputStream());

            this.player = player;

            // get oppenents infos
            opponnent.setName(in.readLine());
            opponnent.setNbPV(Integer.parseInt(in.readLine()));

            System.out.println("Votre adversaire: ---------------------------------------------------------------------");
            System.out.println(opponnent.getName().toUpperCase());
            System.out.println("PV: " + opponnent.getNbPV());
            System.out.println("---------------------------------------------------------------------------------------");

            // Set labels Name Value
            ennemisLabel.setText(opponnent.getName());
            ennemisLabelRespond.setText(opponnent.getName());
            ennemisLabelAsking.setText(opponnent.getName());

            // disable un-use Pane
            respond.setVisible(false);
            asking.setVisible(false);

            // Set labels life Value
            setLifeLabel();

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setLifeLabel() {
        // Set labels life Value
        askMyLife.setText(Integer.toString(this.player.getNbPV()));
        respMyLife.setText(Integer.toString(this.player.getNbPV()));

        askHisLife.setText(Integer.toString(opponnent.getNbPV()));
        respHisLife.setText(Integer.toString(opponnent.getNbPV()));
    }

    private void askPart() {
        try {
            //Activate ASK Pane
            asking.setVisible(true);

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
            //Activate Respond Pane
            respond.setVisible(true);

            System.out.println("Wait for your opponent to ask you a quesiton.");
            // Affiche la question
            String receiveQuestion = in.readLine();
            System.out.println(receiveQuestion);
            question.setText(receiveQuestion);

            //récupère les choix de réponses
            LinkedList<String> reponses = JsonCreator.GetReponsesList(in.readLine());
            firstRespond.setText(reponses.get(0));
            SecondRespond.setText(reponses.get(1));
            thirstRespond.setText(reponses.get(2));
            fourthRespond.setText(reponses.get(3));

            // Demande les items
            // Proposer l'utilisation d'un item.
            System.out.println("Voulez vous utilisez un item ? (O/N)");
            System.out.print("Choisir un des item possible :");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void askTheQuestion() {
        for (Question q : player.getQuestions()) {
            if (q.getQuestion().equals(choiceQuestion.getValue())) {
                out.println(q.getId());
                out.flush();
            }
        }

        try {
            System.out.println("Wait your opponent to answer your question.");

            // result of opponent

            getRightORFalse();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void respondToTheQuestion() {
        try {
            // réafficher les réponses possible
            in.readLine();
            System.out.println("Type your answer select you answer between these by the letter: ");
            // Envoie de la réponse choisie

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

        try {

            if (antiseche.isSelected()) {
                out.println(Pfight.USE_ITEM);
                out.flush();

                //TODO : ICI ON RECOIT LES ITEMS MAIS JE VOIS PAS COMMENT LES AFFICHER AVANT
                // TODO: DE LES SELECTIONNER (AFFICHER LEUR NOMBRE).
                System.out.println(in.readLine());
                out.println(Pfight.ITEM_ANTISECHE);
                out.flush();
            }

            if (livre.isSelected()) {
                out.println(Pfight.USE_ITEM);
                out.flush();

                //TODO : ICI ON RECOIT LES ITEMS MAIS JE VOIS PAS COMMENT LES AFFICHER AVANT
                // TODO: DE LES SELECTIONNER (AFFICHER LEUR NOMBRE).
                System.out.println(in.readLine());
                out.println(Pfight.ITEM_LIVRE);
                out.flush();
            }

            if (biere.isSelected()) {
                out.println(Pfight.USE_ITEM);
                out.flush();

                //TODO : ICI ON RECOIT LES ITEMS MAIS JE VOIS PAS COMMENT LES AFFICHER AVANT
                // TODO: DE LES SELECTIONNER (AFFICHER LEUR NOMBRE).
                System.out.println(in.readLine());
                out.println(Pfight.ITEM_BIERE);
                out.flush();
            }

            if (noItems.isSelected()) {
                out.println(Pfight.NO_USE_ITEM);
                out.flush();
            }

        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }

    private void getRightORFalse() {
        try {
            // result
            String tmp = null;

            tmp = in.readLine().toUpperCase();
            Player temp;
            LOG.log(Level.INFO, "ATTEND DE RECEVOIR RIGHT OR FALSE : " + tmp);
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

        } catch (Exception e)

        {
            e.printStackTrace();
        }
        setLifeLabel();
        beginRound();
    }

}
