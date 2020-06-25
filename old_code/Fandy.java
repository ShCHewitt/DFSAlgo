import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Fandy {
  Map<Integer, ArrayList<PlayerPerformance>> performanceMap;
  ArrayList<Player> players;
  PlayerPerformance performance;


//    A class to hold each individual player performance to later be used to aggregate into a single player from the map
//    we will create when we read the file.


  public class PlayerPerformance {
    private int gameID;
    private String gameDate;
    private String name;
    private String teamName;
    private String opponent;
    private boolean startedGame;
    private boolean homeGame;
    private double minutesPlayed;
    private double usageRate;
    private String daysRest;
    private String position;
    private String cost;
    private double points;

    public PlayerPerformance(int gameID, String gameDate, String name, String teamName, String opponent,
                             boolean startedGame, boolean homeGame, double minutesPlayed, double usageRate, String daysRest,
                             String position, String cost, double points) {
      this.gameID = gameID;
      this.gameDate = gameDate;
      this.name = name;
      this.teamName = teamName;
      this.opponent = opponent;
      this.startedGame = startedGame;
      this.homeGame = homeGame;
      this.minutesPlayed = minutesPlayed;
      this.usageRate = usageRate;
      this.daysRest = daysRest;
      this.position = position;
      this.cost = cost;
      this.points = points;
    }

    public void setStartedGame(boolean startedGame) {
      this.startedGame = startedGame;
    }

    public void setHomeGame(boolean homeGame) {
      this.homeGame = homeGame;
    }

    public void setCost(String cost) {
      this.cost = cost;
    }

  }

  public class Player {
    private int playerID;
    private String name;
    private String teamName;
    private double averageMinutesPlayed;
    private String position;
    private double cost;
    private double averagePoints;
    private double stdDev;
    private double value;
    private double fiftyFiftyValue;
    private double consistencyStat; //fiftyFiftyValue multiplied by average fdPoints/stdDev.
    private int fdRank;
    private int costRank;
    private int valueRank;
    private int fiftyRank;
    private int consistencyRank;
    private double averageRank;
    private double percentGamesStarted;


    //Constructor for the list of actual players that will be used to output to the console.
    public Player(int playerID, String name, String teamName, double averageMinutesPlayed,
                  String position, double cost, double averagePoints, double stdDev, double value, double fiftyFiftyValue,
                  double consistencyStat, double percentGamesStarted) {
      this.playerID = playerID;
      this.name = name;
      this.teamName = teamName;
      this.averageMinutesPlayed = averageMinutesPlayed;
      this.position = position;
      this.cost = cost;
      this.averagePoints = averagePoints;
      this.stdDev = stdDev;
      this.value = value;
      this.fiftyFiftyValue = fiftyFiftyValue;
      this.consistencyStat = consistencyStat;
      this.percentGamesStarted = percentGamesStarted;
    }

    public String toString() {
      String string = "Name: " + name + " Team: " + teamName + " Average Fanduel Points: " + averagePoints + " Standard Deviation: " +
              stdDev + " Consistency Stat: " + consistencyStat + " Average Cost: " + cost;
      return string;
    }
  }

  public class Team {
    private String teamName;
    private double averageFanduelPointsPerGame;
    private double averagePlayerRanking;

    public Team (String teamName, double averageFanduelPointsPerGame, double averagePlayerRanking) {
      this.teamName = teamName;
      this.averageFanduelPointsPerGame = averageFanduelPointsPerGame;
      this.averagePlayerRanking = averagePlayerRanking;
    }
  }

  public class Game {
    private String homeTeam;
    private String awayTeam;
    private String startTime;

    public Game (String homeTeam, String awayTeam, String startTime) {
      this.homeTeam = homeTeam;
      this.awayTeam = awayTeam;
      this.startTime = startTime;
    }
  }

  private class sortByFanduelPoints implements Comparator<Player> {
    public int compare(Player p1, Player p2) {
      return Double.compare(p2.averagePoints, p1.averagePoints);
    }
  }

  private class sortByCost implements Comparator<Player> {
    public int compare(Player p1, Player p2) {
      return Double.compare(p2.cost, p1.cost);
    }
  }

  private class sortByValue implements Comparator<Player> {
    public int compare(Player p1, Player p2) {
      return Double.compare(p2.value, p1.value);
    }
  }

  private class sortByFiftyValue implements Comparator<Player> {
    public int compare(Player p1, Player p2) {
      return Double.compare(p2.fiftyFiftyValue, p1.fiftyFiftyValue);
    }
  }

  private class sortByConsistencyStat implements Comparator<Player> {
    public int compare(Player p1, Player p2) {
      return Double.compare(p2.consistencyStat, p1.consistencyStat);
    }
  }

  private class sortByAverageRank implements Comparator<Player> {
    public int compare(Player p1, Player p2) {
      return Double.compare(p1.averageRank, p2.averageRank);
    }
  }

  private class sortByStartRate implements Comparator<Player> {
    public int compare(Player p1, Player p2) {
      return Double.compare(p2.percentGamesStarted, p1.percentGamesStarted);
    }
  }

  private class sortByTeamPoints implements Comparator<Team> {
    public int compare(Team t1, Team t2) {
      return Double.compare(t2.averageFanduelPointsPerGame, t1.averageFanduelPointsPerGame);
    }
  }

  private class sortByAverageTeamRank implements Comparator<Team> {
    public int compare(Team t1, Team t2) {
      return Double.compare(t1.averagePlayerRanking, t2.averagePlayerRanking);
    }
  }


  public Map<Integer, ArrayList<PlayerPerformance>> makePerformanceMap(String filename) throws Exception{
    BufferedReader reader = new BufferedReader(new FileReader(filename));

    performanceMap = new HashMap<>();

    String line;

    while ((line = reader.readLine()) != null) {

      String[] split = line.split(",");

      performance = new PlayerPerformance(Integer.parseInt(split[0]), split[1], split[3], split[4], split[5],
              false, false, Double.parseDouble(split[8]), Double.parseDouble(split[9]),
              split[10], split[11],split[12], Double.parseDouble(split[13]));

      //Have to replace if the game was a home game for them or not.
      String startedGame = split[6];
      String homeGame = split[7];

      if (startedGame.equals("Y")) {
        performance.setStartedGame(true);
      }
      if (homeGame.equals("H")) {
        performance.setHomeGame(true);
      }

      if (performanceMap.get(Integer.parseInt(split[2])) == null) {
        ArrayList<PlayerPerformance> list = new ArrayList<>();
        list.add(0, performance);
        performanceMap.put(Integer.parseInt(split[2]), list);
      } else {
        performanceMap.get(Integer.parseInt(split[2])).add(performanceMap.get(Integer.parseInt(split[2])).size(),performance);
      }

    }
    return performanceMap;
  }


  public ArrayList<Player> aggregateMap(Map<Integer,ArrayList<PlayerPerformance>> map) {
    ArrayList<Player> players = new ArrayList<>();


    for (Integer player: map.keySet()) {
      //reset these values for calculation of each player.
      double totalPoints = 0;
      double totalCost = 0;
      double totalMinutesPlayed = 0;
      int appearances = 0;
      int gamesStarted = 0;

      for (int i = 0; i < map.get(player).size();i++) {
        totalPoints += map.get(player).get(i).points;
        if (map.get(player).get(i).cost.equals("#N/A")) {
          map.get(player).get(i).setCost("3500");
        }
        totalCost += Double.parseDouble(map.get(player).get(i).cost);
        totalMinutesPlayed += map.get(player).get(i).minutesPlayed;
        appearances += 1;

        if (map.get(player).get(i).startedGame) {
          gamesStarted += 1;
        }
      }

      double averagePoints = totalPoints/appearances;
      double averageCost = totalCost/appearances;
      double averageMinutesPlayed = totalMinutesPlayed/appearances;
      double gamesStartedRatio = (double) gamesStarted/ (double) appearances;

      double dev;
      double totalDev = 0;
      for (int i=0;i < map.get(player).size(); i++) {
        dev = Math.abs(map.get(player).get(i).points - averagePoints);
        dev = dev * dev;
        totalDev += dev;
      }
      double stdDev = Math.sqrt(Math.abs(totalDev)/appearances);
      double value = (1000 * averagePoints / averageCost);
      double fifValue = (averagePoints / stdDev);
      double consistencyValue = (value * fifValue);


      //Deals with the not important people and fixes the comparators
      if (stdDev == 0) {
        fifValue = 0;
        consistencyValue = 0;
      }

      //make the player.
      Player aggregatedPlayer = new Player(player, map.get(player).get(0).name, map.get(player).get(0).teamName,
              averageMinutesPlayed, map.get(player).get(0).position, averageCost,averagePoints, stdDev,
              value, fifValue, consistencyValue, gamesStartedRatio);

      //put the player in the list.
      players.add(players.size(), aggregatedPlayer);
    }
    return players;
  }

  public ArrayList<Player> sortBy(String what, ArrayList<Player> players) {
    //In each case, we need to instead of just printing out the toString method, actually call a display data function.
    switch (what) {
      case "fdPoints":
        Comparator<Player> fdPoints = new sortByFanduelPoints();
        players.sort(fdPoints);
        return players;
      case "cost":
        Comparator<Player> cost = new sortByCost();
        players.sort(cost);
        return players;
      case "value":
        Comparator<Player> value = new sortByValue();
        players.sort(value);
        return players;
      case "50val":
        Comparator<Player> val50 = new sortByFiftyValue();
        players.sort(val50);
        return players;
      case "consistency":
        Comparator<Player> consistency = new sortByConsistencyStat();
        players.sort(consistency);
        return players;
      case "avRank":
        Comparator<Player> avRank = new sortByAverageRank();
        players.sort(avRank);
        return players;
      case "startRate":
        Comparator<Player> startRate = new sortByStartRate();
        players.sort(startRate);
        return players;
    }

    return players;
  }

  //Can simplify the inputs
  public ArrayList<Player> consoleSort(ArrayList<Player> players, Map<String, ArrayList<Player>> teams) {
    calculateAveragePositions(players);

    Scanner userInput = new Scanner(System.in);

    String response = "";
    while (!response.equals("quit")) {
      System.out.println("What would you like to sort by? \n" +
              "Your options are: \n" +
              "[1] Fanduel Points (High to Low)\n" +
              "[2] Average Cost (High to Low)\n" +
              "[3] Value (High to Low)\n" +
              "[4] Fifty Fifty Value (High to Low)\n" +
              "[5] Consistency Stat (High to Low)\n" +
              "[6] Average Ranking of the above metrics.\n" +
              "[7] Start Rate.\n" +
              "[8] Rank Teams by Fanduel Points per Game per Player.\n" +
              "[q] Quit");

      response = userInput.nextLine();

      switch (response) {
        case "1":
          System.out.println("Sorting by Fanduel Points.");
          sortBy("fdPoints", players);
          display(players);
//                    System.out.println(players);
          break;
        case "2":
          System.out.println("Sorting by Average Cost.");
          sortBy("cost", players);
          display(players);
//                    System.out.println(players);
          break;
        case "3":
          System.out.println("Sorting by Value.");
          sortBy("value", players);
          display(players);
//                    System.out.println(players);
          break;
        case "4":
          System.out.println("Sorting by Fifty Fifty Value.");
          sortBy("50val", players);
          display(players);
//                    System.out.println(players);
          break;
        case "5":
          System.out.println("Sorting by Consistency Stat.");
          sortBy("consistency", players);
          display(players);
//                    System.out.println(players);
          break;
        case "6":
          System.out.println("Sorting by Average Rank");
          sortBy("avRank", players);
          display(players);
//                    System.out.println(players);
          break;
        case "7":
          System.out.println("Sorting by Start Rate");
          sortBy("startRate", players);
          display(players);
          break;
        case "8":
          System.out.println("Ranking NBA teams by per Player Fanduel Points per Game.");
          rankTeams(teams, "teamPoints");
          //Doesn't output anything.
        default:
          if (!response.equals("q")) {
            System.out.println("nothing was done because you did not enter an option.");
          }
      }
    }
    return players;
  }

  //Calculates the average position of each sort.
  public void calculateAveragePositions(ArrayList<Player> players) {
    Comparator<Player> fd = new sortByFanduelPoints();
    Comparator<Player> value = new sortByValue();
    Comparator<Player> consistency = new sortByConsistencyStat();
    Comparator<Player> fifT = new sortByFiftyValue();

// Figure out a way to make this more optimal, it's just not right now.
    //Not gonna sort by cost because that would not conclude anything.
    players.sort(fd);
    for (int i = 0; i < players.size(); i++) {
      //Place the ranking for each player in a thing.
      players.get(i).fdRank = players.indexOf(players.get(i));
    }

    players.sort(value);
    for (int i = 0; i < players.size(); i++) {
      players.get(i).valueRank = players.indexOf(players.get(i));
    }

    players.sort(consistency);
    for (int i = 0; i < players.size(); i++) {
      players.get(i).consistencyRank = players.indexOf(players.get(i));
    }

    players.sort(fifT);
    for (int i = 0; i < players.size(); i++) {
      players.get(i).fiftyRank = players.indexOf(players.get(i));

      players.get(i).averageRank =  ((double) (players.get(i).fdRank + players.get(i).valueRank + players.get(i).consistencyRank
              + players.get(i).fiftyRank)/ (4.000));
    }
  }

  public void display(ArrayList<Player> players) {
    System.out.println("Name, Team\nAverageRank, Average Fanduel Points, Consistency Stat, Start Rate");
    for (int i = 0; i < players.size(); i ++) {
      System.out.println((i+1) + ".  " + players.get(i).name + "     " + players.get(i).teamName);
      System.out.printf("%.2f\t", players.get(i).averageRank);
      System.out.printf("%.2f\t", players.get(i).averagePoints);
      System.out.printf("%.2f\t", players.get(i).consistencyStat);
      System.out.printf("%.2f\n", players.get(i).percentGamesStarted);

    }
  }

  public Map<String, ArrayList<Player>> makeTeamMap(ArrayList<Player> players) {
    Map<String, ArrayList<Player>> teamMap = new HashMap<>();
    Set<String> teamNames = new HashSet<>();

    for (int i = 0; i < players.size(); i++) {
      //Make the set of all of the team names.
      teamNames.add(players.get(i).teamName);
    }

    for (String teamName: teamNames) {

      ArrayList<Player> roster = new ArrayList<>();

      for (int i = 0; i < players.size(); i++) {

        if (players.get(i).teamName.equals(teamName)) {
          roster.add(roster.size(),players.get(i));
        }

      }
      teamMap.put(teamName, roster);

    }

    return teamMap;
  }

  //Ranks the teams by the most fanduel points scored per player per game.
  public ArrayList<Team> rankTeams (Map<String, ArrayList<Player>> teamRosters, String rankBy) {
    ArrayList<Team> nbaTeams = new ArrayList<>();

    //Loops through all of the teams in the NBA.
    for (String team: teamRosters.keySet()) {
      int numPlayers = 0;
      double totalPoints = 0;
      double totalConsistency = 0;
      for (Player player: teamRosters.get(team)) {
        totalPoints += player.averagePoints;
        totalConsistency += player.averageRank;
        numPlayers++;
      }

      Team t = new Team(team,(totalPoints/numPlayers), (totalConsistency/numPlayers));
      nbaTeams.add(nbaTeams.size(), t);
    }
    if (rankBy.equals("teamPoints")) {
      Comparator<Team> teamPoints = new sortByTeamPoints();

      nbaTeams.sort(teamPoints);
    }
    else if (rankBy.equals("avRank")) {
      Comparator<Team> avRank = new sortByAverageTeamRank();

      nbaTeams.sort(avRank);
    }


    for (Team t1: nbaTeams) {
      System.out.println(t1.teamName);
    }
    return nbaTeams;
  }

  public Map<String, ArrayList<Game>> setSchedules() throws Exception {
    ArrayList<String> months = new ArrayList<>();

    months.add(0, "data/october.csv");
    months.add(1, "data/november.csv");
    months.add(2, "data/december.csv");
    months.add(3, "data/january.csv");
    months.add(4, "data/february.csv");
    months.add(5, "data/march.csv");
    months.add(6, "data/april.csv");

    Set<String> nbaTeams = new HashSet<>();

    for (int i = 0; i < months.size(); i++) {
        BufferedReader reader = new BufferedReader(new FileReader(months.get(i)));

        String line;
        int count = 0;

        //get rid of the first line because it is headers.
        reader.readLine();

        while ((line = reader.readLine()) != null) {

          String[] split = line.split(",");

          Game game = new Game(split[4], split[2], split[1]);

          nbaTeams.add(split[4]);
        }
    }

    Map<String, ArrayList<Game>> leagueSchedule = new HashMap<>();

    for (String team: nbaTeams) {
      ArrayList<Game> games = new ArrayList<>();

      for (int i = 0; i < months.size(); i ++) {
        BufferedReader reader = new BufferedReader(new FileReader(months.get(i)));

        String line;
        reader.readLine();

        while ((line = reader.readLine()) != null) {
          String[] split = line.split(",");
          Game game = new Game(split[4], split[2], split[1]);
          if (team.equals(split[4]) || team.equals(split[2])) {
            games.add(game);
          }
        }
      }
      leagueSchedule.put(team, games);
    }

    return leagueSchedule;
  }

  public ArrayList<Player> rankPlayersByTeam(String teamName, ArrayList<Player> players) {
    Map<String, ArrayList<Player>> teamMap = makeTeamMap(players);
    Comparator<Player> consistencyComparator = new sortByConsistencyStat();
    for (String teamName1: teamMap.keySet()) {
      if (teamName.equals(teamName1)) {
        teamMap.get(teamName).sort(consistencyComparator);
        System.out.println(teamMap.get(teamName));
        return teamMap.get(teamName);
      }
    }
    return null;
  }



  public static void main(String[] args) {
    String filename = "data/nbaDFSdata.txt";
    Fandy shane = new Fandy();
    try {
      ArrayList<Player> players = shane.aggregateMap(shane.makePerformanceMap(filename));

//      Map<String, ArrayList<Player>> teamRosters = shane.makeTeamMap(players);

      //This will eventually be just in the console sort, its only here for testing right now.
//      shane.rankTeams(teamRosters, "teamPoints");

//      shane.setSchedules();
//        shane.makeTeamMap(players);
        shane.rankPlayersByTeam("Milwaukee", players);

//            shane.consoleSort(players);


    } catch (Exception e) {
      System.out.println(e);
    }

  }
}