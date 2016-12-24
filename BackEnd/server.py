#!/usr/bin/env python3
#*_* coding:utf-8 *_*

import os
import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
from bson.json_util import dumps
from tornado.options import define, options
from models.user import User
from models.notebook import NoteBook
from models.note import Note
from models.recommender import Recommender


define('port', default=8000, help='run on the given port', type=int)

KEYWORDSFILE = os.path.join(os.path.dirname(__file__), 'data', 'keywords')

recommender = Recommender(KEYWORDSFILE)

class BaseHandler(tornado.web.RequestHandler):

    def __init(self, application, request, **kwargs):
        super(BaseHandler, self).__init__(application, request, **kwargs)

    def data_received(self, chunk):
        super(BaseHandler, self).data_received(chunk)

    def make_result(self, code, msg, data):
        self.set_header("Content-type", "application/json")
        rst = {
            "code": code,
            "msg": msg,
        }
        if data:
            rst["data"] = data
        return dumps(rst)

class IndexHandler(BaseHandler):
    def get(self, *args, **kwargs):
        self.render("index.html")

class LoginHandler(BaseHandler):
    def post(self, *args, **kwargs):
        name = self.get_argument("name", None)
        pwd = self.get_argument("pwd", None)
        user_doc = User.authenticate(name, pwd)
        if user_doc:
            self.write(self.make_result(1, "login OK", user_doc))
        else:
            self.write(self.make_result(0, "user not found or wrong password", None))

class RegisterHandler(BaseHandler):
    def post(self, *args, **kwargs):
        new_user_kw = {
            "name": self.get_argument("name", None),
            "pwd": self.get_argument("pwd", None),
            "avatar": self.request.files["avatar"][0]["body"] if "avatar" in self.request.files else None,
        }
        if User.is_name_exist(new_user_kw["name"]):
            self.write(self.make_result(0, "name already exists", None))
            return
        try:
            new_user = User(**new_user_kw)
            new_user.save()
            self.write(self.make_result(1, "register OK", None))
        except ValueError as e:
            self.write(self.make_result(0, str(e), None))
        except IOError as e:
            self.write(self.make_result(0, str(e), None))

class UserUpdateHandler(BaseHandler):
    def post(self, *args, **kwargs):
        user_kw = {
            "uid": self.get_argument("uid", None),
            "pwd": self.get_argument("pwd", None),
        }
        print(user_kw)
        try:
            user = User.get(user_kw["uid"])
            if user:
                if user_kw["pwd"]:
                    user.pwd = user_kw["pwd"]
                user.save()
                self.write(self.make_result(1, "user update OK", None))
            else:
                self.write(self.make_result(0, "user not found or wrong password", None))
        except ValueError as e:
            self.write(self.make_result(0, str(e), None))
        except IOError as e:
            self.write(self.make_result(0, str(e), None))

class UserUploadHandler(BaseHandler):
    def post(self, *args, **kwargs):
        user_kw = {
            "uid": self.get_argument("uid", None),
            "avatar": self.get_argument("avatar", None),
        }
        try:
            user = User.get(user_kw["uid"])
            if user:
                if user_kw["avatar"]:
                    user.avatar_url = user_kw["avatar"]
                user.save()
                self.write(self.make_result(1, "user upload OK", {"avatar_url": user.avatar_url}))
            else:
                self.write(self.make_result(0, "user not found or wrong password", None))
        except ValueError as e:
            self.write(self.make_result(0, str(e), None))
        except IOError as e:
            self.write(self.make_result(0, str(e), None))

class UserResetHandler(BaseHandler):
    def post(self, *args, **kwargs):
        User.reset()
        self.write(self.make_result(1, "user reset OK", None))


class UserAddNoteBookHandler(BaseHandler):
    def post(self, *args, **kwargs):
        uid = self.get_argument("uid", None)
        new_notebook_kw = {
            "name": self.get_argument("name", None),
            "cover": self.get_argument("cover", None),
        }
        try:
            user = User.get(uid)
            if user is None:
                self.write(self.make_result(0, "user not found", None))
                return
            else:
                new_notebook = NoteBook(**new_notebook_kw)
                new_notebook.save()
                User.add_notebook(uid, new_notebook.nbid)
                self.write(self.make_result(1, "notebook add OK"), None)
        except ValueError as e:
            self.write(self.make_result(0, str(e), None))
        except IOError as e:
            self.write(self.make_result(0, str(e), None))


