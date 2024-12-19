package com.example.printingapp.printing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.printingapp.Produit;
import com.example.printingapp.R;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.printingapp.printing.commands.*;

public class Printer {

    /* Au demarrage de l'imprimante > 5sec sont nécéssaires (apres la sortie papier linefeed)
    solution:compter temps entre ping OK et pingOk+5sec et foutre l'etat en base)*/
    private String ip;
    private int port;
    private int id;
    private String name;
    private int type; // vente(0) ou prep(1)
    private MyPrintUtils imp;

    //private boolean isConnected;

    /* CONSTRUCTEURS */
    public Printer(String ip, int port, int id, String name, int type) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.name = name;
    }

    public Printer() {

    }
    /* FIN CONSTRUCTEURS */
    /* FONCTIONS D'IMPRESSION */


    private taskCheck pTask;
    private boolean etat;

    public void imprimer(Context applicationContext, Entete ent, List<Produit> list_Produit, int type, boolean ouvert, double table, Client c) {
        String tkn = "1234";

        //check du token
        pTask = new taskCheck("checkConnectivity", ip, port);
        pTask.execute();
        new CountDownTimer(200, 200) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                etat = pTask.getEtat();

                if (etat == true) {
                    //Toast.makeText(applicationContext, "connectee", Toast.LENGTH_SHORT).show();
                    //todo
                    print(ent, list_Produit, type, ouvert, table, c);
                } else {
                    //Toast.makeText(applicationContext, "non connectee", Toast.LENGTH_SHORT).show();
                    //dialog imprimante indisponible / réessayer
                }

            }

        }.start();

    }

    public void print(Entete ent, List<Produit> list_Produit, int type, boolean ouvert, double table, Client c) {

        String type_ticket = "";
        String detail_type = "";
        switch(type){
            case 0 : type_ticket = "Vente direct";
                break;
            case 1 : type_ticket = "Table "+String.valueOf(table);
                break;
            case 2 : type_ticket = "Livraison"+String.valueOf(""/*nom client.getnom*/)+'\n'+"Detail client"+'\n'+"Detail client"+'\n'+"Detail client"+'\n'+"Detail client"+'\n'+"Detail client"+'\n';

                break;

        }

        //traitement des remise et ajout quantité correspondantes
        List<Produit> list_Produit_InTreat = list_Produit;
        for(int i = 0; i < list_Produit.size(); i++){
            for(int j = 0; j < list_Produit_InTreat.size(); j++){
                if(list_Produit.get(i).getId()==list_Produit_InTreat.get(j).getId() && list_Produit.get(i).getRemise()==list_Produit_InTreat.get(j).getRemise() && i!=j ){
                    list_Produit.get(i).setQte(list_Produit.get(i).getQte()+1);
                    list_Produit.remove(j);
                }
            }
        }

        Collections.sort(list_Produit,new ClasserProduitSelonRemise());

        imp = new MyPrintUtils();
        // init print
        taskPrint pTask = new taskPrint(HW_INIT, ip, port);
        pTask.execute();
        pTask = new taskPrint(TXT_ALIGN_CT, ip, port);
        pTask.execute();
        pTask = new taskPrint(TXT_2WIDTH, ip, port);
        pTask.execute();
        pTask = new taskPrint(MyPrintUtils.entete_ticket(ent.getNom_enseigne()),ip,port);
        pTask.execute();
        pTask = new taskPrint(TXT_NORMAL, ip, port);
        pTask.execute();
        pTask = new taskPrint(MyPrintUtils.entete_ticket(ent),ip,port);
        pTask.execute();
        pTask = new taskPrint(TXT_ALIGN_LT, ip, port);
        pTask.execute();
        pTask = new taskPrint(type_ticket, ip, port);
        pTask.execute();
        pTask = new taskPrint(MyPrintUtils.detail(list_Produit), ip, port);
        pTask.execute();
        pTask = new taskPrint(TXT_2WIDTH, ip, port);
        pTask.execute();
        pTask = new taskPrint(MyPrintUtils.total(list_Produit), ip, port);
        pTask.execute();

        if(ouvert==false){
            pTask = new taskPrint(MyPrintUtils.rendu(0), ip, port); // TODO
            pTask.execute();
            pTask = new taskPrint(TXT_NORMAL, ip, port);
            pTask.execute();
            pTask = new taskPrint(MyPrintUtils.detail_paiement(4.55,0,0,50), ip, port);
            pTask.execute();
        }else{
            pTask = new taskPrint(TXT_NORMAL, ip, port);
            pTask.execute();
            //pTask = new taskPrint(MyPrintUtils.resteapayer(*), ip, port); // TODO
            //pTask.execute();
        }

        pTask = new taskPrint(MyPrintUtils.tva(list_Produit), ip, port);
        pTask.execute();
        pTask = new taskPrint(MyPrintUtils.bas_de_page(new Vendeur(),"154878-652-100"), ip, port);
        pTask.execute();

        if(detail_type.length()>1){
            pTask = new taskPrint(detail_type, ip, port);
            pTask.execute();
        }

        pTask = new taskPrint(TXT_ALIGN_CT, ip, port);
        pTask.execute();
        pTask = new taskPrint(TXT_2WIDTH, ip, port);
        pTask.execute();
        if(type==0 || type==1){
            pTask = new taskPrint(MyPrintUtils.message_bas_de_page("Merci de votre visite"+'\n'+"À bientôt !"), ip, port);
            pTask.execute();
        }
        if(type==2){
            pTask = new taskPrint(MyPrintUtils.message_bas_de_page("Merci de votre commande"+'\n'+"À bientôt !"), ip, port);
            pTask.execute();
        }

        pTask = new taskPrint(CTL_LF, ip, port);
        pTask.execute();
        pTask = new taskPrint(CTL_LF, ip, port);
        pTask.execute();
        pTask = new taskPrint(PAPER_PART_CUT, ip, port);
        pTask.execute();




    }

    public void imprimerImage(Context applicationContext) {
        String tkn = "1234";
        //check du token
        pTask = new taskCheck("checkConnectivity", ip, port);
        pTask.execute();
        new CountDownTimer(200, 200) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                etat = pTask.getEtat();

                if (etat == true) {
                    Toast.makeText(applicationContext, "connectee", Toast.LENGTH_SHORT).show();
                    //todo
                    printImage(applicationContext);
                } else {
                    Toast.makeText(applicationContext, "non connectee", Toast.LENGTH_SHORT).show();
                    //dialog imprimante indisponible / réessayer
                }

            }

        }.start();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final int width = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().height() : drawable.getIntrinsicHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width,
                height <= 0 ? 1 : height, Bitmap.Config.ARGB_8888);

        Log.v("Bitmap width - Height :", width + " : " + height);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public byte[] test(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }


    private void printImage(Context applicationContext) {
        imp = new MyPrintUtils();

        // init print
        taskPrint pTask = new taskPrint(HW_INIT, ip, port);
        pTask.execute();
        pTask = new taskPrint(TXT_ALIGN_CT, ip, port);
        pTask.execute();
        pTask = new taskPrint(TXT_2WIDTH, ip, port);
        pTask.execute();
        pTask = new taskPrint("Image",ip,port);
        pTask.execute();
        pTask = new taskPrint(TXT_NORMAL, ip, port);
        pTask.execute();
        pTask = new taskPrint("Test",ip,port);
        pTask.execute();
        pTask = new taskPrint(SELECT_BIT_IMAGE_MODE,ip,port);
        pTask.execute();





        pTask = new taskPrint(HW_INIT, ip, port);
        pTask.execute();
        pTask = new taskPrint(BEEPER, ip, port);
        pTask.execute();



    }


    /* FIN FONCTIONS D'IMPRESSION */
    /* DEBUT GETTER SETTER */
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }




    /* FIN GETTER SETTER */
}
class ClasserProduitSelonRemise implements Comparator<Produit> {

    // Function to compare
    public int compare(Produit c1, Produit c2)
    {
        if (c1.getRemise() == c2.getRemise())
            return 0;
        else if (c1.getRemise() > c2.getRemise())
            return 1;
        else
            return -1;
    }
}
