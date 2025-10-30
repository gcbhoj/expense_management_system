import pytesseract
from PIL import Image, ImageFilter, ImageEnhance

def preprocess_image(image_path):
    # Convert to grayscale
    image = Image.open(image_path).convert("L")
    # Sharpen the image
    image = image.filter(ImageFilter.SHARPEN)
    # Increase contrast
    enhancer = ImageEnhance.Contrast(image)
    image = enhancer.enhance(2.0)
    return image

def ocr_image(image_path, tesseract_cmd=None):
    if tesseract_cmd:
        pytesseract.pytesseract.tesseract_cmd = tesseract_cmd

    image = preprocess_image(image_path)
    text = pytesseract.image_to_string(image, lang='eng')
    return text

# === Main Program ===
image_path = "receipt.jpg"
tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"

extracted_text = ocr_image(image_path, tesseract_cmd=tesseract_cmd)
print("Extracted Text:\n")
print(extracted_text)
