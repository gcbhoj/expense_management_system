import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReceiptData {
  receipt_data?: {
    total_amount?: number;
    items?: Array<{
      item: string;
      amount: number;
    }>;
    [key: string]: any;
  };
  error?: string;
}

@Injectable({
  providedIn: 'root',
})
export class ReceiptService {
  private pythonApiUrl = 'http://127.0.0.1:5001/api/py';

  constructor(private http: HttpClient) {}

  /**
   * Read receipt from PDF file
   * @param file PDF file to process
   * @returns Observable with receipt data
   */
  readReceiptPdf(file: File): Observable<ReceiptData> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<ReceiptData>(
      `${this.pythonApiUrl}/read_receipt_pdf`,
      formData
    );
  }

  /**
   * Read receipt from JPG/JPEG file
   * @param file Image file to process
   * @returns Observable with receipt data
   */
  readReceiptJpg(file: File): Observable<ReceiptData> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<ReceiptData>(
      `${this.pythonApiUrl}/read_receipt_jpg`,
      formData
    );
  }

  /**
   * Automatically detect file type and call appropriate endpoint
   * @param file Receipt file (PDF or image)
   * @returns Observable with receipt data
   */
  readReceipt(file: File): Observable<ReceiptData> {
    const fileType = file.type.toLowerCase();

    if (fileType.includes('pdf')) {
      return this.readReceiptPdf(file);
    } else if (
      fileType.includes('image') ||
      fileType.includes('jpg') ||
      fileType.includes('jpeg')
    ) {
      return this.readReceiptJpg(file);
    } else {
      throw new Error(
        'Unsupported file type. Please upload a PDF or JPG/JPEG file.'
      );
    }
  }
}
