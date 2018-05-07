package server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Player;
import models.Question;

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
}
