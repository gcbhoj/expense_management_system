import img2pdf
import os

def convert_image_to_pdf(image_path,output_name=None):
    """
    Converts an image to PDF with A4 page size and saves it with a dynamic name.
    
    :param image_path: Path to the input image file (string or list of strings for multiple images)
    :param output_pdf_path: Directory where PDF should be saved
    :param output_name: Name of the output PDF file (without extension)
    """
    # Determining the folder of the input image
    if isinstance(image_path, list):
        folder = os.path.dirname(image_path[0])
    else:
        folder =  os.path.dirname(image_path)
        
    # Determining PDF filename
    if output_name is None:
        output_name = os.path.splitext(os.path.basename(image_path))[0]
        
        pdf_file = os.path.join(folder, f"{output_name}.pdf")
    
    pdf_file = os.path.join(folder, f"{output_name}.pdf")
    
    # Specifying A4 page size in points
    a4inpt = (img2pdf.mm_to_pt(210), img2pdf.mm_to_pt(297))
    layout_fun = img2pdf.get_layout_fun(a4inpt)
    
    # Convert image(s) to PDF
    with open(pdf_file, "wb") as f:
        # img2pdf.convert accepts a string path or a list of string paths
        f.write(img2pdf.convert(image_path, layout_fun=layout_fun))
    
    return pdf_file
