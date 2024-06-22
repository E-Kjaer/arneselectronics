package DAM.g1.domain.usermanagement;

import java.util.UUID;

public class AuthToken {
    
    private static AuthToken instance;
    private String token;

    private AuthToken(){
        this.token = null;
    }

    public static synchronized AuthToken getInstance(){
        if (instance==null){
            instance = new AuthToken();
            return instance;
        }else{
            return instance;
        }
    }

    public String getToken(){
        return token;
    }

    public void setToken(boolean in){
        if(in){
            this.token = UUID.randomUUID().toString();
        }else{
            this.token = null;
        }
    }


    public boolean isSet(){
        if (token!=null){
            return true;
        }else{
            return false;
        }
    }

}
