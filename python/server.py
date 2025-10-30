from flask import Flask
from controllers.read_receipt import receipt_read
app = Flask(__name__)

app.register_blueprint(receipt_read)



if __name__ == '__main__':
    app.run(debug=True, port=5001)