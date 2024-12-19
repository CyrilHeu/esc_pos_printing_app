package com.example.printingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.printingapp.async.AsyncEscPosPrinter;
import com.example.printingapp.async.AsyncTcpEscPosPrint;
import com.example.printingapp.escposprinter.connection.DeviceConnection;
import com.example.printingapp.escposprinter.connection.tcp.TcpConnection;
import com.example.printingapp.escposprinter.textparser.PrinterTextParserImg;
import com.example.printingapp.printing.Client;
import com.example.printingapp.printing.Entete;
import com.example.printingapp.printing.Printer;
import com.example.printingapp.printing.Utils;
import com.example.printingapp.printing.taskPrint;
import com.example.printingapp.printing.taskPrintImage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

import static com.example.printingapp.printing.commands.CHARCODE_PC1252;
import static com.example.printingapp.printing.commands.CHARCODE_PC437;
import static com.example.printingapp.printing.commands.CHARCODE_PC858;
import static com.example.printingapp.printing.commands.HW_INIT;
import static com.example.printingapp.printing.commands.SELECT_BIT_IMAGE_MODE;
import static com.example.printingapp.printing.commands.TXT_ALIGN_CT;
import static com.example.printingapp.printing.commands.TXT_ALIGN_LT;

public class MainActivity extends AppCompatActivity {

    Button BTN_test_print;
    Printer printer;
    WebView WebViewPrintState;
    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BTN_test_print = findViewById(R.id.BTN_print);

        WebViewPrintState = (WebView) findViewById(R.id.webviewstate);
        WebViewPrintState.setWebViewClient(new WebViewClient());
        WebViewPrintState.setVisibility(View.INVISIBLE);

        //printer = new Printer("192.168.1.23",9100,1 ,"Printer test", 0);

        BTN_test_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //printer.printTest();
                Entete ent = new Entete("Nom enseigne",
                        "15 Chemin de l'adresse de l'enseigne",
                        "06.00.00.00.00",
                        "www.sitewebdelenseigne.fr",
                        "Ouvert tous les jours sauf le lundi",
                        "de 11h à 23h");

                List<Produit> list_Produit = new ArrayList<Produit>();
                Produit p1_ticket = new Produit(1,"Produité ", 8.55,5.5, 0);
                Produit p2_ticket = new Produit(2,"Produit 2", 8,10, 0);
                Produit p3_ticket = new Produit(3,"Produit 3", 10.25,20, 0);
                Produit p4_ticket = new Produit(1,"Produité ", 8.55,5.5, 0);
                Produit p5_ticket = new Produit(2,"Produit 2", 8,10, 10);
                Produit p6_ticket = new Produit(2,"Produit 2", 8,10, 50);
                Produit p7_ticket = new Produit(4,"Produit 4", 8,10, 0);

                //setting des quantité par produit si avec remise ou non

                list_Produit.add(p1_ticket);
                list_Produit.add(p2_ticket);
                list_Produit.add(p3_ticket);
                list_Produit.add(p4_ticket);
                list_Produit.add(p5_ticket);
                list_Produit.add(p6_ticket);
                list_Produit.add(p7_ticket);

                Client c = new Client(1,"Toto","Titi");



                //printer.imprimer(getApplicationContext(), ent, list_Produit, 1, true,18,c);

                //PrinterTextParserImg.
                //printer.imprimerImage(getApplicationContext());
                //Bitmap myLogo = ((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.logoprint60, null)).getBitmap();
                //printImage(getPixelsSlow(BitmapFactory.decodeResource(getResources(),R.drawable.logoprint60)));


                taskPrint task = new taskPrint(HW_INIT,"192.168.1.23",9100);
                task.execute();
                task = new taskPrint(CHARCODE_PC1252,"192.168.1.23",9100);
                task.execute();

                task = new taskPrint("msg","192.168.1.23",9100);
                task.execute();

                /*taskPrintImage task = new taskPrintImage("test","192.168.1.23",9100, getApplicationContext());
                task.execute();*/
                /*taskPrint task = new taskPrint(HW_INIT,"192.168.1.23",9100);
                task.execute();

                printTcp();*/
                /*task = new taskPrint(TXT_ALIGN_LT,"192.168.1.23",9100);
                task.execute();
                task = new taskPrint("test","192.168.1.23",9100);
                task.execute();*/

            }

        });
    }
    public void printTcp() {

        try {
            // this.printIt(new TcpConnection(ipAddress.getText().toString(), Integer.parseInt(portAddress.getText().toString())));
            new AsyncTcpEscPosPrint(this)
                    .execute(this.getAsyncEscPosPrinter(new TcpConnection("192.168.1.23", 9100)));
        } catch (NumberFormatException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Invalid TCP port address")
                    .setMessage("Port field must be a number.")
                    .show();
            e.printStackTrace();
        }
    }

    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 72, 1);
        return printer.setTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logotoprint, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n"
                + "[L]\n"
        );
    }



}
