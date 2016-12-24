import pymongo


def get_db():
    client = pymongo.MongoClient()
    # client["test"].authenticate("jj", "2333")
    db = client["Dscientia"]
    return db

def remove_db():
    db = get_db()
    db["Users"].remove({})
    db["NoteBooks"].remove({})
    db["Notes"].remove({})
    db["Recommender"].remove({})
