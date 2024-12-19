package utils.filter;

import entities.Player;
import utils.attribute.Attribute;
import utils.enums.AttributeKey;

import java.util.ArrayList;

public interface Filter extends java.io.Serializable {

    ArrayList<Player> searchPlayersInRange(float low, float high, AttributeKey key);

    ArrayList<Player>filterPlayers(Attribute... attributes);

    ArrayList<Player>getPlayersWithMaximum(String clubName,AttributeKey key);
}
