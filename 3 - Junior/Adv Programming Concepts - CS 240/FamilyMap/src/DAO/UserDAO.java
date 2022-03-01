package DAO;

import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Registers user, adds to database
     *
     * @param user User to be registered
     * @throws DataAccessException
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO Users (UserName, PassWord, Email, FirstName, LastName, Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting user into the database");
        }
    }

    /**
     * Attempts to find user with matching username
     *
     * @param username username of user to be found
     * @return User with matching name, or null if not found
     * @throws DataAccessException
     */
    public User find(String username) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE UserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String newUserName = rs.getString("UserName");
                String newPassWord = rs.getString("PassWord");
                String newEmail = rs.getString("Email");
                String newFirstName = rs.getString("FirstName");
                String newLastName = rs.getString("LastName");
                String newGender = rs.getString("Gender");
                String newPersonID = rs.getString("PersonID");

                return new User(newUserName, newPassWord, newEmail, newFirstName, newLastName, newGender, newPersonID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        }
        return null;
    }

    /**
     * Deletes specified user from table
     *
     * @param userName user to be deleted
     * @return true for a successful deletion, false for an error
     */
    public boolean delete(String userName) throws DataAccessException {
        String sql = "DELETE FROM Users " +
                "WHERE UserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting user #" + userName + " from database");
        }
        return true;
    }

    /**
     * Clears the table of all data
     *
     * @return true for a successful clear, false for an error
     */
    public boolean clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing Users");
        }
        return true;
    }
}
