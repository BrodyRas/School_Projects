package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by westenm on 2/4/19.
 */

public class Database {
    private Connection conn;

    static {
        try {
            //This is how we set up the driver for our database
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The pathing assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {
        openConnection();

        try (Statement stmt = conn.createStatement()) {
            String sql =
                    "DROP TABLE IF EXISTS Persons;" +
                            "DROP TABLE IF EXISTS Users;" +
                            "DROP TABLE IF EXISTS Events;" +
                            "DROP TABLE IF EXISTS AuthTokens;" +
                            "CREATE TABLE Persons " +
                            "(" +
                            "PersonID TEXT NOT NULL UNIQUE, " +
                            "Descendant TEXT NOT NULL, " +
                            "FirstName TEXT NOT NULL, " +
                            "LastName TEXT NOT NULL, " +
                            "Gender TEXT NOT NULL, " +
                            "Father TEXT, " +
                            "Mother TEXT, " +
                            "Spouse TEXT, " +
                            "PRIMARY KEY (PersonID), " +
                            "FOREIGN KEY (Descendant) references Users(Username), " +
                            "FOREIGN KEY (PersonID) references Persons(PersonID)" +
                            ");" +
                            "CREATE TABLE Users" +
                            "(" +
                            "UserName TEXT NOT NULL UNIQUE, " +
                            "PassWord TEXT NOT NULL, " +
                            "Email TEXT NOT NULL, " +
                            "FirstName TEXT NOT NULL, " +
                            "LastName TEXT NOT NULL, " +
                            "Gender TEXT NOT NULL, " +
                            "PersonID TEXT NOT NULL, " +
                            "PRIMARY KEY (UserName)" +
                            ");" +
                            "CREATE TABLE Events" +
                            "(" +
                            "EventID TEXT NOT NULL UNIQUE, " +
                            "Descendant TEXT NOT NULL, " +
                            "PersonID TEXT NOT NULL, " +
                            "Latitude REAL NOT NULL, " +
                            "Longitude REAL NOT NULL, " +
                            "Country TEXT NOT NULL, " +
                            "City TEXT NOT NULL, " +
                            "EventType TEXT NOT NULL, " +
                            "Year INT NOT NULL, " +
                            "PRIMARY KEY (EventID)" +
                            ");" +
                            "CREATE TABLE AuthTokens" +
                            "(" +
                            "UserName TEXT NOT NULL, " +
                            "Token TEXT NOT NULL UNIQUE, " +
                            "PRIMARY KEY (Token)" +
                            ");";

            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while creating tables");
        }
    }

    public void clearTables() throws DataAccessException {
        openConnection();

        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM Persons;" +
                    "DELETE FROM Users;" +
                    "DELETE FROM Events;" +
                    "DELETE FROM AuthTokens;";
            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
