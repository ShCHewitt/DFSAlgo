# Shane Hewitt's DFS NBA Algorithm from Scratch

- The following project is developed entirely by Shane Hewitt. The code progressively works toward outputting an optimal lineup for any given night based on 
a set of desires and a date for NBA daily fantasy sports games. Ideally, this will work for both Fanduel and Draftkings.

- The flow of the project is divided into 3 sections.

## Data Scraping

### Schedule Setting
- Completed for the 2018-2019 NBA season. Easily changed for any following or previous season.

- This is done by scraping the monthly nba schedule from October until April, there are 1230 regular season games played in the NBA regular
season, so we cut off in the month of april once we hit that date as there are playoff games in April as well which we do not want to include.

- The flow of this is that we translate the table on the basketball-reference website to an excel document and then we use a helper function to convert the file
from .xlsx to .csv so we can manipulate and read the file easier across coding languages if necessary.

- Once we have the monthly schedule csvs, we create a class Game and make a seperate instance for each row in the month.csv files. Then we add this to a list of Games.

- We initialize a dictionary with the keys being the names of the teams in the NBA and the values being their schedules for the year in order. We iterate through all of the games in the full regular season list of Games and add the game to both the home and away team's schedules in the dictionary.


