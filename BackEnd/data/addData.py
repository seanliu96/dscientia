#!/usr/bin/env python3
#coding:utf-8
import os
from recommender import Recommender

def InsertData():
    recommender = Recommender("keywords")
    for file in os.listdir("test"):
        if(file[0] == '.'):
            continue
        title = ""
        body = ""
        with open("test/" + file, "r") as f:
            lines = f.readlines()
            for line in lines:
                body += line + "\n"
        recommender.saveVector(file, title, body)
    recommender.generateMatrix()
    print(recommender.train_data_matrix)

if __name__ == '__main__':
    InsertData()


