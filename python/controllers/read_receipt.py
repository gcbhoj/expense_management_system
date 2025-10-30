from flask import Blueprint, request, jsonify

from  utils.read_image import extract_total_expense
from  utils.read_pdf import get_receipt_data

receipt_read = Blueprint('receipt_read', __name__)
receipt_read_pdf = Blueprint('receipt_read_pdf', __name__)

@receipt_read.route('/api/py/read_receipt', methods=['GET'])
def get_receipt_information():
    total_amount =0.0;
    
    read_receipt = extract_total_expense();
    if read_receipt:
        total_amount = read_receipt.group(1)
        return jsonify({'total_amount': total_amount}), 200
    else:
        return jsonify({'error': 'Total amount not found in the receipt.'}), 404
    
    
@receipt_read_pdf.route('/api/py/read_receipt_pdf', methods=['GET'])
def get_receipt_information_pdf():
    receipt_json = get_receipt_data()
    return jsonify({'receipt_data': receipt_json}), 200
    
    
    
        
    
 