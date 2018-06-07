package models;

public enum PlayerClass {
    Cartesien("Le Cartésien : étudiant rationnel, logique, méthodique, il n'est jamais en retard et cherche a avoir les meilleures résultat afin d'acquérir le plus de connaissance possible.", "cartesien.png"),
    Hedoniste("L'Hédoniste : étudiant priorisant les joie de la vie à l'étude, social, drole et bon vivant, cela ne l'empeche pas de réussir ces études malgrès son rythme de vie.", "hedoniste.png"),
    Cynique("Le Cynique : étudiant imprudent, éffronté meme parfois insultant. Il cherchera par tous les moyen de réussir au dépit des autres et par tous les bon et surtout mauvais moyens.", "cynique.png");

    private String description = "";
    private String image = "";

    PlayerClass(String description, String image) {
        this.description = description;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() { return image; }

    @Override
    public String toString() {
        return "PlayerClass{" +
                "description='" + description + '\'' +
                '}';
    }

    public static PlayerClass StringToEnum(String type){
        if(type.equals(Cartesien.name()))
            return Cartesien;
        else if(type.equals(Hedoniste.name()))
            return Hedoniste;
        else
            return Cynique;
    }

    public static String EnumToString(PlayerClass type){
        return type.name();
    }
}
