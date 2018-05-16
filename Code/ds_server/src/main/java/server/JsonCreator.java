package server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Item;
import models.ItemType;
import models.Player;
import models.Question;
import models.db_models.db_Professeur;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class JsonCreator {

    public static String SendPlayer(Player player) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode playerNode = mapper.createObjectNode();
        playerNode.put("id",player.getId());
        playerNode.put("name",player.getName());
        playerNode.put("level", player.getLevel());
        playerNode.put("pv", player.getNbPV());
        playerNode.put("xp", player.getLevel());
        playerNode.put("annee", player.getAnnee());


        ArrayNode questionsNode = mapper.createArrayNode();
        for(Question question : player.getQuestions()){
            ObjectNode questionNode = mapper.createObjectNode();
            questionNode.put("question",question.getQuestion());
            questionNode.put("idQuestion",question.getId());
            questionNode.put("reponseA",question.getReponseOK());
            questionNode.put("reponseB",question.getReponseFalse1());
            questionNode.put("reponseC",question.getReponseFalse2());
            questionNode.put("reponseD",question.getReponseFalse3());
            questionsNode.add(questionNode);
        }

        playerNode.putPOJO("Questions",questionsNode);
        try {
            return mapper.writeValueAsString(playerNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String SendProfesseur(db_Professeur professeur) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode professeurNode = mapper.createObjectNode();
        professeurNode.put("id",professeur.getId());
        professeurNode.put("name",professeur.getNom());
        professeurNode.put("level", professeur.getNiveau());
        professeurNode.put("pv", professeur.getPv());


        ArrayNode questionsNode = mapper.createArrayNode();
        for(Question question : professeur.getQuestions()){
            ObjectNode questionNode = mapper.createObjectNode();
            questionNode.put("question",question.getQuestion());
            questionNode.put("idQuestion",question.getId());
            questionNode.put("reponseA",question.getReponseOK());
            questionNode.put("reponseB",question.getReponseFalse1());
            questionNode.put("reponseC",question.getReponseFalse2());
            questionNode.put("reponseD",question.getReponseFalse3());
            questionsNode.add(questionNode);
        }

        professeurNode.putPOJO("Questions",questionsNode);
        try {
            return mapper.writeValueAsString(professeurNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String sendReponses(Question question){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        ArrayNode reponsesNodes = mapper.createArrayNode();

        ObjectNode A = mapper.createObjectNode();
        A.put("rep", question.getReponseFalse1());
        A.put("idRep", "A");

        ObjectNode B = mapper.createObjectNode();
        B.put("rep", question.getReponseFalse2());
        B.put("idRep", "B");

        ObjectNode C = mapper.createObjectNode();
        C.put("rep", question.getReponseFalse3());
        C.put("idRep", "C");

        ObjectNode D = mapper.createObjectNode();
        D.put("rep", question.getReponseOK());
        D.put("idRep", "D");

        reponsesNodes.add(A);
        reponsesNodes.add(B);
        reponsesNodes.add(C);
        reponsesNodes.add(D);

        payload.putPOJO("Reponses",reponsesNodes);
        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String parseReponseByLetter(String jsonPayload, String reponse){
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = null;
        try {

            rootNode = objectMapper.readTree(jsonPayload);

            JsonNode reponsesNode = rootNode.path("Reponses");
            Iterator<JsonNode> elements = reponsesNode.elements();

            while(elements.hasNext()){
                JsonNode rep = elements.next();
                if(rep.path("idRep").asText().equals(reponse))
                    return rep.path("rep").asText();
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public static String sendItems(LinkedList<Item> items){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        ArrayNode reponsesNodes = mapper.createArrayNode();

        int nbItemAntiSeche = 0;
        int nbItemLivre = 0;
        int nbItemBiere = 0;

        for(Item item : items){
            switch(item.getType()){
                case AntiSeche:
                    nbItemAntiSeche++;
                    break;
                case Livre:
                    nbItemLivre++;
                    break;
                case Biere:
                    nbItemBiere++;
                    break;
            }
        }

        ObjectNode itemAntiseche = mapper.createObjectNode();
        itemAntiseche.put("item", ItemType.AntiSeche.name());
        itemAntiseche.put("nbAvailable", nbItemAntiSeche);
        itemAntiseche.put("idItem", "1");

        ObjectNode itemLivre = mapper.createObjectNode();
        itemLivre.put("item", ItemType.Livre.name());
        itemLivre.put("nbAvailable", nbItemLivre);
        itemLivre.put("idItem", "2");

        ObjectNode itemBiere = mapper.createObjectNode();
        itemBiere.put("item", ItemType.Biere.name());
        itemBiere.put("nbAvailable", nbItemBiere);
        itemBiere.put("idItem", "3");


        reponsesNodes.add(itemAntiseche);
        reponsesNodes.add(itemLivre);
        reponsesNodes.add(itemBiere);


        payload.putPOJO("Items",reponsesNodes);
        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
