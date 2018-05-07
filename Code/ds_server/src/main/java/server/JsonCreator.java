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

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(playerNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
