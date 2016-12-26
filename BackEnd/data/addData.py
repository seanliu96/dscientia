#!/usr/bin/env python3
#coding:utf-8
import os
import random
from recommender import Recommender

def InsertData(n=1000):
    recommender = Recommender("keywords")
    keywords  = []
    with open("keywords", "r") as f:
        while True:
            line = f.readline()
            if not line:
                break
            keywords.append(line.strip('\n'))
    l = len(keywords)
    for i in range(n):
        index = random.randint(0, l-1)
        title = ""
        body = ""
        for j in range(10):
            if random.random() < 0.5:
                k = random.randint(index-10, index+10)
                k = min(l-1, k)
                k = max(0, k)
                body += (keywords[k] + "\n")
        for j in range(-3,3):
            k = min(l-1, index+j)
            k = max(0, k)
            body += (keywords[k] + "\n")
        recommender.saveVector(str(i), title, body)
    recommender.generateMatrix()
    print(recommender.train_data_matrix)

if __name__ == '__main__':
    InsertData()
