"""
This file handles the top level stuff for the set up of the algorithm.

As of right now:
The nba schedule is full set.
"""

from schedule import Schedule

def set_schedule():
    league_schedule = Schedule()
    league_schedule.set_league_schedules()
    return league_schedule

schedge = set_schedule()
