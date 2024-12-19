package utils;

import entities.Player;
import utils.filter.PlayerSearcher;
import utils.attribute.StringAttribute;
import utils.enums.AttributeKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Club implements Serializable {


    private static Club club;

    public ArrayList<Player>players;

    public HashMap<Player,String>forAuction;

    public final String name;


    public final StringAttribute clubAttribute;

    public PlayerSearcher getSearch() {
        return search;
    }

    private final PlayerSearcher search;

    public Club(String name, ArrayList<Player> players, HashMap<Player,String> forAuction) {
        this.name = name;
        this.players = players;
        this.forAuction = forAuction;
        clubAttribute = new StringAttribute(name, AttributeKey.CLUB);
        search = new PlayerSearcher(players);
    }

    public static void setClub(Club club) {
        Club.club = club;

    }

    public static Club getClub() {
        return club;
    }


    public long getTotalYearlySalary() {
        float total = 0;
        for(Player p:players)
            total += p.getSalary();
        return (long)total * 48;

    }
}