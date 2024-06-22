package DAM.g1.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class SetupConnection {
    

    public SetupConnection(){
        this.path = "src/main/resources/DAM/db-config.properties";
        //this.path = "damsystem\\src\\main\\resources\\db-config.properties";
        init();
    }
    public SetupConnection(String path){
        this.path = path;
        init();
    }
    
    private String path;
    private InputStream input;
    private Properties props;
    private String username;
    private String password;
    private String host;
    private String port;
    private String database;
    private Connection connection;

    private void init(){
        try {
            this.input = new FileInputStream(path);
            props = new Properties();
            this.props.load(input);
        } catch (IOException e) {

            e.printStackTrace();

        }

        this.database = this.props.getProperty("db-name");
        this.username = this.props.getProperty("db-username");
        this.password = this.props.getProperty("db-password");
        this.host = this.props.getProperty("db-host");
        this.port = this.props.getProperty("db-port");
    }

    public Connection getConnection(){

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

        try {

            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public String toString(){
        return connection.toString();
    }

}
