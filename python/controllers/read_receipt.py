from flask import Blueprint, request, jsonify
from utils.read_image import extract_total_expense
from utils.read_pdf import get_receipt_data
from utils.read_image_receipt import read_receipt_jpg  
from utils.converters import convert_image_to_pdf
from datetime import datetime
from utils.receipt_data_create_pdf import create_receipt_data_pdf
from utils.receipt_data_create_pdf import create_receipt_data_pdf
import os
from werkzeug.utils import secure_filename



# Define Blueprints
receipt_read = Blueprint('receipt_read', __name__)
receipt_read_pdf = Blueprint('receipt_read_pdf', __name__)
receipt_read_jpg = Blueprint('receipt_read_jpg', __name__)
save_receipt_bp = Blueprint('save_receipt_data', __name__)

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




@save_receipt_bp.route('/api/py/save_receipt_data', methods=['POST'])
def save_receipt_data():
    data = request.get_json()
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    file_name = data.get('file_name')
    receipt_data = data.get('receipt_data')
    
    for item in receipt_data:
        item.pop('expenseItemId', None)

    print("🧾 Receipt data received:", receipt_data)

    if not file_name or not receipt_data:
        return jsonify({'error': 'file_name and receipt_data are required'}), 400   
    
    # Define upload folder and file path
    BASE_DIR = os.path.abspath(os.path.dirname(__file__))
    UPLOAD_FOLDER = os.path.join(BASE_DIR, '..', 'uploads')
    os.makedirs(UPLOAD_FOLDER, exist_ok=True)
    file_path = os.path.join(UPLOAD_FOLDER, file_name)

    # Check if file exists
    if not os.path.exists(file_path):
        return jsonify({'error': f'File {file_name} not found on server'}), 404

    # Output PDF path in same folder
    base_name, _ = os.path.splitext(file_name)
    output_pdf_path = os.path.join(UPLOAD_FOLDER, f"{base_name}_data.pdf")
    
        # checking for file types
    if file_path.lower().endswith(('.png', '.jpg', '.jpeg')):
        convert_image_to_pdf(file_path, base_name)
        create_receipt_data_pdf(receipt_data, output_pdf_path)
    elif file_path.lower().endswith('.pdf'):
        create_receipt_data_pdf(receipt_data, output_pdf_path)
    else:
        raise ValueError("Unsupported file format")

    # Now call with full path
    

    print(f"📄 PDF created at: {output_pdf_path}")
    return jsonify({
        "message": f"Receipt {file_name} saved successfully.",
        "pdf_path": output_pdf_path
    })



    
    

   

