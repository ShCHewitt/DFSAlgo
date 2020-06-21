"""
This is a file to set and manage the schedules of each team and the entire NBA schedule.
"""

import csv

class Schedule:
    def __init__(self, teamName):
        self.teamName = teamName
        self.teams = list()

    def append_monthly_data(self):

        home_teams = []
        away_teams = []
        home_scores = []
        away_scores = []
        start_times = []

        file_list = list()
        file_list.append("october.csv")
        file_list.append("november.csv")
        file_list.append("december.csv")
        file_list.append("january.csv")
        file_list.append("february.csv")
        file_list.append("march.csv")
        file_list.append("april.csv")

        for file in file_list:
            games = open(file, "r")
            read = csv.reader(games)
            for game in read:
                start_times.append(game[1])
                home_teams.append(game[4])
                home_scores.append(game[5])
                away_teams.append(game[2])
                away_scores.append(game[3])

            # By now the seasons games should be in order in lists.
            # The logic here is screwy im going to bed.
        for home_team in home_teams:
            opponents = list()
            if home_team not in opponents:
                for i in range(len(home_teams)):
                    if home_teams[i] == home_team:
                        opponents.append(away_teams[i])
                team = NBATeam(home_team, opponents)
                # print(team.name)
                # Add the team and their schedule to the overall NBA schedule class.
                self.teams.append(team)
        print(len(self.teams))


class NBATeam:
    def __init__(self, name, opponents):
        self.name = name
        self.opponents = opponents
        
test = Schedule("NBA")
test.append_monthly_data()

            

