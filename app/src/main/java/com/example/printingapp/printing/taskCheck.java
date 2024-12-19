package com.example.printingapp.printing;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.Normalizer;

public class taskCheck extends AsyncTask<Void, Void, String> {

    private String msg = null;
    private String msg2 = null;
    private char[] escpos = null;
    private byte[] b_escpos = null;
    private String ipPrint;
    private int port;
    private boolean etat;


    public taskCheck(){
        super();

    }
    public taskCheck(String msg, String ipPrint, int port){
        super();
        this.msg = getNormalize(msg);
        this.escpos = escpos;
        this.ipPrint = ipPrint;
        this.port = port;

    }
    public taskCheck(char[] escpos, String ipPrint, int port){
        super();
        this.msg = msg;
        this.escpos = escpos;
        this.ipPrint = ipPrint;
        this.port = port;

    }

    public taskCheck(byte[] b_escpos, String ipPrint, int port){
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

            if(inetIPprint.isReachable(100)) {
                    try {
                    Log.d("ABC","Reached") ;
                    etat = true;


                } catch (Exception ex) {
                    Log.e("ConnectionTask", "Failure !", ex);
                } finally {

            }
        }else{
            Log.d("ABC","Not reached") ;
            etat = false;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void onPostExecute(String result)
    {
        if (result != null) {

        }
    }

    public boolean getEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }
}