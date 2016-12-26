#!/usr/bin/env python3
#*_* coding:utf-8 *_*

import base64
import os
import re
from datetime import datetime
from bson.objectid import ObjectId
from consts import *
from db import get_db

IMG_DIR = os.path.join(os.path.dirname(__file__), os.pardir, "static", "img", "notebook")

class NoteBook(object):
    def __init__(self, name, cover=None, nbid=None, **kwargs):
        self._nbid = None
        self._name = None
        self._cover_url = SERVER_ROOT_URL + "/static/img/notebook/default_cover.png"
        self.name = name
        if cover:
            self.cover_url = cover
        if nbid:
            self.nbid = nbid

    def save(self):
        db = get_db()
        if self.nbid:
            db["NoteBooks"].update(
                {
                    "_id": ObjectId(self.nbid),
                },
                {
                    "name": self.name,
                    "cover_url": self.cover_url,
                    "notes": self.notes,
                    "datetime": datetime.now(),
                },
            )
        else:
            nbid_obj = db["NoteBooks"].insert(
                {
                    "name": self.name,
                    "cover_url": self.cover_url,
                    "notes": [],
                    "datetime": datetime.now(),
                }
            )
            self.nbid = str(nbid_obj)

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, value):
        if value:
            self._name = value
        else:
            raise ValueError("You did not provide valid name")

    @property
    def nbid(self):
        return self._nbid

    @nbid.setter
    def nbid(self, value):
        if value:
            self._nbid = value
        else:
            raise ValueError("You did not provide valid nbid")

    

    @property
    def cover_url(self):
        return self._cover_url

    @cover_url.setter
    def cover_url(self, value):
        if value:
            value = base64.b64decode(value)
            now = re.sub(r'[ :.]', '-', str(datetime.now()))
            path = os.path.join(IMG_DIR, str(self.name) + "_cover" + now + ".png")
            cover_file = open(path, 'wb')
            cover_file.write(value)
            self._cover_url = SERVER_ROOT_URL + '/static/' + '/'.join(path.split('/')[-3:])
            cover_file.close()
        else:
            raise ValueError("You did not provide valid cover")

    @classmethod
    def get(cls, nbid):
        db = get_db()
        doc = db["NoteBooks"].find_one({"_id": ObjectId(nbid)})
        if doc:
            print(doc)
            nb = cls(**doc)
            nb.nbid = nbid
            nb._cover_url = doc["cover_url"]
            return nb
        else:
            return None

    @classmethod
    def getdoc(cls, nbid):
        db = get_db()
        doc = db["NoteBooks"].find_one({"_id": ObjectId(nbid)})
        if doc:
            return doc
        else:
            return None

    @classmethod
    def reset(cls):
        db = get_db()
        db["NoteBooks"].remove({})

    @classmethod
    def add_note(cls, nbid, nid):
        db = get_db()
        db["NoteBooks"].update(
            {"_id": ObjectId(nbid)},
            {"$addToSet": {"notes": nid}},
        )

    @classmethod
    def remove_note(cls, nbid, nid):
        db = get_db()
        db["NoteBooks"].update(
            {"_id": ObjectId(nbid)},
            {"$pull": {"notes": nid}},
        )
    