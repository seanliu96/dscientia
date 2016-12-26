#!/usr/bin/env python3
#coding:utf-8
import random

def generateData(n=1000):
    keywords = []
    with open("keywords", "r") as f:
        while True:
            line = f.readline()
            if not line:
                break
            keywords.append(line.strip('\n'))
    l = len(keywords)
    for i in range(n):
        with open("test/" + str(i), "w") as f:
            index = random.randint(0, l)
            for j in range(20):
                while random.random() < 0.5:
                    k = random.randint(index-10, index+10)
                    k = min(l-1, k)
                    k = max(0, k)
                    f.write(keywords[k] + "\n")

if __name__ == "__main__":
    generateData()


    

