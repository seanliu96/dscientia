#!/usr/bin/env python3
# *_* coding:utf-8 *_*

SERVER_IP = "172.18.71.35"
SERVER_PORT = "8080"
SERVER_ROOT_URL = "http://" + SERVER_IP + ":" + SERVER_PORT

INDEX_API = SERVER_ROOT_URL + "/"
LOGIN_API = SERVER_ROOT_URL + "/api/login"
REGISTER_API = SERVER_ROOT_URL + "/api/register"
USER_UPDATE_API = SERVER_ROOT_URL + "/api/user/update"
USER_UPLOAD_API = SERVER_ROOT_URL + "/api/user/upload"
USER_RESET_API = SERVER_ROOT_URL + "/api/user/reset"
USER_ADDNOTEBOOK_API = SERVER_ROOT_URL + "/api/user/addnotebook"
NOTEBOOK_UPDATE_API = SERVER_ROOT_URL + "/api/notebook/update"
NOTEBOOK_UPLOAD_API = SERVER_ROOT_URL + "/api/notebook/upload"
NOTEBOOK_ADDNOTE_API = SERVER_ROOT_URL + "/api/notebook/addnote"
NOTE_UPDATE_API = SERVER_ROOT_URL + "/api/note/update"
