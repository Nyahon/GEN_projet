package models;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Player;
import sun.awt.image.ImageWatched;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class JsonCreator {

    public static Player readPlayer(String jsonPayload) {
        ObjectMapper objectMapper = new ObjectMapper();
        Player player = null;
        LinkedList<Question> questions = new LinkedList<>();
        //read JSON like DOM Parser
        JsonNode rootNode = null;
        try {

            rootNode = objectMapper.readTree(jsonPayload);

            JsonNode idNode = rootNode.path("id");
            JsonNode nameNode = rootNode.path("name");
            JsonNode levelNode = rootNode.path("level");
            JsonNode pvNode = rootNode.path("pv");
            JsonNode xpNode = rootNode.path("xp");
            JsonNode anneeNode = rootNode.path("annee");
            JsonNode imageNode  = rootNode.path("image");

            JsonNode questionsNode = rootNode.path("Questions");
            Iterator<JsonNode> elements = questionsNode.elements();

            while(elements.hasNext()){
                JsonNode question = elements.next();
                Question q = new Question(question.path("idQuestion").asInt(),question.path("question").asText(),
                        question.path("reponseA").asText(),
                        question.path("reponseB").asText(),
                        question.path("reponseC").asText(),
                        question.path("reponseD").asText());
                questions.add(q);
            }

            player = new Player(idNode.asInt(),nameNode.asText(),anneeNode.asInt(),pvNode.asInt(),levelNode.asInt(),xpNode.asInt(), imageNode.asText());
            player.setQuestions(questions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return player;
    }

    public static void AfficheReponses(String jsonPayload){
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = null;
        try {

            rootNode = objectMapper.readTree(jsonPayload);

            JsonNode reponsesNode = rootNode.path("Reponses");
            Iterator<JsonNode> elements = reponsesNode.elements();

            while(elements.hasNext()){
                JsonNode reponse = elements.next();
                System.out.println(reponse.path("idRep").asText() + " : " + reponse.path("rep").asText());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<String> GetReponsesList(String jsonPayload){
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedList<String> responses = new LinkedList<>();
        JsonNode rootNode = null;
        try {

            rootNode = objectMapper.readTree(jsonPayload);

            JsonNode reponsesNode = rootNode.path("Reponses");
            Iterator<JsonNode> elements = reponsesNode.elements();

            while(elements.hasNext()){
                JsonNode reponse = elements.next();
                System.out.println(reponse.path("idRep").asText() + " : " + reponse.path("rep").asText());
                responses.add(reponse.path("idRep").asText() + " : " + reponse.path("rep").asText());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return responses;
    }

    public static void AfficherItems(String jsonPayload){
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = null;
        try{

            rootNode = objectMapper.readTree(jsonPayload);

            JsonNode itemsNode = rootNode.path("Items");
            Iterator<JsonNode> elements = itemsNode.elements();

            while(elements.hasNext()){
                JsonNode item = elements.next();
                System.out.println(item.path("idItem").asText() + " : " + item.path("item").asText() + " -> " + item.path("nbAvailable"));
            }


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int[] GetNumberOfEveryItem (String jsonPayload){
        ObjectMapper objectMapper = new ObjectMapper();
        int[] items = new int[3];
        JsonNode rootNode = null;
        try{

            rootNode = objectMapper.readTree(jsonPayload);

            JsonNode itemsNode = rootNode.path("Items");
            Iterator<JsonNode> elements = itemsNode.elements();

            int numberOfAntiSeche = 0;
            int numberOfLivre = 0;
            int numberOfBiere = 0;
            while(elements.hasNext()){
                JsonNode item = elements.next();
                int numberItem = Integer.parseInt(item.path("nbAvailable").asText());
                if( numberItem > 0)
                switch (item.path("idItem").asText()){
                    case "1" :
                        numberOfAntiSeche += numberItem;
                        break;
                    case "2" :
                        numberOfLivre  += numberItem;
                        break;
                    case "3" :
                        numberOfBiere  += numberItem;
                        break;
                }
            }
            items[0] = numberOfAntiSeche;
            items[1] = numberOfLivre;
            items[2] = numberOfBiere;


        } catch (IOException e){
            e.printStackTrace();
        }

        return items;
    }
}
