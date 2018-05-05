package models;

import models.db_models.db_Player;
import models.db_models.db_Question;
import sun.awt.image.ImageWatched;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionDB {

    private final static Logger LOG = Logger.getLogger(ConnectionDB.class.getName());

    private static final String DB_URL = "jdbc:sqlite:src/main/ds_db";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Connection connection = null;

    public static Connection getDBConnection() {
        if (null == connection)
            connection = connect();
        return connection;
    }

    public static boolean closeConnection() {
        if (null == connection)
            return false;
        try {
            connection.close();
            LOG.log(Level.INFO, "Database closed");
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "FAILED TO DISCONNECT DATABASE : " + e.getMessage(), e);
        }
        return true;
    }

    public static boolean isClosed() {
        boolean result = false;
        try {
            result = connection.isClosed();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    /**
     * Simple connection method
     */
    private static Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            if (connection != null) {
                LOG.log(Level.INFO, "Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) connection.getMetaData();
                LOG.log(Level.INFO, "Driver name: " + dm.getDriverName());
                LOG.log(Level.INFO, "Driver version: " + dm.getDriverVersion());
                LOG.log(Level.INFO, "Product name: " + dm.getDatabaseProductName());
                LOG.log(Level.INFO, "Product version: " + dm.getDatabaseProductVersion());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return connection;
    }


    public static void closeRessources(Connection c, Statement stmt, ResultSet rs){
        try {
            rs.close();
            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertJoueur(String nom, int annee){
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            c = DriverManager.getConnection(DB_URL);
            System.out.println("Base de donnee ouverte");

            String sql = "INSERT INTO joueurs VALUES( ?, ?, ?)";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setInt(2, annee);
            stmt.executeUpdate();
            System.out.println("Nouveau joueur insere");

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                stmt.close();
                //c.commit();
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static db_Player getJoueurByName(String nom) {
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        db_Player player = null;
        try {
            c = DriverManager.getConnection(DB_URL);
            stmt = c.createStatement();
            System.out.println("Base de données ouverte");

            rs = stmt.executeQuery("SELECT * FROM joueur WHERE nom = " + nom + ";");

            while (rs.next()) {
                String hisName = rs.getString("Nom");
                int annee = rs.getInt("Annee");
                int id = rs.getInt("Id");

                player = new db_Player(id, nom, annee);
                System.out.println(player);
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeRessources(c, stmt, rs);
            return player;
        }
    }

    public static void insertQuestion(String question, String reponseOK, String reponseFalse1, String reponseFalse2
    , String reponseFalse3){
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            c = DriverManager.getConnection(DB_URL);
            System.out.println("Base de donnee ouverte");

            String sql = "INSERT INTO question VALUES( ?, ?, ?, ?, ?, ?)";
            stmt = c.prepareStatement(sql);
            stmt.setString(2, question);
            stmt.setString(3, reponseOK);
            stmt.setString(4, reponseFalse1);
            stmt.setString(5, reponseFalse2);
            stmt.setString(6, reponseFalse3);
            stmt.executeUpdate();
            System.out.println("Nouvelle question insere");

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                stmt.close();
                //c.commit();
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void assignQuestion(int idQuestion, int idPlayer){
        Connection c = null;
        PreparedStatement stmt = null;

        try {
            c = DriverManager.getConnection(DB_URL);
            System.out.println("Base de donnee ouverte");

            String sql = "INSERT INTO possede VALUES( ?, ?, ?)";
            stmt = c.prepareStatement(sql);
            stmt.setInt(2, idPlayer);
            stmt.setInt(3, idQuestion);
            stmt.executeUpdate();
            System.out.println("question " + idQuestion + " est assignee a "+ idPlayer);

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                stmt.close();
                //c.commit();
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static LinkedList<db_Question> getQuestionByPlayer(int idPlayer) {
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        LinkedList<db_Question> questions = new LinkedList<>();
        try {
            c = DriverManager.getConnection(DB_URL);
            stmt = c.createStatement();
            System.out.println("Base de données ouverte");

            rs = stmt.executeQuery("SELECT question.Id, question.Question, question.ReponseCorrecte," +
                    " question.ReponseFausse1, question.ReponseFausse2, question.ReponseFausse3 FROM question " +
                    "INNER JOIN possede ON question.Id = possede.IdQuestion WHERE IdJoueur = " + idPlayer + ";");

            while (rs.next()) {
                String question = rs.getString("Question");
                String reponseCorrecte = rs.getString("ReponseCorrecte");
                String reponseFausse1 = rs.getString("ReponseFausse1");
                String reponseFausse2 = rs.getString("ReponseFausse2");
                String reponseFausse3 = rs.getString("ReponseFausse3");
                int id = rs.getInt("Id");

                db_Question db_question = new db_Question(id,question, reponseCorrecte, reponseFausse1, reponseFausse2, reponseFausse3);
                questions.add(db_question);
                System.out.println(db_question);
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeRessources(c, stmt, rs);
            return questions;
        }
    }

}
