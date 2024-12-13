package utils;

import entities.Player;
import utils.attribute.Attribute;
import utils.attribute.NumericAttribute;
import utils.attribute.StringAttribute;
import utils.enums.AttributeKey;

import java.util.ArrayList;

public interface Filter {

    ArrayList<Player> searchPlayersInRange(float low, float high, AttributeKey key);

    ArrayList<Player>filterPlayers(Attribute... attributes);

    ArrayList<Player>getPlayersWithMaximum(String clubName,AttributeKey key);
}
