package utils;

import entities.Player;
import utils.filter.PlayerSearcher;
import utils.attribute.StringAttribute;
import utils.enums.AttributeKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Club implements Serializable {


    public static Player newPlayer;

    private static Club club;

    public ArrayList<Player>players,allPlayers;

    public HashMap<Player,String>forAuction;

    public final String name;


    public final StringAttribute clubAttribute;

    public PlayerSearcher getSearch() {
        return search;
    }

    private final PlayerSearcher search;

    public Club(String name, ArrayList<Player> allPlayers, HashMap<Player,String> forAuction) {
        this.name = name;
        clubAttribute = new StringAttribute(name, AttributeKey.CLUB);
        this.allPlayers = allPlayers;
        search = new PlayerSearcher(allPlayers);
        this.players = search.filterPlayers(clubAttribute);
        this.forAuction = forAuction;
    }

    public static void setClub(Club club) {
        Club.club = club;

    }

    public static Club getClub() {
        return club;
    }


    public Player getPlayer(Player player){
        for(Player p:allPlayers)if(p.equals(player))return p;
        return null;
    }

    public long getTotalYearlySalary() {
        float total = 0;
        for(Player p:players)
            total += p.getSalary();
        return (long)total * 48;

    }

    public String toString(){
        return name;
    }

}