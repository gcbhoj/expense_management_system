from reportlab.lib.pagesizes import LETTER
from reportlab.lib import colors
from reportlab.lib.units import inch
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle
import os
from reportlab.lib.pagesizes import LETTER
from reportlab.lib import colors
from reportlab.lib.units import inch
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle
import os

def create_receipt_data_pdf(receipt_items, output_file_name):
    """
    Creates a PDF document with receipt data in a tabular format.
    """
    folder = os.path.dirname(output_file_name)
    if folder:  # ✅ Only create if path is not empty
        os.makedirs(folder, exist_ok=True)

    doc = SimpleDocTemplate(output_file_name, pagesize=LETTER)
    elements = []

    table_data = [["Item", "Amount ($)"]]
    for item in receipt_items:
        name = item.get("expenseItemName", "")
        amount = item.get("expenseItemCost", "")
        table_data.append([name, str(amount)])

    table = Table(table_data, colWidths=[4.5 * inch, 1.5 * inch])
    table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), colors.lightgrey),
        ('GRID', (0, 0), (-1, -1), 0.5, colors.black),
        ('ALIGN', (1, 1), (-1, -1), 'RIGHT'),
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
    ]))

    elements.append(table)
    doc.build(elements)
    print(f"✅ PDF saved as {output_file_name}")
    return output_file_name

