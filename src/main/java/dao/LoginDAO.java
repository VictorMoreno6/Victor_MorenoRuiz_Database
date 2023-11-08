package dao;

import model.Credential;
import model.User;

public interface LoginDAO {
    boolean doLogin(User user, Credential credential);

    Credential getCredential(String username);
}
