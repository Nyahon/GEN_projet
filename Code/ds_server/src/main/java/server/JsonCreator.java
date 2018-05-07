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
import models.Player;
import models.Question;

import java.io.IOException;
import java.util.Iterator;

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
}
