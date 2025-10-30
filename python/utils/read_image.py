import pytesseract
from PIL import Image, ImageFilter, ImageEnhance,ImageOps
import re
import os 
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

# the following function extracts the total amount from the receipt and sents it to the main java controller
def extract_total_expense():

    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    image_path = os.path.join(BASE_DIR, "receipt.jpg")

    tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"

    extracted_text = ocr_image(image_path, tesseract_cmd=tesseract_cmd)
    extracted_text = extracted_text.lower()  # Convert to lowercase
    # # extracted_text = re.sub(r'http\S+', '', extracted_text)  # Remove URLs
    # extracted_text = re.sub(r'[^\w\s]', '', extracted_text)  # Remove punctuation
    extracted_text = re.sub(r'\s+', ' ', extracted_text).strip()  

    total_expense_match = re.search(r'total[:\s]*\$?(\d+\.\d{2})', extracted_text)
    # print("Total Expense Found:", total_expense_match.group(1) if total_expense_match else "Not Found")
    # print("Extracted Text:\n")
    # print(extracted_text)
    return total_expense_match
