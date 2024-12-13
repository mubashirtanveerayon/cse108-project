import utils.enums.AttributeKey;
import utils.enums.MenuState;
import database.PlayerDatabase;
import entities.Player;
import utils.attribute.StringAttribute;
import utils.attribute.Attribute;

import java.util.List;
import java.util.Scanner;

public class CLI {
    // private enum MenuState {MAIN_MENU, PLAYER_SEARCH, CLUB_SEARCH,EXIT,ADD_PLAYER,INVALID};
    private static final String mainMenu = "Main Menu:" +
            "\n(1) Search Players" +
            "\n(2) Search Clubs"+
            "\n(3) Add Player"+
            "\n(4) Exit System";
    private static final String playerSearchMenu = "Player Searching Options:" +
            "\n(1) By Player Name" +
            "\n(2) By Club and Country" +
            "\n(3) By Position" +
            "\n(4) By Salary Range" +
            "\n(5) Country-wise player count" +
            "\n(6) Back to Main Menu";
    private static final String clubSearchMenu = "Club Searching Options:" +
            "\n(1) Player(s) with the maximum salary of a club" +
            "\n(2) Player(s) with the maximum age of a club" +
            "\n(3) Player(s) with the maximum height of a club" +
            "\n(4) Total yearly salary of a club" +
            "\n(5) Back to Main Menu";
    public static void main(String[] args) {


        PlayerDatabase.initialize();


        Scanner sc = new Scanner(System.in);

        MenuState state = MenuState.MAIN_MENU;
        PlayerDatabase database = PlayerDatabase .getInstance();
        do{
            MenuState output = MenuState.INVALID;
            if(state== MenuState.MAIN_MENU){
                System.out.println(mainMenu);
                output = processMainMenu(sc);

            }else if(state==MenuState.PLAYER_SEARCH){
                System.out.println(playerSearchMenu);
                output = processPlayerSearchMenu(sc,database);
            }else if(state == MenuState.CLUB_SEARCH){
                System.out.println(clubSearchMenu);
                output = processClubSearchMenu(sc,database);
            }else if(state== MenuState.ADD_PLAYER){
                output = processAddPlayerMenu(sc,database);
            }
            if(output==MenuState.INVALID)System.out.println("Invalid input");
            else state = output;
        }while(state!= MenuState.EXIT);
        System.out.println("Exiting System...");
        sc.close();
        PlayerDatabase.close();

    }

    public static MenuState processMainMenu(Scanner sc){
        String input = sc.nextLine();
        try{
            int choice = Integer.parseInt(input);
            switch(choice){
                case 1:return MenuState.PLAYER_SEARCH;
                case 2:return MenuState.CLUB_SEARCH;
                case 3:return MenuState.ADD_PLAYER;
                case 4:return MenuState.EXIT;
                default:return MenuState.INVALID;
            }
        }catch(Exception e){
            return MenuState.INVALID;
        }
    }

    public static MenuState processPlayerSearchMenu(Scanner sc,PlayerDatabase playerDatabase){
        String input = sc.nextLine();
        try{
            int choice = Integer.parseInt(input);
            switch(choice){
                case 1:{
                    System.out.print("Enter name: ");
                    String playerName = sc.nextLine();
                    Attribute nameAttribute = new StringAttribute(playerName, AttributeKey.NAME);
                    List<Player>filtered = playerDatabase.filterPlayers(nameAttribute);
                    if(filtered.isEmpty()) System.out.println("No such player with this name");
                    for(Player p:filtered){
                        System.out.println(p);
                        break;
                    }

                    break;
                } case 2:{
                    System.out.print("Enter country: ");
                    String country = sc.nextLine();
                    System.out.print("Enter club: ");
                    String club = sc.nextLine();
                    List<Player>filtered = club.equalsIgnoreCase("any")?playerDatabase.filterPlayers(new StringAttribute(country,AttributeKey.COUNTRY)):playerDatabase.filterPlayers(new StringAttribute(country,AttributeKey.COUNTRY),new StringAttribute(club,AttributeKey.CLUB));
                    if(filtered.isEmpty()) System.out.println("No such player with this country and club");
                    for(int i=0;i<filtered.size();i++)
                        System.out.println((i+1)+"."+filtered.get(i));

                    break;
                } case 3:{
                    System.out.print("Enter position: ");
                    String position = sc.nextLine();
                    List<Player>filtered = playerDatabase.filterPlayers(new StringAttribute(position,AttributeKey.POSITION));
                    if(filtered.isEmpty()) System.out.println("No such player with this position");
                    for(int i=0;i<filtered.size();i++)
                        System.out.println((i+1)+"."+filtered.get(i));
                    break;
                } case 4:{

                    try{
                        System.out.print("Enter lower bound: ");
                        float lower = Float.parseFloat(sc.nextLine());
                        System.out.print("Enter upper bound: ");
                        float upper = Float.parseFloat(sc.nextLine());
                        List<Player>filtered = playerDatabase.searchPlayersInRange(lower,upper,AttributeKey.SALARY);
                        if(filtered.isEmpty()) System.out.println("No such player with this weekly salary range");
                        for(int i=0;i<filtered.size();i++)
                            System.out.println((i+1)+"."+filtered.get(i));


                    }catch(Exception e){
                        return MenuState.INVALID;
                    }
                    break;
                } case 5:{
                    List<String>countryWiseCount = playerDatabase.getCountryWisePlayerCount();
                    for(String s:countryWiseCount)
                        System.out.println(s);
                    break;
                }
                case 6:return MenuState.MAIN_MENU;
                default:return MenuState.INVALID;
            }
        }catch(Exception e){
            return MenuState.INVALID;
        }
        return MenuState.PLAYER_SEARCH;
    }

