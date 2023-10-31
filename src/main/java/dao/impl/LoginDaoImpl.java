package dao.impl;

import common.Configuration;
import common.Constants;
import dao.LoginDAO;
import jakarta.inject.Inject;
import model.Credential;
import model.User;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginDaoImpl implements LoginDAO {

    private DBConnection db;

    @Inject
    public LoginDaoImpl(DBConnection db) {
        this.db = db;
    }

    public Credential getCredential(String username) {
        Credential credential = null;
        try (Connection myConnection = db.getConnection();
             PreparedStatement stmt = myConnection.prepareStatement("SELECT * FROM credentials where user_name = ?")){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                credential = new Credential(rs.getInt("id"), rs.getString("user_name"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return credential;
    }

    @Override
    public boolean doLogin(User user) {
        Credential credential = getCredential(user.getNombre());
        if (credential == null)
            return false;
        else
            return user.getPassword().equals(credential.getPassword());
    }
}
