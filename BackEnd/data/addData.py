#!/usr/bin/env python3
#coding:utf-8

from db import get_db
import os.path
from recommender import Recommender

def InsertData():
    recommender = Recommender("keywords")
    uid = 0
    for file in os.listdir("test"):
        if(file[0] == '.'):
            continue
        print(file)
        title = ""
        body = ""
        db = get_db()
        with open("test/" + file, "r") as f:
            lines = f.readlines()
            for line in lines:
                body += line + "\n"
        recommender.saveVector(uid, title, body)
        uid += 1
    recommender.generateMatrix()
    print(recommender.train_data_matrix)

if __name__ == '__main__':
    InsertData()


