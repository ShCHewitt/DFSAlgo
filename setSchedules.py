import urllib
from bs4 import BeautifulSoup
import pandas as pd
import xlsxwriter

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
    # lets just hard code the urls for each month. no point in optimizing it.

    html = urllib.request.urlopen(url)

    soup = BeautifulSoup(html, features="html.parser")

    soup.findAll('tr', limit=2)

    headers = [th.getText() for th in soup.findAll('tr', limit=2)
               [0].findAll('th')]

    headers = headers[1:]

    rows = soup.findAll('tr')[1:]

    rest_data = [[td.getText() for td in rows[i].findAll('td')]
                 for i in range(len(rows))]

    data = pd.DataFrame(rest_data, columns=headers)

    writer = pd.ExcelWriter(month + '.xlsx', engine='xlsxwriter')

    data.to_excel(writer, sheet_name='Sheet1')

    writer.save()

    # Verifies that the correct operation is happening.
    print(data)

    return data

# Run the file
main()
