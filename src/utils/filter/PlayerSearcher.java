package utils.filter;

import entities.Player;
import utils.attribute.Attribute;
import utils.attribute.NumericAttribute;
import utils.attribute.StringAttribute;
import utils.enums.AttributeKey;

import java.util.ArrayList;

public final class PlayerSearcher implements Filter,java.io.Serializable{
    private final ArrayList<Player>players;

    public PlayerSearcher(ArrayList<Player> players){
        this.players = players;
    }

    public ArrayList<Player>searchPlayersInRange(float low, float high, AttributeKey key){
        ArrayList<Player> result = new ArrayList<>();
        NumericAttribute lowTag = new NumericAttribute(low,key);
        NumericAttribute highTag = new NumericAttribute(high,key);
        for(Player p:players)
            if(p.attributeInRange(lowTag,highTag))result.add(p);
        return result;
    }

    public ArrayList<Player>filterPlayers(Attribute... attributes){
        ArrayList<Player> filteredPlayers = new ArrayList<>();
        for(Player p:players)
            if(p.containsAttribute(attributes))filteredPlayers.add(p);
        return filteredPlayers;
    }

    public ArrayList<Player>getPlayersWithMaximum(String clubName,AttributeKey key){
        ArrayList<Player>list = new ArrayList<>();
        Attribute club = new StringAttribute(clubName,AttributeKey.CLUB);

        for(Player p:players){
            if(p.containsAttribute(club)){
                Attribute attribute = p.getAttribute(key);
                if(!(attribute instanceof NumericAttribute))continue;
                float value = (Float) attribute.getContent();
                if(list.isEmpty()||((Float)list.get(0).getAttribute(key).getContent())==value){
                    list.add(p);
                }else if(((Float)list.get(0).getAttribute(key).getContent())<value){
                    list.clear();
                    list.add(p);
                }
            }
        }

        return list;
    }
}
