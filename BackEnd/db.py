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

def print_users():
    db = get_db()
    users = db["Users"].find()
    for user in users:
        print(user)

def print_notebooks():
    db = get_db()
    notebooks = db["NoteBooks"].find()
    for notebook in notebooks:
        print(notebook)

def print_notes():
    db = get_db()
    notes = db["Notes"].find()
    for note in notes:
        print(note)

def print_recommenders():
    db = get_db()
    recommender = db["Recommender"].find()
    for r in recommender:
        print(r)

