package DAO;

import Models.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO {
    private Connection conn;

    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts new AuthToken to table
     *
     * @param at AuthToken to be inserted
     * @return true if successful, false if rollback is desired
     * @throws DataAccessException
     */
    public void insert(AuthToken at) throws DataAccessException {
        String sql = "INSERT INTO AuthTokens (UserName, Token) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, at.getUserName());
            stmt.setString(2, at.getToken());
            stmt.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting authToken into the database");
        }
    }

    /**
     * Searches for AuthToken by its unique code
     *
     * @param token AuthToken to be found
     * @return AuthToken which matches the queried token, or null if not found
     * @throws DataAccessException
     */
    public AuthToken find(String token) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE Token = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String newUserName = rs.getString("UserName");
                String newToken = rs.getString("Token");
                return new AuthToken(newUserName, newToken);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        }
        return null;
    }

    /**
     * Clears the table of all data
     *
     * @return true if successfully cleared
     */
    public boolean clear() throws Exception {
        String sql = "DELETE FROM AuthTokens";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing Users");
        }
        return true;
    }
}
