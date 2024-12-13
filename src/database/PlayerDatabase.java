package database;

import utils.Filter;
import utils.PlayerSearcher;
import utils.enums.AttributeKey;
import entities.Player;
import utils.exceptions.MultipleInitializationException;
import utils.attribute.StringAttribute;
import utils.attribute.Attribute;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerDatabase implements Filter {


    ArrayList<Player> players;

    private PlayerSearcher search;

    private static PlayerDatabase database;
    private PlayerDatabase(){
        players = new ArrayList<>();

        try(BufferedReader br=new BufferedReader(new FileReader("data/players.txt"))){
            byte[] data = new byte[500];
            int length=0;
            int status;
            while((status = (byte)br.read())!=-1){
                data[length++] = (byte)status;

                if(status == '\n'){
                    Player player = loadPlayer(data,length);
                    if(player != null)players.add(player);;
                    length=0;
                }

                else if(length == data.length){
                    byte[] newData= new byte[length * 2];
                    System.arraycopy(data,0,newData,0,length);
                    data = newData;
                }
            }

            Player player = loadPlayer(data,length);
            if(player != null)players.add(player);;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }




        search = new PlayerSearcher(players);
    }

    private static Player loadPlayer(byte[] data,int length){
        byte[] lineData = new byte[length];
        System.arraycopy(data,0,lineData,0,length);
        String[]playerInfo = new String(lineData, StandardCharsets.UTF_8).trim().replaceAll("\n","").split(",");
        if(playerInfo.length<8)return null;
        return new Player(playerInfo);


    }


    public static PlayerDatabase getInstance(){
        return database;
    }

    public static void initialize()throws MultipleInitializationException {
        if(database != null)throw new MultipleInitializationException();
        database = new PlayerDatabase();
    }

    public static void close(){




        save();
        database = null;
    }

    public static void save(){
        if(database == null)return;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/players.txt"))){

            for(Player p:database.players){
                String line = String.format("%s,%s,%d,%.2f,%s,%s",p.getName(),p.getCountry(),(int)p.getAge(),p.getHeight(),p.getClub(),p.getPosition());
                if(p.getAttributeCount()==8)line += String.format(",%d,%d",(int)p.getNumber(),(int)p.getSalary());
                else line += String.format(",,%d",(int)p.getSalary());

                bw.write(line);
                bw.write(System.lineSeparator());
            }
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }


    public ArrayList<String> getCountryWisePlayerCount() {


        HashMap<StringAttribute,Integer>map = new HashMap<>();
        for(Player p:players) {
            StringAttribute country =(StringAttribute)p.getAttribute(AttributeKey.COUNTRY);
            if (map.containsKey(country))
                map.put(country, map.get(country)+ 1);
            else map.put(country, 1);
        }

        ArrayList<String>sortedList = new ArrayList<>();

        for(StringAttribute country:map.keySet()){
            int count = map.get(country);
            String output = country + ": "+count;
            int index=sortedList.size();
            for(int i=sortedList.size()-1;i>=0;i--){
                String s = sortedList.get(i);
                int c = Integer.parseInt(s.split(": ")[1]);
                if(c>count){
                    break;
                }else{
                    index--;
                }
            }
            sortedList.add(index,output);
        }



        return sortedList;
    }



    public long getTotalYearlySalary(String clubName){
        Attribute club = new StringAttribute(clubName,AttributeKey.CLUB);
        float total = 0;
        for(Player p:players)
            if(p.containsAttribute(club))total += p.getSalary();
        return (long)total * 48;
    }

    public boolean addPlayer(Player player) {
        if(players.contains(player))return false;
        players.add(player);
        return true;
    }

    @Override
    public ArrayList<Player> searchPlayersInRange(float low, float high, AttributeKey key) {
       return search.searchPlayersInRange(low,high,key);
    }

    @Override
    public ArrayList<Player> filterPlayers(Attribute... attributes) {
        return search.filterPlayers(attributes);
    }

    @Override
    public ArrayList<Player> getPlayersWithMaximum(String clubName, AttributeKey key) {
        return search.getPlayersWithMaximum(clubName,key);
    }
}
