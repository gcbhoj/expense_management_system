from flask import Blueprint, request, jsonify
from utils.read_image import extract_total_expense
from utils.read_pdf import get_receipt_data
from utils.read_image_receipt import read_receipt_jpg  # Make sure function name matches

# Define Blueprints
receipt_read = Blueprint('receipt_read', __name__)
receipt_read_pdf = Blueprint('receipt_read_pdf', __name__)
receipt_read_jpg = Blueprint('receipt_read_jpg', __name__)

# --- Routes ---
@receipt_read.route('/api/py/read_receipt', methods=['GET'])
def get_receipt_information():
    read_receipt = extract_total_expense()
    if read_receipt:
        total_amount = read_receipt.group(1)
        return jsonify({'total_amount': total_amount}), 200
    else:
        return jsonify({'error': 'Total amount not found in the receipt.'}), 404

@receipt_read_pdf.route('/api/py/read_receipt_pdf', methods=['GET'])
def get_receipt_information_pdf():
    receipt_json = get_receipt_data()
    return jsonify({'receipt_data': receipt_json}), 200

@receipt_read_jpg.route('/api/py/read_receipt_jpg', methods=['GET'])
def get_receipt_information_jpg():
    receipt_details = read_receipt_jpg()
    return jsonify({'receipt_data': receipt_details}), 200
