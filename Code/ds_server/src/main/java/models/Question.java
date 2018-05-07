package models;

public class Question {
    private int id;
    private String question;
    private String reponseOK;
    private String reponseFalse1;
    private String ReponseFalse2;
    private String ReponseFalse3;

    public Question(int id, String question, String reponseOK, String reponseFalse1, String reponseFalse2, String reponseFalse3) {
        this.id = id;
        this.question = question;
        this.reponseOK = reponseOK;
        this.reponseFalse1 = reponseFalse1;
        ReponseFalse2 = reponseFalse2;
        ReponseFalse3 = reponseFalse3;
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

    public String getReponseOK() {
        return reponseOK;
    }

    public void setReponseOK(String reponseOK) {
        this.reponseOK = reponseOK;
    }

    public String getReponseFalse1() {
        return reponseFalse1;
    }

    public void setReponseFalse1(String reponseFalse1) {
        this.reponseFalse1 = reponseFalse1;
    }

    public String getReponseFalse2() {
        return ReponseFalse2;
    }

    public void setReponseFalse2(String reponseFalse2) {
        ReponseFalse2 = reponseFalse2;
    }

    public String getReponseFalse3() {
        return ReponseFalse3;
    }

    public void setReponseFalse3(String reponseFalse3) {
        ReponseFalse3 = reponseFalse3;
    }

    @Override
    public String toString() {
        return "db_Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", reponseOK='" + reponseOK + '\'' +
                ", reponseFalse1='" + reponseFalse1 + '\'' +
                ", ReponseFalse2='" + ReponseFalse2 + '\'' +
                ", ReponseFalse3='" + ReponseFalse3 + '\'' +
                '}';
    }
}
