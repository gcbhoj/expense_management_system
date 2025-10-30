import img2pdf


#Specifying the paper size and margins
a4inpt = (img2pdf.mm_to_pt(210), img2pdf.mm_to_pt(297))
layout_fun = img2pdf.get_layout_fun(a4inpt)
with open("receiptOutput.pdf","wb") as f:
    f.write(img2pdf.convert("receipt.jpg",layout_fun=layout_fun))