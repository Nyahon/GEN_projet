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

public class JsonCreator {

    public static Player readPlayer(String jsonPayload) {
        ObjectMapper objectMapper = new ObjectMapper();
        Player player = null;
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

            player = new Player(idNode.asInt(),nameNode.asText(),anneeNode.asInt(),pvNode.asInt(),levelNode.asInt(),xpNode.asInt());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return player;
    }
}
