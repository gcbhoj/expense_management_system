from PyPDF2 import PdfReader
import re
from collections import defaultdict
import pandas as pd
import os

# -------- STEP 1: Read and extract text from PDF --------
def extract_text_from_pdf(pdf_path):
    reader = PdfReader(pdf_path)
    text = ""
    for page in reader.pages:
        text += page.extract_text() + "\n"
    return text

# -------- STEP 2: Extract items and amounts from text --------
def extract_items(text):
    # Matches lines like "Coffee 3.50", "Pen Set $12.99", etc.
    pattern = r"([A-Za-z0-9\s\-\&\.\,]+?)\s+\$?(\d+\.\d{2})"
    items = re.findall(pattern, text)
    return [(name.strip(), float(amount)) for name, amount in items]

# -------- STEP 3: Categorize items --------
def categorize_item(name):
    n = name.lower()
    if any(word in n for word in ["meal", "restaurant", "coffee", "drink", "food", "snack"]):
        return "Food & Beverage"
    elif any(word in n for word in ["taxi", "bus", "uber", "flight", "train", "transport", "fuel", "gas"]):
        return "Travel"
    elif any(word in n for word in ["hotel", "motel", "inn", "stay", "accommodation"]):
        return "Lodging"
    elif any(word in n for word in ["pen", "notebook", "stationery", "office", "folder", "paper", "stapler"]):
        return "Office Supplies"
    elif any(word in n for word in ["subscription", "license", "software", "service"]):
        return "Services & Software"
    else:
        return "Other"

# -------- STEP 4: Combine everything --------
def analyze_receipt(pdf_path):
    text = extract_text_from_pdf(pdf_path)
    items = extract_items(text)

    categorized_items = [
        {"Item": name, "Amount": amount, "Category": categorize_item(name)}
        for name, amount in items
    ]

    totals = defaultdict(float)
    for entry in categorized_items:
        totals[entry["Category"]] += entry["Amount"]

    print("\n--- Extracted Items ---")
    for entry in categorized_items:
        print(f"{entry['Item']} (${entry['Amount']:.2f}) → {entry['Category']}")

    print("\n--- Category Totals ---")
    for cat, amt in totals.items():
        print(f"{cat}: ${amt:.2f}")

    # # Optional: Save to Excel for reporting
    # output_path = os.path.join(os.path.dirname(pdf_path), "categorized_receipt.xlsx")
    # df = pd.DataFrame(categorized_items)
    # df.to_excel(output_path, index=False)
    # print(f"\n✅ Saved results to {output_path}")

# -------- STEP 5: Run the analyzer --------
if __name__ == "__main__":
    pdf_path = "eReceipt.pdf"  # Change if needed
    analyze_receipt(pdf_path)
