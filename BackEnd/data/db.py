import pymongo


def get_db():
    client = pymongo.MongoClient()
    # client["test"].authenticate("jj", "2333")
    db = client["Dscientia"]
    return db
