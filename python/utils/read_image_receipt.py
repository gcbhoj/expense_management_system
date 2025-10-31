import pytesseract
from PIL import Image, ImageFilter, ImageEnhance,ImageOps
import re
import os 
from flask import jsonify

# //processsing the image
def preprocess_image(image_path):
    # Convert to grayscale
    image = Image.open(image_path).convert("L")
    # # Sharpen the image
    image = image.filter(ImageFilter.SMOOTH)
    # Increase contrast
    enhancer = ImageEnhance.Contrast(image)
    image = enhancer.enhance(2)
        # Optional: Invert colors if background is dark
    image = ImageOps.invert(image) if image.getextrema()[0] < 128 else image
    
    # Denoise slightly instead of oversharpening
    image = image.filter(ImageFilter.MedianFilter(size=3))
    return image

def ocr_image(image_path, tesseract_cmd=None):
    if tesseract_cmd:
        pytesseract.pytesseract.tesseract_cmd = tesseract_cmd

    image = preprocess_image(image_path)
    text = pytesseract.image_to_string(image, lang='eng')
    return text

def clean_text(text):
    text = text.replace("<A>", "")
    text = re.sub(r'\s{2,}', ' ', text)
    return text.strip()



def extract_item_lines(text):
    """
    Extracts each line with an amount, splitting the last numeric part (xx.xx) as price.
    Returns list of dictionaries: [{"item": "...", "amount": 12.34}, ...]
    """
    lines = text.split("\n")
    items = []
    
    for line in lines:
        if re.search(r"\d+\.\d{2}", line):
            cleaned = clean_text(line)
            # Extract the last amount in the line
            match = re.search(r"(\d+\.\d{2})(?!.*\d+\.\d{2})", cleaned)
            if match:
                amount = float(match.group(1))
                item_name = cleaned.replace(match.group(1), "").strip()
                items.append({"item": item_name, "amount": amount})
    
    return items

# the following function extracts all the details from the receipt
def read_receipt_jpg(file_path):
    # BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    # image_path = os.path.join(BASE_DIR, "receipt.jpg")

    tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"

    extracted_text = ocr_image(file_path, tesseract_cmd=tesseract_cmd)
    extracted_text_cln = re.sub(r'\s+', ' ', extracted_text).strip()  

    # Get item lines
    items = extract_item_lines(extracted_text)

    # Get total amount
    total_expense_match = re.search(r'total[:\s]*\$?(\d+\.\d{2})', extracted_text_cln, re.IGNORECASE)
    total_amount = float(total_expense_match.group(1)) if total_expense_match else None
    
    # If still not found, return a 400 response
    if total_amount is None or total_amount <= 0:
        return jsonify({
            "status": 400,
            "message": "Total amount not found or invalid in the receipt.",
            "data": None
        }), 400
        # Fallback: Try to find the largest amount as total
    amounts = [item['amount'] for item in items]
    total_amount = max(amounts) if amounts else None

    # Build JSON-serializable dictionary
    receipt_data = {
        "items": items,
        "total_amount": total_amount
    }

    return receipt_data



