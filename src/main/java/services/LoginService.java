package services;

import dao.LoginDAO;
import jakarta.inject.Inject;
import model.Credential;
import model.User;

public class LoginService {
    @Inject
    private LoginDAO dao;


    public Credential getCredential(String username) {
        return dao.getCredential(username);
    }

    public boolean doLogin(User user, Credential credential) {
        return dao.doLogin(user, credential);
    }
}
