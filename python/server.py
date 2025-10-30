from flask import Flask
from controllers.read_receipt import receipt_read, receipt_read_pdf, receipt_read_jpg

app = Flask(__name__)

# Register Blueprints
app.register_blueprint(receipt_read)
app.register_blueprint(receipt_read_pdf)
app.register_blueprint(receipt_read_jpg)

if __name__ == '__main__':
    app.run(debug=True, port=5001)
