package com.example.printingapp.printing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.example.printingapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.Normalizer;

public class taskPrintImage extends AsyncTask<Void, Void, String> {

    private Context ctx = null;
    private String msg = null;
    private String msg2 = null;
    private char[] escpos = null;
    private byte[] b_escpos = null;
    private String ipPrint;
    private int port;
    private Drawable drawable;


    public taskPrintImage(){
        super();

    }
    public taskPrintImage(String msg, String ipPrint, int port, Context ctx){
        super();
        this.msg = getNormalize(msg);
        this.escpos = escpos;
        this.ipPrint = ipPrint;
        this.port = port;
        this.ctx = ctx;

    }
    public taskPrintImage(char[] escpos, String ipPrint, int port){
        super();
        this.msg = msg;
        this.escpos = escpos;
        this.ipPrint = ipPrint;
        this.port = port;

    }

    public taskPrintImage(byte[] b_escpos, String ipPrint, int port){
        super();
        this.msg = msg;
        this.escpos = escpos;
        this.b_escpos = b_escpos;
        this.ipPrint = ipPrint;
        this.port = port;
    }

    public String getIpPrint() {
        return ipPrint;
    }

    public void setIpPrint(String ipPrint) {
        this.ipPrint = ipPrint;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    // constructeur 2

    // fonction ASCII normalisation
    public String getNormalize(String s){
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    //
    public String doInBackground(Void... params) {
        String ret = null;
        BufferedReader in = null;
        PrintWriter out = null;
        Socket socket = null;
        InetAddress inetIPprint = null;
        //TextView Logs = (TextView) findViewById(R.id.TextViewLogs);
        try {
            inetIPprint = InetAddress.getByName(ipPrint);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            Log.d("ABC","Try") ;
            if(inetIPprint.isReachable(100)) {
                try {
                    Log.d("ABC","Reached") ;
                    socket = new Socket(ipPrint, 9100);    //adresse IP de l'imprimante
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream());
                    printPhoto(ctx, out);

                    //ret = b.readLine();
                } catch (Exception ex) {
                    // on utilise Log sous android !
                    Log.d("ABC", "Failure 1 !", ex);
                    //Logs.setText("Failure printer");
                } finally {


                    // il faut tout fermer hein !!!
                    if (out != null) out.close();
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Log.d("ABC", "Absente reseau");

            }
        } catch (IOException e) {
            Log.d("ABC", "Failure 1 !", e);
            e.printStackTrace();
        }

        //Logs.setText(in.toString());
        return ret;
    }

    public void onPostExecute(String result)
    {
        if (result != null) {
            //TextView Logs = (TextView) findViewById(R.id.TextViewLogs);
            //Logs.setText(result);
        }
    }
    public void printPhoto(Context ctx, PrintWriter o) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.logoprint60);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                o.println(new byte[]{0x1B, 0x2A, 33, -128, 0});
                o.println(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

}
