"""
This is a file to set and manage the schedules of each team and the entire NBA schedule.

As of right now this works for setting the NBA schedule into a dictionary where the keys are the team names and the values are lists for the schedules of each team.
"""

import csv


class Game:
    def __init__(self):
        self.home_team = None
        self.home_score = None
        self.away_team = None
        self.away_score = None
        self.start_time = None

class Schedule:
    def __init__(self):
        self.schedule = dict()

    def set_league_schedules(self):

        home_teams = []
        away_teams = []
        home_scores = []
        away_scores = []
        start_times = []

        file_list = list()
        file_list.append("../data/october.csv")
        file_list.append("../data/november.csv")
        file_list.append("../data/december.csv")
        file_list.append("../data/january.csv")
        file_list.append("../data/february.csv")
        file_list.append("../data/march.csv")
        file_list.append("../data/april.csv")

        # Initialize a set of teams.
        list_of_teams = set()

        # Initialize a list of games.
        list_of_games = list()

        for file in file_list:
            games = open(file, "r")
            read = csv.reader(games)
            for game in read:
                # Read into a game variable.
                nba_game = Game()
                nba_game.start_time = game[1]
                nba_game.home_team = game[4]
                nba_game.away_team = game[2]
                nba_game.home_score = game[5]
                nba_game.away_score = game[3]

                # Only care about the regular season games. There are 1230 Regular season games but we need to kill 7 header lines in the games.
                if len(list_of_games) < 1237:
                    list_of_games.append(nba_game)

                # Add to the set of teams just to make sure we have all 30.
                list_of_teams.add(game[4])

        # Remove the headers that were added as a team.
        list_of_teams.remove('Home/Neutral')
        list_of_teams.remove('')

        # Initialize the dictionary for the schedule that will be set to the class
        full_league_schedules = dict()

        # Initialize lists for each of the teams schedules.
        for i in range(30):
            full_league_schedules[list_of_teams.pop()] = list()
        
        # Loop through the games and add to the schedules of each team
        for game in list_of_games:
            # Add the game to the home teams and the away teams schedules as long as the game isnt a header.
            if game.home_team != 'Home/Neutral':
                full_league_schedules[game.away_team].append(game)
                full_league_schedules[game.home_team].append(game)

        self.schedule = full_league_schedules



            

