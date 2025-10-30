from PyPDF2 import PdfReader

with open("receiptOutput.pdf", "rb") as file:
    reader = PdfReader(file)
    number_of_pages = len(reader.pages)
    
    metadata = reader.metadata  # instance property, not class property
    total_expense = metadata.get("total", "N/A")  # safely get TOTAL key if it exists
    
    
    for page in reader.pages:
        text = page.extract_text()
        print(text)
    
