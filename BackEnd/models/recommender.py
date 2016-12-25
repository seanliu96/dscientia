#!/usr/bin/env python3
#-*- coding:utf-8 -*-

import numpy as np
import re
from db import get_db

keywords = []
userMap = {}

class Recommender(object):
    def __init__(self, fileName):
        self.train_data_matrix = []
        self.keywords = self.readKeywords(fileName)
        self.userMap = {}

    def readKeywords(self, fileName):
        with open(fileName, "r") as f:
            while True:
                line  = f.readline()
                if not line:
                    break
                keywords.append(line.strip('\n'))
        return keywords

    def findKeywordsVector(self, title, body):
        keywordsVector = [0.0] * len(self.keywords)
        for i in range(len(self.keywords)):
            if title.find(self.keywords[i]) != -1:
                keywordsVector[i] = 1.0
            elif body.find(self.keywords[i]) != -1:
                keywordsVector[i] = 1.0
        return keywordsVector

    def saveVector(self, uid, title, body):
        if not uid:
            return
        newKeywordsVector = self.findKeywordsVector(title, body)
        db = get_db()
        doc = db["Recommender"].find_one({"uid": uid})
        if doc:
            oldKeywordsVector = doc["keywordsVector"]
            if(len(oldKeywordsVector)) == len(newKeywordsVector):
                for i in range(len(newKeywordsVector)):
                    if(oldKeywordsVector[i]):
                        newKeywordsVector[i] = 1.0
            db["Recommender"].update(
                {"uid": uid},
                {"uid": uid, "keywordsVector": newKeywordsVector}
            )
        else:
            db["Recommender"].insert(
                {"uid": uid, "keywordsVector": newKeywordsVector}
            )

    def generateMatrix(self):
        db = get_db()
        docs = db["Recommender"].find()
        self.train_data_matrix = []
        self.userMap = {}
        index = 0
        for doc in docs:
            self.userMap[doc["uid"]] = index
            index += 1
            self.train_data_matrix.append(doc["keywordsVector"])
        self.train_data_matrix = np.array(self.train_data_matrix)

    def computeSimilarity(self):
        from sklearn.metrics.pairwise import pairwise_distances
        self.user_similarity = pairwise_distances(self.train_data_matrix, metric='cosine')

    def generatePrediction(self):
        from scipy.sparse.linalg import svds
        print(type(self.train_data_matrix[0][0]))
        print(self.train_data_matrix)
        u, s, vt = svds(self.train_data_matrix, k=10)
        self.user_prediction = np.dot(np.dot(u, np.diag(s)), vt)

    def recommend(self, uid):
        recommendList = []
        index = self.userMap[uid]
        for i in range(3):
            max_index = -1
            for j in range(len(self.user_prediction[index])):
                if self.train_data_matrix[index][j] == 0:
                    if j not in recommendList:
                        if max_index == -1 or self.user_prediction[index][j] < self.user_prediction[index][max_index]:
                            max_index = j
            recommendList.append(max_index)
        recommendList = sorted(recommendList)
        for i in range(len(recommendList)):
            recommendList[i] = keywords[recommendList[i]]
        return recommendList

    def reset(self):
        db = get_db()
        db["Recommender"].remove({})

if __name__ == '__main__':
    r = Recommender()
    r.generateMatrix()
    r.computeSimilarity()
    r.generatePrediction()
    print(r.recommend(3))
