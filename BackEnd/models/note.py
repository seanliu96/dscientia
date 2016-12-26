#!/usr/bin/env python3
#*_* coding:utf-8 *_*

import os
import re
from bson.objectid import ObjectId
from consts import *
from db import get_db

IMG_DIR = os.path.join(os.path.dirname(__file__), os.pardir, "static", "img", "note")

class Note(object):

    def __init__(self, title, time, weekday, body, nid=None, **kwargs):
        self._nid = None
        self._title = None
        self._time = None
        self._weekday = None
        self._body = None
        self.title = title
        self.time = time
        self.weekday = weekday
        self.body = body
        if nid:
            self.nid = nid

    def save(self):
        db = get_db()
        if self.nid:
            db["Notes"].update(
                {
                    "_id": ObjectId(self.nid),
                },
                {
                    "title": self.title,
                    "time": self.time,
                    "weekday": self.weekday,
                    "body": self.body,
                },
            )
        else:
            nid_obj = db["Notes"].insert(
                {
                    "title": self.title,
                    "time": self.time,
                    "weekday": self.weekday,
                    "body": self.body,
                }
            )
            self.nid = str(nid_obj)

    @property
    def nid(self):
        return self._nid

    @nid.setter
    def nid(self, value):
        if value:
            self._nid = value
        else:
            raise ValueError("You did not provide valid nid")

    @property
    def title(self):
        return self._title

    @title.setter
    def title(self, value):
        if value:
            self._title = value
        else:
            raise ValueError("You did not provide valid title")

    @property
    def time(self):
        return self._time

    @time.setter
    def time(self, value):
        if value:
            self._time = value
        else:
            raise ValueError("You did not provide valid time")

    @property
    def weekday(self):
        return self._weekday

    @weekday.setter
    def weekday(self, value):
        if value:
            self._weekday = value
        else:
            raise ValueError("You did not provide valid weekday")

    @property
    def body(self):
        return self._body

    @body.setter
    def body(self, value):
        if value:
            self._body = value
        else:
            raise ValueError("You did not provide valid body")

    @classmethod
    def get(cls, nid):
        db = get_db()
        doc = db["Notes"].find_one({"_id": ObjectId(nid)})
        if doc:
            print(doc)
            n = cls(**doc)
            n.nid = nid
            return n
        else:
            return None

    @classmethod
    def getdoc(cls, nid):
        db = get_db()
        doc = db["Notes"].find_one({"_id": ObjectId(nid)})
        if doc:
            return doc
        else:
            return None

    @classmethod
    def reset(cls):
        db = get_db()
        db["Notes"].remove({})
    