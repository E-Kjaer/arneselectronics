package DAM.g1.domain.usermanagement;

import javax.security.auth.login.LoginException;

import DAM.g1.AppUser;
import DAM.g1.data.AccessDatabase;

public class Authenticator {

    private AccessDatabase accDB;
    private AuthToken token = AuthToken.getInstance();

    public Authenticator(AccessDatabase accDB) {
        this.accDB = accDB;
    }

    public boolean login(String username, String password) {
        try {
            AppUser user = accDB.getUser(username, password);
            if (user != null) {
                token.setToken(true);
                return true; 
            } else {
                return false; 
            }
        } catch (LoginException e) {
            return false;
        }
    }

    public void logout(){
        token.setToken(false);
    }

    public AuthToken getToken(){
        return token;
    }

    public boolean hasAccess() {
        return token.isSet();
    }
}
