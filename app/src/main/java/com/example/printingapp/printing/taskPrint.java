package com.example.printingapp.printing;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.Normalizer;

import static com.example.printingapp.printing.commands.CHARCODE_PC437;
import static com.example.printingapp.printing.commands.CHARCODE_PC858;

public class taskPrint extends AsyncTask<Void, Void, String> {

    private String msg = null;
    private String msg2 = null;
    private char[] escpos = null;
    private byte[] b_escpos = null;
    private String ipPrint;
    private int port;
    private boolean co;


    public taskPrint(){
        super();

    }
    public taskPrint(String msg, String ipPrint, int port){
        super();
        this.msg = getNormalize(msg);
        this.escpos = escpos;
        this.ipPrint = ipPrint;
        this.port = port;

    }
    public taskPrint(char[] escpos, String ipPrint, int port){
        super();
        this.msg = msg;
        this.escpos = escpos;
        this.ipPrint = ipPrint;
        this.port = port;

    }

    public taskPrint(byte[] b_escpos, String ipPrint, int port){
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
    byte[] data = new byte[0];
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
                    if (msg != null) {
                        out.println(msg.getBytes("windows-1252"));

                        byte[] bytes = msg.getBytes("windows-1252");
                        byte[] data = new byte[bytes.length + this.data.length];
                        System.arraycopy(this.data, 0, data, 0, this.data.length);
                        System.arraycopy(bytes, 0, data, this.data.length, bytes.length);
                        this.data = data;
                        out.write(data);
                        this.msg = null;
                    }
                    if (escpos != null) {
                        out.println(escpos);
                        this.escpos = null;
                    }
                    if (b_escpos != null) {
                        out.println(b_escpos);
                        this.b_escpos = null;
                    }
                        /*out.write(HW_INIT);
                        out.write("test");
                        out.write(euro);
                        out.flush();*/
                    co = true;

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
                co = false;
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

    public boolean isCo() {
        return co;
    }

    public void setCo(boolean co) {
        this.co = co;
    }
}
