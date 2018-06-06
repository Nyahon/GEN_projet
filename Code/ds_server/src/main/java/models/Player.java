package models;

import java.net.ConnectException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import server.PlayerConnectionHandler;

public class Player {

    public static final int INITIAL_PV = 100;
    public static final int INITIAL_XP = 0;
    public static final int INITIAL_LEVEL = 0;
    public static final String INITIAL_IMAGE = "player.png";

    private int id;
    private int annee;
    private int nbPV;
    private int nbXP;
    private String name;
    private int level;
    private String image;
    private Socket clientSocket = null;
    private PlayerConnectionHandler playerConnectionHandler;
    private boolean inFight = false;
    private LinkedList<Question> questions;
    private LinkedList<Item> items;
    private PlayerClass type;

    private BlockingQueue<String> fightMessageIn = new ArrayBlockingQueue<>(500, true);
    private BlockingQueue<String> fightMessageOut = new ArrayBlockingQueue<>(500, true);

    public Player(String name, int annee, int pv, int niveau, int xp, PlayerClass type, String image) {
        this.name = name;
        this.nbPV = pv;
        this.nbXP = xp;
        this.level = niveau;
        this.annee = annee;
        this.image = image;
        questions = new LinkedList<>();
        this.type = type;
        this.items = new LinkedList<>();

    }


    public boolean IsLive() {
        return nbPV > 0;
    }

    // ==========================================================
    // GETTERS SETTERS
    // ==========================================================
    public int getNbPV() {
        return nbPV;
    }

    public void setNbPV(int nbPV) {
        this.nbPV = nbPV;
    }

    public void loosePV(int nbPV) {
        this.nbPV -= nbPV;
    }

    public int getNbXP() {
        return nbXP;
    }

    public void setNbXP(int nbXP) {
        this.nbXP = nbXP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setQuestions(LinkedList<Question> questions) {
        this.questions = questions;
    }

    public void addQuestions(Question question) {
        if(questions.isEmpty()) {
            ConnectionDB.assignQuestion(question.getId(), id);
        }
        else {
            boolean addQuestion = true;
            for (Question q : questions) {
                if (q.getId() == question.getId()) {
                    addQuestion = false;
                    break;
                }
            }
            if(addQuestion)
                ConnectionDB.assignQuestion(question.getId(), id);
        }
        questions = ConnectionDB.getQuestionByPlayer(id);
    }

    public int getId() {
        return id;
    }

    public LinkedList<Question> getQuestions() {
        return this.questions;

    }

    public String getFightMessageIn() throws InterruptedException {
        return fightMessageIn.take();
    }

    public void setFightMessageIn(String message) throws InterruptedException {
        fightMessageIn.put(message);
    }

    public String getFightMessageOut() throws InterruptedException {
        return fightMessageOut.take();
    }

    public void setFightMessageOut(String message) throws InterruptedException {
        fightMessageOut.put(message);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setPlayerConnectionHandler(PlayerConnectionHandler handler) {
        this.playerConnectionHandler = handler;
    }


    public boolean getInFight() {
        return inFight;
    }

    public void setInFight(boolean bool) {
        inFight = bool;
    }

    public int getAnnee() {
        return annee;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerClass getType() {
        return type;
    }

    public void UseItem(Item item, Question question){
        switch (item.getType()){
            case Biere:
                nbPV += 30;
                break;
            case Livre:
                question.setReponseFalse2("##################");
                break;
            case AntiSeche:
                nbPV -=20;
                question.setReponseFalse1("##################");
                question.setReponseFalse2("##################");
                question.setReponseFalse3("##################");
                break;
        }
    }

    public void setItems (LinkedList<Item> items){
        this.items = items;
    }

    public void addItem (Item item){
        this.items.add(item);
    }

    public LinkedList<Item> getItems(){
        return this.items;
    }

    public Item getItem(ItemType itemType){
        Item tmp = null;
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getType().equals(itemType)){
                tmp = items.get(i);
                items.remove(i);
                break;
            }
        }
        return tmp;
    }

    public int getNbItem(ItemType itemType){
        int count = 0;
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getType().equals(itemType)){
                count++;
            }
        }
        return count;
    }

    public void initQuestionPlayer(){

        Question question1 = ConnectionDB.getQuestionById(1);
        Question question2 = ConnectionDB.getQuestionById(2);
        Question question3 = ConnectionDB.getQuestionById(3);
        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        for(Question q : questions)
            ConnectionDB.assignQuestion(this.id, q.getId());
    }

    public void initItemsPlayer(){

        switch (type){
            case Cynique:
                items.add(new Item(ItemType.AntiSeche));
                items.add(new Item(ItemType.AntiSeche));
                items.add(new Item(ItemType.AntiSeche));
                break;
            case Cartesien:
                items.add(new Item(ItemType.Livre));
                items.add(new Item(ItemType.Livre));
                items.add(new Item(ItemType.Livre));
                break;
            case Hedoniste:
                items.add(new Item(ItemType.Biere));
                items.add(new Item(ItemType.Biere));
                items.add(new Item(ItemType.Biere));
                break;
        }
            ConnectionDB.assignItems(items,this.id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", annee=" + annee +
                ", nbPV=" + nbPV +
                ", nbXP=" + nbXP +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", image=" + image +
                ", clientSocket=" + clientSocket +
                ", playerConnectionHandler=" + playerConnectionHandler +
                ", inFight=" + inFight +
                ", questions=" + questions +
                ", items=" + items +
                ", type=" + type +
                ", fightMessageIn=" + fightMessageIn +
                ", fightMessageOut=" + fightMessageOut +
                '}';
    }


}
