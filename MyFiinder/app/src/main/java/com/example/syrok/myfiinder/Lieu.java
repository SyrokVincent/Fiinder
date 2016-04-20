package com.example.syrok.myfiinder;

/**
 * Classe de Lieu qui récupèrera les données de l'API Google
 * et en particulier la longitude et la latitude
 * La distance pourra être calculée par nous-même dans le pire des cas
 */
public class Lieu implements Comparable{
    private String nom;
    private int distance;
    private String type;
    private Float note;
    private double longitude;
    private double latitude;

    public Lieu(String n, int d, String t, float no, double ln, double la){
        this.nom = n;
        this.distance = d;
        this.type = t;
        this.note = no;
        this.longitude = ln;
        this.latitude = la;
    }
    public Lieu(String n, int d, String t, float no){
        this.nom = n;
        this.distance = d;
        this.type = t;
        this.note = no;
        this.longitude = 0;
        this.latitude = 0;
    }
    public String getNom(){ return nom; }
    public int getDistance(){return distance;}
    public String getType(){return  type;}
    public Float getNote(){ return note; }
    public double getLongitude(){ return longitude;}
    public double getLatitude(){return latitude;}

    public void setNom(String s){nom = s;}
    public void setDistance(int i){ distance = i;}
    public void setType(String s){type = s;}
    public void setNote(Float f){note = f;}


    /**
     * Permet par la suite de pouvoir trier une liste de lieux en fonction de leur distance à nous
     * @param l Le lieu à comparer
     * @return un nombre négatif si this est plus proche, et négatif si this est plus loin
     */
    @Override
    public int compareTo(Object l) {
        Lieu l2 = (Lieu) l;
        return this.getDistance()-l2.getDistance();
    }
}
