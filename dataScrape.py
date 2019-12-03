from urllib.request import urlopen
from bs4 import BeautifulSoup
import pandas as pd

url = "https://www.basketball-reference.com/leagues/NBA_2020_per_game.html".format()

html = urlopen(url)

soup = BeautifulSoup(html, features="html.parser")

soup.findAll('tr', limit=2)

headers = [th.getText() for th in soup.findAll('tr', limit=2)
           [0].findAll('th')]

headers = headers[1:]

rows = soup.findAll('tr')[1:]
player_stats = [[td.getText() for td in rows[i].findAll('td')]
            for i in range(len(rows))]

stats = pd.DataFrame(player_stats, columns=headers)
print(stats)
