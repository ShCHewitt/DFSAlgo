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

        //Constructors for individual player performances.


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
        private double averageUsageRate;
        private String position;
        private double cost;
        private double averagePoints;
        private double stdDev;

        //Constructor for the list of actual players that will be used to output to the console.
        public Player(int playerID, String name, String teamName, double averageMinutesPlayed, double averageUsageRate,
                      String position, double cost, double averagePoints, double stdDev) {
            this.playerID = playerID;
            this.name = name;
            this.teamName = teamName;
            this.averageMinutesPlayed = averageMinutesPlayed;
            this.averageUsageRate = averageUsageRate;
            this.position = position;
            this.cost = cost;
            this.averagePoints = averagePoints;
            this.stdDev = stdDev;
        }

        public String toString() {
            String string = "Name: " + name + " Team: " + teamName + " Average Fanduel Points: " + averagePoints + " Standard Deviation: " +
                    stdDev;
            return string;
        }
    }

    private class sortByFanduelPoints implements Comparator<Player> {
        public int compare(Player p1, Player p2) {
            return (int) (p2.averagePoints-p1.averagePoints);
        }
    }

    private class sortByCost implements Comparator<Player> {
        public int compare(Player p1, Player p2) {
            return (int) (p2.cost - p1.cost);
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
            double totalUsageRate = 0;
            double totalDeviation = 0;
            int appearances = 0;

            for (int i = 0; i < map.get(player).size();i++) {
                totalPoints += map.get(player).get(i).points;
                if (map.get(player).get(i).cost.equals("#N/A")) {
                    map.get(player).get(i).setCost("3500");
                }
                totalCost += Double.parseDouble(map.get(player).get(i).cost);
                totalMinutesPlayed += map.get(player).get(i).minutesPlayed;
                totalUsageRate += map.get(player).get(i).usageRate;
                appearances += 1;
            }

            double averagePoints = totalPoints/appearances;
            double averageCost = totalCost/appearances;
            double averageMinutesPlayed = totalMinutesPlayed/appearances;
            double averageUsageRate = totalUsageRate/appearances;

            double dev;
            double totalDev = 0;
            for (int i=0;i < map.get(player).size(); i++) {
                dev = Math.abs(map.get(player).get(i).points - averagePoints);
                dev = dev * dev;
                totalDev += dev;
            }
            double stdDev = Math.sqrt(Math.abs(totalDev)/appearances);

            //make the player.
            Player aggregatedPlayer = new Player(player, map.get(player).get(0).name, map.get(player).get(0).teamName,
                    averageMinutesPlayed, averageUsageRate, map.get(player).get(0).position, averageCost,averagePoints, stdDev);

            //put the player in the list.
            players.add(players.size(), aggregatedPlayer);
        }
        return players;
    }

    public ArrayList<Player> sortBy(String what, ArrayList<Player> players) {
        switch (what) {
            case "fdPoints":
                Comparator<Player> fdPoints = new sortByFanduelPoints();
                players.sort(fdPoints);
                return players;
            case "cost":
                Comparator<Player> value = new sortByCost();
                players.sort(value);
                return players;
            case "minutesPlayed":
//            Comparator<Player> minPlayed = new sortByMinutesPlayed();
//            players.sort(minPlayed);
                return players;
        }

        return players;
    }

    public ArrayList<Player> consoleSort(ArrayList<Player> players) {
        Scanner userInput = new Scanner(System.in);

        String response = "";

        System.out.println("What would you like to sort by? \n" +
                "Your options are: \n" +
                "[Fanduel Points] Fanduel Points (High to Low)\n" +
                "[Average Cost] Average Cost (High to Low)");

        response = userInput.nextLine();

        switch (response) {
            case "Fanduel Points":
                sortBy("fdPoints", players);
                System.out.println(players);
                return players;
            case "Average Cost":
                sortBy("cost", players);
                System.out.println(players);
                return players;
        }
        return players;
    }




    public static void main(String[] args) {
        String filename = "data/nbaDFSdata.txt";
        Fandy shane = new Fandy();
        try {
            ArrayList<Player> players = shane.aggregateMap(shane.makePerformanceMap(filename));

            System.out.println(shane.sortBy("cost", players));
            shane.consoleSort(players);

        } catch (Exception e) {
            System.out.println("Something went wrong.");
        }

    }
}