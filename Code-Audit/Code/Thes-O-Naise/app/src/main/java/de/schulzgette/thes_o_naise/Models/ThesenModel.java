package de.schulzgette.thes_o_naise.Models;

import org.json.JSONArray;

/**
 * Created by Jessica on 02.11.2016.
 */

public class ThesenModel {


    String thesentext;
    Integer likes;
    Integer Pro;
    Integer Neutral;
    Integer Contra;
    JSONArray KPro;
    JSONArray KNeutral;
    JSONArray KContra;
    JSONArray WPro;
    JSONArray WNeutral;
    JSONArray WContra;
    JSONArray KPosition;
    String TID;
    String Kategorie;
    String Wahlkreis;


    public ThesenModel(String TID, String thesentext, String Kategorie, String Wahlkreis, Integer likes, Integer Pro, Integer Neutral, Integer Contra, JSONArray KPro, JSONArray KNeutral, JSONArray KContra, JSONArray WPro, JSONArray WNeutral, JSONArray WContra, JSONArray KPosition) {
        this.TID = TID;
        this.thesentext = thesentext;
        this.likes = likes;
        this.Kategorie = Kategorie;
        this.Wahlkreis = Wahlkreis;
        this.Pro = Pro;
        this.Neutral = Neutral;
        this.Contra = Contra;
        this.KPro = KPro;
        this.KNeutral = KNeutral;
        this.KContra = KContra;
        this.WPro = WPro;
        this.WNeutral = WNeutral;
        this.WContra = WContra;
        this.KPosition = KPosition;
    }

    public String getThesentext() {
        return thesentext;
    }

    public String getTID() {
        return TID;
    }

   public Integer getLikes() {
       return likes;
    }

    public Integer getPro() {
        return Pro;
    }
    public Integer getNeutral() {
        return Neutral;
    }
    public Integer getContra() {
        return Neutral;
    }

    public JSONArray getKPro() {
        return KPro;
    }

    public JSONArray getKNeutral() {
        return KNeutral;
    }

    public JSONArray getKContra() {
        return KContra;
    }

    public JSONArray getWPro() {
        return WPro;
    }

    public JSONArray getWNeutral() {
        return WNeutral;
    }

    public JSONArray getWContra() {
        return WContra;
    }

    public JSONArray getKPosition() {
        return KPosition;
    }

    public String getWahlkreis(){
        return Wahlkreis;
    }

    public String getKategorie(){
        return Kategorie;
    }
}
