from flask import Flask, jsonify
from flask_cors import CORS
from controllers.read_receipt import receipt_read
from controllers.read_receipt import receipt_read_pdf
from controllers.read_receipt import receipt_read_jpg

app = Flask(__name__)

# Configure CORS to allow requests from Angular dev server
CORS(app, resources={
    r"/api/*": {
        "origins": ["http://localhost:4200", "http://127.0.0.1:4200"],
        "methods": ["GET", "POST", "OPTIONS"],
        "allow_headers": ["Content-Type"]
    }
})

app.register_blueprint(receipt_read)
app.register_blueprint(receipt_read_pdf)
app.register_blueprint(receipt_read_jpg)

# Health check endpoint
@app.route('/')
def home():
    return jsonify({
        'status': 'running',
        'message': 'Receipt Processing API',
        'endpoints': [
            '/api/py/read_receipt (GET)',
            '/api/py/read_receipt_pdf (POST)',
            '/api/py/read_receipt_jpg (POST)'
        ]
    }), 200



if __name__ == '__main__':
    app.run(debug=True, port=5001, use_reloader=False)