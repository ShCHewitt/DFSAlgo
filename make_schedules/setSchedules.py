import urllib
from bs4 import BeautifulSoup
import pandas as pd
import xlsxwriter
import schedule

# A quick fix to a certificate authentification failed error. Specific to my computer.
import os, ssl
if (not os.environ.get('PYTHONHTTPSVERIFY', '') and
    getattr(ssl, '_create_unverified_context', None)):
    ssl._create_default_https_context = ssl._create_unverified_context

# URLs for each of the months that we need to scrape through:
# https://www.basketball-reference.com/leagues/NBA_2019_games-october.html
# https://www.basketball-reference.com/leagues/NBA_2019_games-november.html
# https://www.basketball-reference.com/leagues/NBA_2019_games-december.html
# https://www.basketball-reference.com/leagues/NBA_2019_games-january.html
# https://www.basketball-reference.com/leagues/NBA_2019_games-february.html
# https://www.basketball-reference.com/leagues/NBA_2019_games-march.html
# https://www.basketball-reference.com/leagues/NBA_2019_games-april.html
# regular season ends april 15th.
#

def main():
    data_scrape("https://www.basketball-reference.com/leagues/NBA_2019_games-october.html", "october")
    data_scrape("https://www.basketball-reference.com/leagues/NBA_2019_games-november.html", "november")
    data_scrape("https://www.basketball-reference.com/leagues/NBA_2019_games-december.html", "december")
    data_scrape("https://www.basketball-reference.com/leagues/NBA_2019_games-january.html", "january")
    data_scrape("https://www.basketball-reference.com/leagues/NBA_2019_games-february.html", "february")
    data_scrape("https://www.basketball-reference.com/leagues/NBA_2019_games-march.html", "march")
    data_scrape("https://www.basketball-reference.com/leagues/NBA_2019_games-april.html", "april")


def data_scrape(url, month):
    print("Entering Data Scrape for making NBA team schedules.")

    html = urllib.request.urlopen(url)

    soup = BeautifulSoup(html, features="html.parser")

    soup.findAll('tr', limit=2)

    headers = [th.getText() for th in soup.findAll('tr', limit=2)
               [0].findAll('th')]

    headers = headers[1:]


    rows = soup.findAll('tr')[1:]

    rest_data = [[td.getText() for td in rows[i].findAll('td')]
                 for i in range(len(rows))]

    dates = [[data.getText() for data in rows[i].findAll('th')]
                for i in range(len(rows))]

    print(dates)

    dates2 = pd.DataFrame(dates)

    other_data = pd.DataFrame(rest_data, columns = headers)

    frames = [dates2, other_data]

    result = pd.concat(frames, axis=1)

    writer = pd.ExcelWriter('../data/' + month + '.xlsx', engine='xlsxwriter')

    result.to_excel(writer, sheet_name='Sheet1')

    writer.save()

    return result

# Run the file
main()

