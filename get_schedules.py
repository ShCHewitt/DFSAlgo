import urllib
from bs4 import BeautifulSoup
import pandas as pd

import os, ssl
if (not os.environ.get('PYTHONHTTPSVERIFY', '') and
    getattr(ssl, '_create_unverified_context', None)):
    ssl._create_default_https_context = ssl._create_unverified_context

def get_site_data(url):
    print("getting schedules from the site.")

    html = urllib.request.urlopen(url)

    soup = BeautifulSoup(html, features="html.parser")

get_site_data("https://www.basketball-reference.com/leagues/NBA_2019_games-october.html")