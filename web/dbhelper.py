import os
import database
import appdirs
from utils import *


class DBHelper(object):
    """Database helper class"""
    TABLE_SUMMARY = "summary"
    TABLE_EVENT = "event"
    TABLE_PEOPLE = "people"
    TABLE_EVENT_PEOPLE = "event_people"
    TABLE_MONEY = "money"
    TABLE_SUMMARY_SIG = "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT"
    TABLE_EVENT_SIG = "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, trip_id INTEGER NOT NULL, total_share INTEGER, total_spent INTEGER, FOREIGN KEY(trip_id) REFERENCES " + TABLE_SUMMARY + "(_id)"
    TABLE_EVENT_PEOPLE_SIG = "event_id INTEGER NOT NULL, people_id TEXT NOT NULL, FOREIGN KEY(event_id) REFERENCES " + TABLE_EVENT + "(_id)"
    TABLE_PEOPLE_SIG = "_id TEXT PRIMARY KEY, name TEXT"
    TABLE_MONEY_SIG = "_id INTEGER PRIMARY KEY AUTOINCREMENT, people_id TEXT NOT NULL, share INTEGER, spent INTEGER, event_id INTEGER NOT NULL, trip_id INTEGER NOT NULL, FOREIGN KEY(event_id) REFERENCES " + TABLE_EVENT + "(_id), FOREIGN KEY (trip_id) REFERENCES " + TABLE_SUMMARY + "(_id)"

    def __init__(self, filename='hisaab.db'):
        db_file_dir = (appdirs.user_data_dir("Hisaab", "Rajat", roaming=True))
        db_file = os.path.join(db_file_dir, filename)
        mkdir_p(db_file_dir)
        create_tables = not os.path.exists(db_file)
        self.connection = database.Connection(db_file)
        if create_tables:
            self.create_all_tables()

    def create_table(self, table_name, table_signature):
        self.connection.execute("create table %s (%s);" % (table_name, table_signature))

    def drop_table(table_name):
        self.connection.execute("drop table if exists %s;" % table_name)

    def create_all_tables(self):
        for x, y in [(self.TABLE_SUMMARY, self.TABLE_SUMMARY_SIG), (self.TABLE_EVENT, self.TABLE_EVENT_SIG),
            (self.TABLE_PEOPLE, self.TABLE_PEOPLE_SIG), (self.TABLE_EVENT_PEOPLE, self.TABLE_EVENT_PEOPLE_SIG),
            (self.TABLE_MONEY, self.TABLE_MONEY_SIG)]:
            self.create_table(x, y)

    def drop_all_tables(self):
        for x in [self.TABLE_SUMMARY, self.TABLE_EVENT, self.TABLE_MONEY, self.TABLE_PEOPLE, self.TABLE_EVENT_PEOPLE]:
            self.drop_table(x)
