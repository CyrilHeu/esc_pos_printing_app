package com.example.printingapp.escposprinter.barcode;

import com.example.printingapp.escposprinter.EscPosPrinterCommands;
import com.example.printingapp.escposprinter.EscPosPrinterSize;
import com.example.printingapp.escposprinter.exceptions.EscPosBarcodeException;

public class BarcodeEAN8 extends BarcodeNumber {
    public BarcodeEAN8(EscPosPrinterSize printerSize, String code, float widthMM, float heightMM, int textPosition) throws EscPosBarcodeException {
        super(printerSize, EscPosPrinterCommands.BARCODE_TYPE_EAN8, code, widthMM, heightMM, textPosition);
    }

    @Override
    public int getCodeLength() {
        return 8;
    }
}
