package de.schulzgette.thes_o_naise.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jessica on 19.11.2016.
 */

//TODO MEHR KATEGORIEN ?
public class KandidatenModel {
    String vorname;
    String nachname;
    String partei;
    String kid;
    String email;
    String wahlkreis;
    JSONArray beantworteteThesen;
    Integer Punkte_Insgesamt;
    Integer Punkte_Lokal;
    Integer Punkte_Umwelt;
    Integer Punkte_AP;
    Integer Punkte_Satire;
    JSONArray Begruendungen;
    JSONObject Biografie;
    JSONObject Wahlprogramm;


    public KandidatenModel(String kid, String vorname, String nachname,  String partei, String email,  String wahlkreis,  JSONArray beantworteteThesen, Integer punkte_Insgesamt,Integer punkte_Lokal, Integer punkte_Umwelt, Integer punkte_AP, Integer punkte_Satire, JSONArray Begruendungen, JSONObject Biografie, JSONObject Wahlprogramm) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.kid = kid;
        this.email = email;
        this.partei = partei;
        this.wahlkreis = wahlkreis;
        this.Punkte_Insgesamt = punkte_Insgesamt;
        this.beantworteteThesen = beantworteteThesen;
        this.Punkte_Lokal = punkte_Lokal;
        this.Punkte_Umwelt = punkte_Umwelt;
        this.Punkte_AP = punkte_AP;
        this.Punkte_Satire = punkte_Satire;
        this.Begruendungen = Begruendungen;
        this.Biografie = Biografie;
        this.Wahlprogramm = Wahlprogramm;
    }
    public String getGeburtsdatum() throws JSONException {
        String geburtsdatum = Biografie.getString("Geburtsdatum");
        return geburtsdatum;
    }
    public String getBildungsweg() throws JSONException {
        String bildungsweg = Biografie.getString("Bildungsweg");
        return bildungsweg;
    }
    public String getBerufe() throws JSONException {
        String berufe = Biografie.getString("Berufe");
        return berufe;
    }
    public String getMitgliedschaften() throws JSONException {
        String mitgliedschaften = Biografie.getString("Mitgliedschaften");
        return mitgliedschaften;
    }
    public String getWebseite() throws JSONException {
        String webseite = Wahlprogramm.getString("Webseite");
        return webseite;
    }
    public String getBeschreibung() throws JSONException {
        String beschreibung = Wahlprogramm.getString("Text");
        return beschreibung;
    }
    public String getLink() throws JSONException {
        String link = Wahlprogramm.getString("Link");
        return link;
    }

    public JSONArray getBegruendungen() {
        return Begruendungen;
    }

    public JSONObject getBiografie() {
        return Biografie;
    }

    public JSONObject getWahlprogramm() {
        return Wahlprogramm;
    }

    public String getNachname() {
        return nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public String getPartei() {
        return partei;
    }

    public String getKid() {
        return kid;
    }

    public String getEmail() {
        return email;
    }

    public String getWahlkreis() {
        return wahlkreis;
    }

    public JSONArray getBeantworteteThesen() {
        return beantworteteThesen;
    }

    public Integer getPunkte_Insgesamt() {
        return Punkte_Insgesamt;
    }

    public Integer getPunkte_Lokal() {
        return Punkte_Lokal;
    }

    public Integer getPunkte_Umwelt() {
        return Punkte_Umwelt;
    }

    public Integer getPunkte_AP() {
        return Punkte_AP;
    }

    public Integer getPunkte_Satire() {
        return Punkte_Satire;
    }

    public Integer getAnzahlThesenPositionen() {
        return beantworteteThesen.length();
    }

    public Integer getAnzahlPositionenZuThesenMitKategorie(String kategorie) {
        Integer result= 0;
        for (int i = 0; i<beantworteteThesen.length(); i++) {
            JSONObject object = null;
            String objectKategorie = null;
            try {
                object = (JSONObject) beantworteteThesen.get(i);
                objectKategorie = object.getString("KATEGORIE");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(objectKategorie.equals(kategorie)) result += 1;
        }
        return result;
    }

    public Double durchschnittlichePunkteProThesen(String kategorie){
        Double result = 0.0;
        if(kategorie.equals("punkte_insgesamt")){
            result = (double) getPunkte_Insgesamt()/getAnzahlThesenPositionen();
            result = Math.round(1000.0 * result) / 1000.0;
        }else if(kategorie.equals("Lokal")){
            result = (double) getPunkte_Lokal()/getAnzahlPositionenZuThesenMitKategorie("Lokal");
            result = Math.round(1000.0 * result) / 1000.0;
        }else if(kategorie.equals("Umwelt")){
            result = (double) getPunkte_Umwelt()/getAnzahlPositionenZuThesenMitKategorie("Umwelt");
            result = Math.round(1000.0 * result) / 1000.0;
        }else if(kategorie.equals("Aussenpolitik")){
            result = (double) getPunkte_AP()/getAnzahlPositionenZuThesenMitKategorie("Aussenpolitik");
            result = Math.round(1000.0 * result) / 1000.0;
        }else if(kategorie.equals("Satire")){
            result = (double) getPunkte_Satire()/getAnzahlPositionenZuThesenMitKategorie("Satire");
            result = Math.round(1000.0 * result) / 1000.0;
        }
        return result;
    }

}

