from flask import Blueprint, request, jsonify
from utils.read_image import extract_total_expense
from utils.read_pdf import get_receipt_data
from utils.read_image_receipt import read_receipt_jpg  # Make sure function name matches
from datetime import datetime
import os
from werkzeug.utils import secure_filename
# Define Blueprints
receipt_read = Blueprint('receipt_read', __name__)
receipt_read_pdf = Blueprint('receipt_read_pdf', __name__)
receipt_read_jpg = Blueprint('receipt_read_jpg', __name__)

UPLOAD_FOLDER = "uploads"
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

# --- Routes ---
#the following API provides only the total amount from the jpg receipt

@receipt_read.route('/api/py/read_receipt', methods=['GET'])
def get_receipt_information():
    read_receipt = extract_total_expense()
    if read_receipt:
        total_amount = read_receipt.group(1)
        return jsonify({'total_amount': total_amount}), 200
    else:
        return jsonify({'error': 'Total amount not found in the receipt.'}), 404

# the following API provides all the details of the receipt that is in pdf format
@receipt_read_pdf.route('/api/py/read_receipt_pdf', methods=['POST'])
def get_receipt_information_pdf():
    
    if 'file' not in request.files:
        return jsonify({'error': 'No file part in the request'}), 400
    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400
    
        # Define your upload folder
    UPLOAD_FOLDER = 'uploads'
    os.makedirs(UPLOAD_FOLDER, exist_ok=True)

    # Save file to uploads directory (same filename)
    filepath = os.path.join(UPLOAD_FOLDER, file.filename)
    file.save(filepath)
    
    receipt_json = get_receipt_data(filepath)
    
    
    return jsonify({'receipt_data': receipt_json}), 200

# the following API provides all the details of the receipt that is in jpg format
@receipt_read_jpg.route('/api/py/read_receipt_jpg', methods=['POST'])
def get_receipt_information_jpg():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part in the request'}), 400
    
    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400

    # Define your upload folder
    UPLOAD_FOLDER = 'uploads'
    os.makedirs(UPLOAD_FOLDER, exist_ok=True)

    # Save file to uploads directory (same filename)
    filepath = os.path.join(UPLOAD_FOLDER, file.filename)
    file.save(filepath)

    # Pass the saved file path to your receipt reader
    receipt_details = read_receipt_jpg(filepath)

    return jsonify({'receipt_data': receipt_details}), 200

