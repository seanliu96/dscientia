文件目录：
├── __pycache__（python自动生成）
│   ├── consts.cpython-35.pyc（自动生成）
│   └── db.cpython-35.pyc（自动生成）
├── consts.py（一些常量的定义）
├── db.py（连接数据库）
├── models
│   ├── __pycache__（自动生成）
│   │   ├── note.cpython-35.pyc（自动生成）
│   │   ├── notebook.cpython-35.pyc（自动生成）
│   │   └── user.cpython-35.pyc（自动生成）
│   ├── note.py（笔记）
│   ├── notebook.py（笔记本）
│   └── user.py（用户）
├── server.py（主文件）
├── static（静态文件夹）
└── templates（模板文件夹）




user注册(r'/api/register', RegisterHandler),
    需要name, pwd。(name不允许在数据库重复,后端检查)
    成功返回1，失败返回0
    参照许泳哲师兄的注册代码，所以理论上安卓端应该不需要改动太多，只需要在需要填写的信息那里注意一下

user登陆(r'/api/login', LoginHandler),
    需要name,pwd
    成功返回1和用户信息，失败返回0

user主页(r'/', IndexHandler),
    需要name,pwd
    成功返回1和用户信息，失败返回0

user更新(r'/api/user/update', UserUpdateHandler),
    更新密码，需要uid，pwd。其中uid包含在登录成功返回的用户信息中
    成功返回1，失败返回0

user上传(r'/api/user/upload', UserUploadHandler),
    上传头像，需要uid, avatar。
    成功返回1和avatar在服务器中的位置，失败返回0

user重置(r'/api/user/reset', UserResetHandler),
    删除所有用户
    成功返回1，失败返回0

user添加notebook(r'/api/user/addnotebook', UserAddNoteBookHandler),
    添加笔记本，需要uid， 笔记本name， 笔记本封面cover（这个可以省略，使用默认）
    成功返回1，失败返回0


notebook更新(r'/api/notebook/update', NoteBookUpdateHandler),
    更新笔记本，需要nbid，name，其中nbid包含在笔记本信息中
    成功返回1，失败返回0

notebook上传(r'/api/notebook/upload', NoteBookUploadHandler),
    上传封面，需要cover
    成功返回1，失败返回0

notebook添加note(r'/api/notebook/addnote', NoteBookAddNoteHandler),
    添加笔记，需要uid, nbid，title，time, weekday, body
    成功返回1，失败返回0

note更新(r'/api/note/update', NoteUpdateHandler),
    更新笔记，需要uid, nid，title，time, weekday, body
    成功返回1，失败返回0



