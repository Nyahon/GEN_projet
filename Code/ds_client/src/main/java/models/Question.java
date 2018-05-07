package models;

public class Question {
    private int id;
    private String question;
    private String repA;
    private String repB;
    private String repC;
    private String repD;

    public Question(int id, String question, String repA, String repB, String repC, String repD) {
        this.id = id;
        this.question = question;
        this.repA = repA;
        this.repB = repB;
        this.repC = repC;
        this.repD = repD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {

        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getRepA() {
        return repA;
    }

    public void setRepA(String repA) {
        this.repA = repA;
    }

    public String getRepB() {
        return repB;
    }

    public void setRepB(String repB) {
        this.repB = repB;
    }

    public String getRepC() {
        return repC;
    }

    public void setRepC(String repC) {
        this.repC = repC;
    }

    public String getRepD() {
        return repD;
    }

    public void setRepD(String repD) {
        this.repD = repD;
    }

}
