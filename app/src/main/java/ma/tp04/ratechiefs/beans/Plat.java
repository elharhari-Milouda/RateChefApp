package ma.tp04.ratechiefs.beans;

public class Plat {
    private int id;
    private String nom;
    private int img;
    private int chef;
    private String recette;
    private static int comp=0;

    public Plat(String nom, int img, int chef, String recette) {
        this.id = ++comp;
        this.nom = nom;
        this.img = img;
        this.chef = chef;
        this.recette = recette;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }
    public int getChef() {
        return chef;
    }
    public void setChef(int chef) {
        this.chef = chef;
    }
    public String getRecette() {
        return recette;
    }
    public void setRecette(String recette) {
        this.recette = recette;
    }
}
