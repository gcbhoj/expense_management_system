from PyPDF2 import PdfReader
import re
import json
import os
from flask import jsonify

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

def extract_total(text):
    """
    Finds the total amount from the receipt text.
    """
    match = re.search(r'total[\s:]*\$?\s*(\d+\.\d{2})', text, re.IGNORECASE)
    if match:
        return float(match.group(1))
    return None


# ---- Main Section ----
def get_receipt_data(file_path):
    # BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    # pdf_path = os.path.join(BASE_DIR, "ereceipt.pdf")

    with open(file_path, "rb") as file:
        reader = PdfReader(file)
        number_of_pages = len(reader.pages)
        print(f"Number of pages: {number_of_pages}")

        all_items = []
        all_text = ""

        for page_num in range(number_of_pages):
            text = reader.pages[page_num].extract_text()
            all_text += text + "\n"
            items = extract_item_lines(text)
            all_items.extend(items)

    # Extract total
    total_amount = extract_total(all_text)
        # If still not found, return a 400 response
    if total_amount is None or total_amount <= 0:
        return jsonify({
            "status": 400,
            "message": "Total amount not found or invalid in the receipt.",
            "data": None
        }), 400

    # Build JSON
    receipt_data = {
        "items": all_items,
        "total_amount": total_amount
    }

    receipt_json = json.dumps(receipt_data, indent=4)
    print("\nJSON Output:\n")
    print(receipt_json)
    return receipt_data

# # Optionally save to file
# with open("receipt_data.json", "w") as f:
#     f.write(receipt_json)
