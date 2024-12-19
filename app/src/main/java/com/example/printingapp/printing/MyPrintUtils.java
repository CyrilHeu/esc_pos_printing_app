package com.example.printingapp.printing;

import com.example.printingapp.Produit;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MyPrintUtils {

    private int cpt;
    private static int nb_lower = 48;
    private static int nb_upper = 24;
    private static String devise = " EUR";
    private static DecimalFormat df;
    private static DecimalFormat dfremise;
    private static List<Double> listTVA;
    private static String line = "------------------------------------------------";

    public MyPrintUtils() {
        df = new DecimalFormat("0.00");
        dfremise = new DecimalFormat("0");
        listTVA = new ArrayList<Double>();
    }

    public static String detail(List<Produit> listP){

        String ligne = "";
        List<Produit> toTreat = listP;
        //entête
        String Qte_t = "Qte";
        String Nom_t = "Désignation";
        String PrixU_t = "Prix U.";
        String PrixT_t = "Prix";
        int s_Qte_t = Qte_t.length();
        int s_Nom_t = Nom_t.length();
        int s_PrixU_t = PrixU_t.length();
        int s_PrixT_t = PrixT_t.length();

        int l_qte_max = 3;
        int l_element_max = 17;
        int l_prixUnitaire_max = 11;
        int l_prixTotal_max = 12;

        String espace1 = " ";
        String espace2 = "  ";

        ligne+=Qte_t;
        ligne+=espace1;
        ligne+=Nom_t;
        for(int i = 0; i < l_element_max - s_Nom_t;i++){
            ligne+= " ";
        }

        ligne+=espace1;
        for(int i = 0; i < l_prixUnitaire_max - s_PrixU_t;i++){
            ligne+= " ";
        }
        ligne+=espace2;
        ligne+=PrixU_t;
        for(int i = 0; i < l_prixTotal_max - s_PrixT_t-2;i++){ // -2 ici pour aligner le P de Prix sur la deuxieme decimal du prix
            ligne+= " ";
        }

        ligne+=PrixT_t+'\n';

        for(int i = 0; i < listP.size(); i++){

            if(listP.get(i).getRemise()>0){
                ligne += "      Réduction de "+dfremise.format(listP.get(i).getRemise())+"% :"+'\n';
                //6 espaces pour ne pas afficher le texte dansq la colonne des Qte
            }
            
            String s_qte = String.valueOf(listP.get(i).getQte());
            String s_prixUnitaire = String.valueOf(df.format(listP.get(i).getPrix_u()));
            String s_prixTotal = String.valueOf(df.format(listP.get(i).getPrix_t()));
            String s_element = String.valueOf(listP.get(i).getNom());

            s_prixTotal = s_prixTotal + devise;

            int l_qte = s_qte.length();
            int l_element =  s_element.length();
            int l_prixUnitaire = s_prixUnitaire.length();
            int l_prixTotal = s_prixTotal.length();

            int ecart1 = l_qte_max - l_qte;
            //sous entendu length de qte inferieur a 3 (taille max)
            for(int j = 0; j<ecart1;j ++){
                ligne += espace1;
            }
            ligne += s_qte;
            ligne+=espace1; // 1 espace obligatoire entre qte et element
            int ecart2 = l_element_max - l_element;
            ligne += s_element; //sous entendu length de qte inferieur a 18 (taille max)
            for(int j = 0; j<ecart2;j ++){
                ligne += espace1;
            }
            ligne+=espace2; // 2 espaces obligatoire entre qte et element

            int ecart3 = l_prixUnitaire_max - l_prixUnitaire;

            for(int j = 0; j<ecart3;j ++){
                ligne += espace1;
            }
            ligne += s_prixUnitaire;
            ligne+=espace2;

            int ecart4 = l_prixTotal_max - l_prixTotal;

            for(int j = 0; j<ecart4;j ++){
                ligne += espace1;
            }
            ligne += s_prixTotal;

        }

        return ligne;
    }

    public static String total(List<Produit> listP){//2WIDTH fonction

        String ligne = "";
        //total
        double Total=0;
        for(int i = 0; i<listP.size();i++){
            Total += listP.get(i).getPrix_t();
        }
        String strTotal = String.valueOf(df.format(Total))+devise;
        String strEnt = "TOTAL";
        int sizeTotal = strTotal.length();
        int sizeEnt = strEnt.length();
        int ecart = nb_upper - sizeEnt -  sizeTotal;

        String ligneTotal = strEnt;
        for(int j = 0 ; j < ecart ; j++){
            ligneTotal+= " ";
        }
        ligneTotal += strTotal ;
        ligne += '\n' + ligneTotal ;

        return ligne;

    }//2WIDTH fonction

    public static String tva(List<Produit> listP){
        //tva
        String ligne = "";
        ligne+=line;
        ligne+=entete_tva();
        for(int i =0; i<listP.size();i++){
            if(listTVA.contains(listP.get(i).getTva())==false){
                listTVA.add(listP.get(i).getTva());
            }
        }
        Collections.sort(listTVA);
        for(int i = 0; i < listTVA.size(); i++){
            ligne += detail_ligne_tva(listP, listTVA.get(i));
            ligne += '\n';
        }
        return ligne;
        // fin tva
    }

    private static String detail_ligne_tva(List<Produit> listP, Double tva) {
        String ligne ="";
        String ligneTvaDet="";
        if(tva>0){
            float MontantTva = 0;
            float MontantTTC = 0;
            for(int i = 0; i < listP.size();i++){
                if(listP.get(i).getTva()==tva){
                    MontantTTC+=listP.get(i).getPrix_t();
                }
            }
            MontantTva = (float) (MontantTTC-(MontantTTC*(0.01*tva)));

            String strTauxDet = String.valueOf(df.format(tva)) + "%";
            String strMontantHtDet = String.valueOf(df.format(MontantTva))+devise;
            String strMontantTtcDet = String.valueOf(df.format(MontantTTC))+devise;
            String strTvaDet= String.valueOf(df.format(MontantTTC-MontantTva))+devise;

            int sizeTauxDet = strTauxDet.length();
            int sizeMontantHtDet = strMontantHtDet.length();
            int sizeMontantTtcDet = strMontantTtcDet.length();
            int sizeTvaDet = strTvaDet.length();

            int sizeTauxDetMax = 10;
            int sizeMontantHtMax = 12;
            int sizeMontantTtcMax = 12;
            int sizeTvaDetMax = 11;

            ligne += strTauxDet;
            int ecart = sizeTauxDetMax-sizeTauxDet;
            for(int i = 0; i < ecart;i++){
                ligne +=" ";
            }
            ligne +=" ";
            int ecart1 = sizeMontantHtMax-sizeMontantHtDet;
            for(int i = 0; i < ecart1;i++){
                ligne +=" ";
            }
            ligne += strMontantHtDet;
            ligne +=" ";

            int ecart2 = sizeMontantTtcMax-sizeMontantTtcDet;
            for(int i = 0; i < ecart2;i++){
                ligne +=" ";
            }
            ligne += strMontantTtcDet;
            ligne +=" ";
            int ecart3 = sizeTvaDetMax-sizeTvaDet;
            for(int i = 0; i < ecart3;i++){
                ligne +=" ";
            }
            ligne += strTvaDet;

        }
    return ligne;
    }
    public static String entete_tva(){
        /* tva -> tronc */

        String strTaux = "Taux TVA";
        String strMontantHt = "Montant HT";
        String strMontantTtc = "Montant TTC";
        String strTva = "T.V.A";
        String ligne ="";
        String ligneTvaDet="";

        int sizeTaux = strTaux.length();
        int sizeMontantHt = strMontantHt.length();
        int sizeMontantTtc = strMontantTtc.length();
        int sizeTvaDet = strTva.length();

        int sizeTauxHtMax = 12;
        int sizeMontantHtMax = 9;
        int sizeMontantTtcMax = 10;
        int sizeTvaDetMax = 11;

        String espace1 =" "; //espace de 1
        String espace2 ="   ";
        String espace3 ="   ";

        int ecart1 = sizeTauxHtMax - sizeTaux;
        ligne += strTaux;
        for(int i = 0; i<ecart1;i++){
            ligne +=" ";
        }
        ligne += espace1;

        int ecart2 = sizeMontantHtMax - sizeMontantHt;
        ligne+= strMontantHt;
        for(int i=0; i<ecart2; i++){
            ligne +=" ";
        }
        ligne += espace2;

        int ecart3 = sizeMontantTtcMax - sizeMontantTtc;
        ligne+= strMontantTtc;
        for(int i=0; i<ecart3; i++){
            ligne +=" ";
        }

        ligne += espace3;
        ligne += strTva+'\n';

        return ligne;
    }

    public static String rendu(double rendu){ //2WIDTH fonction
        String ligne ="";
        ligne+="RENDU";
        int size_text = "RENDU".length();
        int size_rendu = (String.valueOf(rendu)+devise).length();
        for(int i=0;i<nb_upper-size_rendu-size_text;i++){
            ligne+=" ";
        }
        ligne+= String.valueOf(rendu)+devise;
        return ligne;
    }
    public static String detail_paiement(double cb, double esp, double tr, double cheque){ //normal text
        String ligne="";
        String paiement = "PAIEMENT";
        int s_paiement = paiement.length();

        String str_cb_t ="CB";
        int s_cb_t = str_cb_t.length();
        String str_cb = String.valueOf(cb)+devise;
        int s_cb = str_cb.length();

        String str_esp_t = "ESP";
        int s_esp_t = str_esp_t.length();
        String str_esp = String.valueOf(esp)+devise;
        int s_esp = str_esp.length();

        String str_tr_t = "TR";
        int s_tr_t = str_tr_t.length();
        String str_tr = String.valueOf(tr)+devise;
        int s_tr = str_tr.length();

        String str_cheque_t = "CHEQUE";
        int s_cheque_t = str_cheque_t.length();
        String str_cheque = String.valueOf(cheque)+devise;
        int s_cheque = str_cheque.length();

        int w_col = 12;
        String space = " ";
        ligne+=paiement+'\n';

        if(cb>0){
            ligne+=str_cb_t;
            for(int i = 0; i<w_col-s_cb_t;i++){
                ligne+=" ";
            }
        }
        if(esp>0){
            ligne+=str_esp_t;
            for(int i = 0; i<w_col-s_esp_t;i++){
                ligne+=" ";
            }
        }
        if(tr>0){
            ligne+=str_tr_t;
            for(int i = 0; i<w_col-s_tr_t;i++){
                ligne+=" ";
            }
        }
        if(cheque>0){
            ligne+=str_cheque_t;
            for(int i = 0; i<w_col-s_cheque_t;i++){
                ligne+=" ";
            }
        }
        ligne+='\n';

        if(cb>0){
            ligne+=str_cb;
            for(int i = 0; i<w_col-s_cb;i++){
                ligne+=" ";
            }
        }
        if(esp>0){
            ligne+=str_esp;
            for(int i = 0; i<w_col-s_esp;i++){
                ligne+=" ";
            }
        }
        if(tr>0){
            ligne+=str_tr;
            for(int i = 0; i<w_col-s_tr;i++){
                ligne+=" ";
            }
        }
        if(cheque>0){
            ligne+=str_cheque;
            for(int i = 0; i<w_col-s_cheque;i++){
                ligne+=" ";
            }
        }
        ligne+='\n';

        return ligne;
    }

    public static String info_client(Client c){
        String ligne="";
        return ligne;

    }

    public static String entete_ticket(String entete){
        String ent = entete;
        return ent;
    }
    public static String entete_ticket(Entete ent){
        String ligne = "";
        //ligne += ent.getNom_enseigne() + '\n';
        ligne += ent.getAdresse_enseigne() + '\n';
        ligne += ent.getTelephone() + '\n';
        ligne += ent.getInfo_ouverture() + '\n';
        ligne += ent.getHoraire() + '\n';
        ligne += ent.getAdresse_web() + '\n';
        return ligne;
    }
    public static String bas_de_page(Vendeur v, String infoticket){
        String ligne = "";
        ligne+=infoticket;
        String date = date();
        for(int i = 0; i<nb_lower-infoticket.length()-date.length();i++){
            ligne+=" ";
        }
        ligne+=date()+'\n';
        ligne+="Vendeur: "+ v.getUsual_name();
        return ligne;
    }
    public static String message_bas_de_page(String message){
        return message;
    }

    public String getDevise() {
        return devise;
    }
    public void setDevise(String devise) {
        this.devise = devise;
    }

    public static String date(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int today = cal.get(cal.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 0);

        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        mFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        String strDate = mFormat.format(cal.getTime());
        /*SimpleDateFormat mFormat = new SimpleDateFormat("HHmmssddMMyyyy");*/

        return strDate;
    }
}
