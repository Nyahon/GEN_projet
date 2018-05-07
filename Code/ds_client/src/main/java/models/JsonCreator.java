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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

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

            JsonNode questionsNode = rootNode.path("Questions");
            Iterator<JsonNode> elements = questionsNode.elements();

            while(elements.hasNext()){
                JsonNode question = elements.next();
                Question q = new Question(question.path("idQuestion").asInt(),question.path("question").asText());
                questions.add(q);
            }

            player = new Player(idNode.asInt(),nameNode.asText(),anneeNode.asInt(),pvNode.asInt(),levelNode.asInt(),xpNode.asInt());
            player.setQuestions(questions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return player;
    }
}
