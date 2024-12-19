package com.example.printingapp.escposprinter.textparser;

import com.example.printingapp.escposprinter.EscPosPrinterCommands;
import com.example.printingapp.escposprinter.exceptions.EscPosEncodingException;

public interface IPrinterTextParserElement {
    int length() throws EscPosEncodingException;
    IPrinterTextParserElement print(EscPosPrinterCommands printerSocket) throws EscPosEncodingException;
}
