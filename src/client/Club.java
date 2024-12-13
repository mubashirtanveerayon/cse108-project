package client;

import database.IOWrapper;
import entities.Player;
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

    public Club(String name, ArrayList<Player> players, HashMap<Player,String> forAuction) {
        this.name = name;
        this.players = players;
        this.forAuction = forAuction;
        clubAttribute = new StringAttribute(name, AttributeKey.CLUB);
    }

    public static void setClub(Club club) {
        Club.club = club;
    }

    public static Club getClub() {
        return club;
    }





}