import matplotlib
from selenium import webdriver
from pandas import *
import pandas
import numpy as np
import matplotlib.pyplot as plt
from sqlalchemy import *

path_to_chromedriver = "/Users/Shane Hewitt/Downloads/chromedriver_win32 (2)/chromedriver.exe"
browser = webdriver.Chrome(executable_path=path_to_chromedriver)

url = "https://stats.nba.com/players/boxscores-traditional"

browser.get(url)

browser.find_element_by_xpath(
    '/html/body/main/div[2]/div/div[2]/div/div/nba-stat-table/div[1]/div/div/select/option[2]').click()

table = browser.find_element_by_class_name('nba-stat-table')

player_names = []
player_stats = []
player_stats_2 = []
page_count = browser.find_element_by_xpath(
    '/html/body/main/div[2]/div/div[2]/div/div/nba-stat-table/div[1]/div/div/text()[2]')
print(page_count)

for line_id, lines in enumerate(table.text.split('\n')):
    # print(lines)
    if line_id == 0:
        column_names = lines.split(' ')[1:]
        print(column_names)
    else:
        if line_id % 2 == 1:
            player_names.append(lines)
            # print(player_names)
        if line_id % 2 == 0:
            if lines != "PLAYER":
                player_stats_2 = lines.split(' ')[1:]
                player_stats.append(player_stats_2)
        if line_id > 99:
            print("breaking")
            break

print("gets out of loop")

print("setting db")
db = pandas.DataFrame({'player': player_names,
                       'team': [i[0] for i in player_stats],
                       'game_type': [i[1] for i in player_stats],
                       'opponent': [i[2] for i in player_stats],
                       'date': [i[3] for i in player_stats],
                       'win': [i[4] for i in player_stats],
                       'min': [i[5] for i in player_stats],
                       'pts': [i[6] for i in player_stats],
                       'fgm': [i[7] for i in player_stats],
                       'fga': [i[8] for i in player_stats],
                       'fg%': [i[9] for i in player_stats],
                       '3pm': [i[10] for i in player_stats],
                       '3pa': [i[11] for i in player_stats],
                       '3p%': [i[12] for i in player_stats],
                       'ftm': [i[13] for i in player_stats],
                       'fta': [i[14] for i in player_stats],
                       'ft%': [i[15] for i in player_stats],
                       'oreb': [i[16] for i in player_stats],
                       'dreb': [i[17] for i in player_stats],
                       'reb': [i[18] for i in player_stats],
                       'ast': [i[19] for i in player_stats],
                       'stl': [i[20] for i in player_stats],
                       'blk': [i[21] for i in player_stats],
                       'tov': [i[22] for i in player_stats],
                       'pf': [i[23] for i in player_stats],
                       '+/-': [i[24] for i in player_stats]
                       }
                      )
print("db set")

print(db)
