from urllib import request, parse
from consts import *
from datetime import datetime

def register():
    name = input("name")
    pwd = input("pwd")
    data = {
        "name": name,
        "pwd": pwd,
    }
    data = parse.urlencode(data)
    req = request.Request(REGISTER_API, data.encode('utf-8'))
    resp = request.urlopen(req)
    print(resp.read().decode("utf-8"))

def login():
    name = input("name")
    pwd = input("pwd")
    data = {
        "name": name,
        "pwd": pwd,
    }
    data = parse.urlencode(data)
    req = request.Request(LOGIN_API, data.encode('utf-8'))
    resp = request.urlopen(req)
    print(resp.read().decode("utf-8"))

def addNotebook():
    uid = input("uid")
    name = input("name")
    data = {
        "uid": uid,
        "name": name,
    }
    data = parse.urlencode(data)
    req = request.Request(USER_ADDNOTEBOOK_API, data.encode('utf-8'))
    resp = request.urlopen(req)
    print(resp.read().decode("utf-8"))

def addNote():
    uid = input("uid")
    nbid = input("nbid")
    title = input("title")
    time = datetime.now()
    weekday = datetime.weekday(time) + 1
    body = input("body")
    data = {
        "uid": uid,
        "nbid": nbid,
        "title": title,
        "time": time,
        "weekday": weekday,
        "body": body,
    }
    data = parse.urlencode(data)
    req = request.Request(NOTEBOOK_ADDNOTE_API, data.encode('utf-8'))
    resp = request.urlopen(req)
    print(resp.read().decode("utf-8"))


def getNotebook():
    uid = input("uid")
    nbid = input("nbid")
    data = {
        "uid": uid,
        "nbid": nbid,
    }
    data = parse.urlencode(data)
    req = request.Request(NOTEBOOK_GETINFO_API, data.encode('utf-8'))
    resp = request.urlopen(req)
    print(resp.read().decode("utf-8"))

def getNote():
    uid = input("uid")
    nbid = input("nbid")
    nid = input("nid")
    data = {
        "uid": uid,
        "nbid": nbid,
        "nid": nid,
    }
    data = parse.urlencode(data)
    req = request.Request(NOTE_GETINFO_API, data.encode('utf-8'))
    resp = request.urlopen(req)
    print(resp.read().decode("utf-8"))