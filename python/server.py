from flask import Flask
from datetime import datetime

app = Flask(__name__)

@app.route('/')
def home():
    return "Welcome to the Expense Management System!"



if __name__ == '__main__':
    app.run(debug=True, port=5001)