class NoteBookUpdateHandler(BaseHandler):
    def post(self, *args, **kwargs):
        notebook_kw = {
            "nbid": self.get_argument("nbid", None),
            "name": self.get_argument("name", None),
        }
        try:
            notebook = NoteBook.get(notebook_kw["nbid"])
            if notebook:
                if notebook_kw["name"]:
                    notebook.name = notebook_kw["name"]
                notebook.save()
                self.write(self.make_result(1, "notebook update OK", None))
            else:
                self.write(self.make_result(0, "notebook not found"))
        except ValueError as e:
            self.write(self.make_result(0, str(e), None))
        except IOError as e:
            self.write(self.make_result(0, str(e), None))

class NoteBookUploadHandler(BaseHandler):
    def post(self, *args, **kwargs):
        notebook_kw = {
            "nbid": self.get_argument("nbid", None),
            "cover": self.get_argument("cover", None),
        }
        try:
            notebook = NoteBook.get(notebook_kw["nbid"])
            if notebook:
                if notebook_kw["cover"]:
                    notebook.cover_url = notebook_kw["cover"]
                notebook.save()
                self.write(self.make_result(1, "notebook upload OK", None))
            else:
                self.write(self.make_result(0, "notebook not found", None))
        except ValueError as e:
            self.write(self.make_result(0, str(e), None))
        except IOError as e:
            self.write(self.make_result(0, str(e), None))

class NoteBookAddNoteHandler(BaseHandler):
    def post(self, *args, **kwargs):
        nbid = self.get_argument("nbid", None)
        new_note_kw = {
            "title": self.get_argument("title", None),
            "time": self.get_argument("time", None),
            "weekday": self.get_argument("weekday", None),
            "body": self.get_argument("body", None),
        }
        try:
            notebook = NoteBook.get(nbid)
            if notebook is None:
                self.write(swle.make_result(0, "notebook not found", None))
                return
            else:
                new_note = Note(**new_note_kw)
                new_note.save()
                NoteBook.add_note(nbid, new_notebook.nid)
                uid = self.get_argument("uid", None)
                recommender.saveVector(uid, new_note_kw["title"], new_note_kw["body"])
                recommender.generateMatrix()
                recommender.computeSimilarity()
                recommender.generatePrediction()
                recommendList = recommender.recommend(uid)
                self.write(self.make_result(1), "note add OK", recommendList)
        except ValueError as e:
            self.write(self.make_result(0, str(e), None))
        except IOError as e:
            self.write(self.make_result(0, str(e), None))


class NoteUpdateHandler(BaseHandler):
    def post(self, *args, **kwargs):
        note_kw = {
            "nid": self.get_argument("nid", None),
            "title": self.get_argument("title" ,None),
            "time": self.get_argument("time", None),
            "weekday": self.get_argument("weekday", None),
            "body": self.get_argument("body" ,None),
        }
        try:
            note = Note.get(note_kw["nid"])
            if note:
                if note_kw["title"]:
                    note.title = note_kw["title"]
                if note_kw["time"]:
                    note.time = note_kw["time"]
                if note_kw["weekday"]:
                    note.weekday = note_kw["weekday"]
                note.body = note_kw["body"]
                note.save()
                uid = self.get_argument("uid", None)
                recommender.saveVector(uid, new_note_kw["title"], new_note_kw["body"])
                recommender.generateMatrix()
                recommender.computeSimilarity()
                recommender.generatePrediction()
                recommendList = recommender.recommend(uid)
                self.write(self.make_result(1, "note update OK", recommendList))
            else:
                self.write(self.make_result(0, "note not found"))
        except ValueError as e:
            self.write(self.make_result(0, str(e), None))
        except IOError as e:
            self.write(self.make_result(0, str(e), None))


if __name__ == "__main__":
    tornado.options.parse_command_line()
    application = tornado.web.Application(
        handlers=[
            (r'/', IndexHandler),
            (r'/api/login', LoginHandler),
            (r'/api/register', RegisterHandler),
            (r'/api/user/update', UserUpdateHandler),
            (r'/api/user/upload', UserUploadHandler),
            (r'/api/user/reset', UserResetHandler),
            (r'/api/user/addnotebook', UserAddNoteBookHandler),
            (r'/api/notebook/update', NoteBookUpdateHandler),
            (r'/api/notebook/upload', NoteBookUploadHandler),
            (r'/api/notebook/addnote', NoteBookAddNoteHandler),
            (r'/api/note/update', NoteUpdateHandler),
        ],
        template_path=os.path.join(os.path.dirname(__file__), 'templates'),
        static_path=os.path.join(os.path.dirname(__file__), 'static'),
        login_url='/login'
    )
    http_server = tornado.httpserver.HTTPServer(application)
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.current().start()