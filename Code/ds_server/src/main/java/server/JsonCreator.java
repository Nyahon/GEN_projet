package server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;
import models.db_models.db_Professeur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
        playerNode.put("image", player.getImage());



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
        professeurNode.put("image", professeur.getImage());


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

        List<String> questions = new ArrayList<>();
        questions.add(question.getReponseFalse1());
        questions.add(question.getReponseFalse2());
        questions.add(question.getReponseFalse3());
        questions.add(question.getReponseOK());

        // randomizer l'ordre des réponses.
        Collections.shuffle(questions);

        ObjectNode A = mapper.createObjectNode();
        A.put("rep", questions.remove(0));
        A.put("idRep", "A");

        ObjectNode B = mapper.createObjectNode();
        B.put("rep", questions.remove(0));
        B.put("idRep", "B");

        ObjectNode C = mapper.createObjectNode();
        C.put("rep", questions.remove(0));
        C.put("idRep", "C");

        ObjectNode D = mapper.createObjectNode();
        D.put("rep", questions.remove(0));
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

    public static LinkedList<Question> readQuestionsAndProfFromFile(){

        LinkedList<Question> questions = new LinkedList<>();
        try {

            byte[] jsonData = Files.readAllBytes(Paths.get("questions.json"));

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = null;

            rootNode = objectMapper.readTree(jsonData);

            JsonNode reponsesNode = rootNode.path("Questions");
            Iterator<JsonNode> elements = reponsesNode.elements();

            while(elements.hasNext()){
                JsonNode reponse = elements.next();
                questions.add(new Question(reponse.path("id").asInt(),reponse.path("Question").asText(), reponse.path("RepOk").asText(),
                        reponse.path("rep2").asText(), reponse.path("rep3").asText(), reponse.path("rep4").asText()));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    public static void createProfessorsFromFile(){

        try {

            byte[] jsonData = Files.readAllBytes(Paths.get("questions.json"));

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = null;

            rootNode = objectMapper.readTree(jsonData);

            JsonNode reponsesNode = rootNode.path("Profs");
            Iterator<JsonNode> elements = reponsesNode.elements();

            while(elements.hasNext()){
                JsonNode reponse = elements.next();
                ConnectionDB.addProfessor(new db_Professeur(reponse.path("id").asInt(), reponse.path("Prof").asText(), reponse.path("pv").asInt(), reponse.path("niveau").asInt(), reponse.path("Prof").asText().toLowerCase()+".png" ));

                JsonNode listQuestions = reponse.path("questions");

                Iterator<JsonNode> questions = listQuestions.elements();

                while (questions.hasNext()){
                    JsonNode quest = questions.next();
                    ConnectionDB.assignQuestionToProf(reponse.path("id").asInt(),quest.asInt());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
