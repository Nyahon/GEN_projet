package models;

public enum ItemType {

    //Permet de regagner des points de vies.
    Biere("La bière, toujours là dans les moment difficile et pour ce changer les idées. " +
            "Ce breuvage vous apportera reconfort et vous permettra de faire durer le combat."),

    //Permet de cacher une mauvaise réponses.
    Livre("Le livre, puit de savoir et de connaissance, c'est dans les moments difficile qu'il pourrait vous apporter un soutient vers la bonne réponse."),

    //Permet de donner la bonne réponse mais en contre partie vous perdez de la vie.
    AntiSeche("L'Anti-sèche, Dur a utiliser mais fort utilise, si vous osez l'utiliser vous vous assurez la réussite.");

    private String description = "";

    ItemType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ItemType{" +
                "description='" + description + '\'' +
                '}';
    }
}
