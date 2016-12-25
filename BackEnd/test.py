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
    print(resp.read())

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
    print(resp.read())

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
    print(resp.read())

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
        "title": name,
        "time": time,
        "weekday": weekday,
        "body": body,
    }
    data = parse.urlencode(data)
    req = request.Request(NOTEBOOK_ADDNOTE, data.encode('utf-8'))
    resp = request.urlopen(req)
    print(resp.read())