    public static MenuState processClubSearchMenu(Scanner sc,PlayerDatabase playerDatabase){
        String input = sc.nextLine();
        try{
            int choice = Integer.parseInt(input);

            switch(choice){
                case 1:{
                    System.out.print("Enter club name: ");
                    String clubName = sc.nextLine();
                    List<Player>filtered = playerDatabase.filterPlayers(new StringAttribute(clubName,AttributeKey.CLUB));// playerDatabase.getPlayersWithMaximum(clubName,AttributeKey.SALARY);
                    if(filtered.isEmpty()) System.out.println("No such club with this name");
                    for(int i=0;i<filtered.size();i++)
                        System.out.println((i+1)+"."+filtered.get(i));
                    break;
                } case 2:{
                    System.out.print("Enter club name: ");
                    String clubName = sc.nextLine();
                    List<Player>filtered = playerDatabase.getPlayersWithMaximum(clubName,AttributeKey.AGE);
                    if(filtered.isEmpty()) System.out.println("No such club with this name");
                    for(int i=0;i<filtered.size();i++)
                        System.out.println((i+1)+"."+filtered.get(i));
                    break;
                } case 3:{
                    System.out.print("Enter club name: ");
                    String clubName = sc.nextLine();
                    List<Player>filtered = playerDatabase.getPlayersWithMaximum(clubName,AttributeKey.HEIGHT);
                    if(filtered.isEmpty()) System.out.println("No such club with this name");
                    for(int i=0;i<filtered.size();i++)
                        System.out.println((i+1)+"."+filtered.get(i));
                    break;
                } case 4:{
                    System.out.print("Enter club name: ");
                    String clubName = sc.nextLine();
                    float total = playerDatabase.getTotalYearlySalary(clubName);
                    if(total ==0) System.out.println("No such club with this name");
                    else System.out.println("Total yearly salary: "+(long)total);
                    break;
                }
                case 5:return MenuState.MAIN_MENU;
                default:return MenuState.INVALID;
            }
        } catch (Exception e) {
            return MenuState.INVALID;
        }
        return MenuState.CLUB_SEARCH;
    }

    public static MenuState processAddPlayerMenu(Scanner sc,PlayerDatabase playerDatabase){
        System.out.print("Enter player name: ");
        String playerName = sc.nextLine();
        System.out.print("Enter player country: ");
        String country = sc.nextLine();

        int age,number=-1;
        float height;
        long salary;
        try {
            System.out.print("Enter player age: ");
            age = Integer.parseInt(sc.nextLine());
            System.out.print("Enter player height: ");
            height = Float.parseFloat(sc.nextLine());

        }catch (Exception e){
            return MenuState.INVALID;
        }
        System.out.print("Enter club name: ");
        String clubName = sc.nextLine();
        System.out.print("Enter player position: ");
        String position = sc.nextLine();
        try{
            System.out.print("Enter player number: (press enter to skip)");
            String numberString = sc.nextLine();
            if(!numberString.isEmpty())number = Integer.parseInt(numberString);
            System.out.print("Enter player salary: ");
            salary = Long.parseLong(sc.nextLine());
        }catch(Exception e){
            return MenuState.INVALID;
        }

        Player player;
        if(number==-1)player = new Player(playerName,country,age,height,clubName,position,salary);
        else player = new Player(playerName,country,age,height,clubName,position,number,salary);
        if(!playerDatabase.addPlayer(player)) System.out.println("Player with this name already exists");


        return MenuState.MAIN_MENU;
    }


}
