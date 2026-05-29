
from dataclasses import dataclass

from flask import Flask, request, jsonify
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity, create_refresh_token
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.orm import Mapped, mapped_column
from werkzeug.security import generate_password_hash, check_password_hash

app = Flask(__name__)
db = SQLAlchemy()

app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///project.db"
db.init_app(app)

app.config["JWT_SECRET_KEY"] = "secret_key"
jwt = JWTManager(app)


@dataclass
class User(db.Model):
    __tablename__ = "users"

    id: Mapped[int] = mapped_column(primary_key=True)
    username: Mapped[str] = mapped_column(unique=True, nullable=False)
    password: Mapped[str] = mapped_column(nullable=False)


@dataclass
class TodoItem(db.Model):
    __tablename__ = "todos"

    id: Mapped[int] = mapped_column(primary_key=True)
    userId: Mapped[int] = mapped_column()
    text: Mapped[str] = mapped_column()


@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"


@app.route("/login", methods=["POST"])
def login():
    username = request.json.get("username", None)
    password = request.json.get("password", None)

    user = db.session.query(User).filter_by(username=username).first()
    if user is not None:
        if check_password_hash(user.password, password):
            access_token = create_access_token(identity=username)
            refresh_token = create_refresh_token(identity=username)
            return jsonify(access_token=access_token, refresh_token=refresh_token)
        else:
            return jsonify({"message": "Invalid username or password"}), 401
    else:
        return jsonify({"error": "Invalid username"}), 401

@app.route("/register", methods=["POST"])
def register():
    username = request.json.get("username", None)
    password = request.json.get("password", None)

    if db.session.query(User.id).filter_by(username=username).first() is not None:
        return jsonify({"message": "Username already exists"}), 400

    passwordHashed = generate_password_hash(password, method='pbkdf2:sha256')
    user = User(username=username, password=passwordHashed)
    db.session.add(user)
    db.session.commit()

    access_token = create_access_token(identity=username)
    refresh_token = create_refresh_token(identity=username)
    return jsonify(access_token=access_token, refresh_token=refresh_token)

@app.route("/protected", methods=["GET"])
@jwt_required()
def protected():
    # Access the identity of the current user with get_jwt_identity
    current_user = get_jwt_identity()
    return jsonify(logged_in_as=current_user), 200


@app.route("/refresh", methods=["POST"])
@jwt_required(refresh=True)
def refresh():
    identity = get_jwt_identity()
    access_token = create_access_token(identity=identity)
    refresh_token = create_refresh_token(identity=identity)
    return jsonify(access_token=access_token, refresh_token=refresh_token)


@app.route("/users")
def user_list():
    users = db.session.execute(db.select(User)).scalars().all()
    return jsonify(users)


@app.route("/items")
@jwt_required()
def item_list():
    # items = [{
    #     "id": 1,
    #     "text": "Item 1",
    #     "userId": 1,
    # }, {
    #     "id": 2,
    #     "text": "Item 3 test",
    #     "userId": 1,
    # }, {
    #     "id": 3,
    #     "text": "Item 3 asdasd",
    #     "userId": 1,
    # }, ]
    items = db.session.execute(db.select(TodoItem)).scalars().all()
    return jsonify(items)


@app.route("/items/add", methods=["POST"])
@jwt_required()
def add_todo():
    text = request.json["text"]
    todoItem = TodoItem(text=text, userId=1)
    db.session.add(todoItem)
    db.session.commit()
    return "Success"


if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    # app.run(host='localhost', port=8080, debug=True)
    app.run(host='0.0.0.0', port=8080, debug=True)
