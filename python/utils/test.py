from receiptparser.config import read_config
from receiptparser.parser import process_receipt

config = read_config('my_config.yml')
receipt = process_receipt(config, "receipt.jpg", out_dir=None, verbosity=0)

print(receipt)

# print("Filename:   ", receipt.filename)
# print("Company:    ", receipt.company)
# print("Postal code:", receipt.postal)
# print("Date:       ", receipt.date)
# print("Amount:     ", receipt.sum)