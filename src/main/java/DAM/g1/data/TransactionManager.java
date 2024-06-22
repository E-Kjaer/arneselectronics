package DAM.g1.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import DAM.g1.Asset;
import DAM.g1.Tag;

public class TransactionManager {
    
    TransactionManager(Connection connection){
        this.connection = connection;
    }

    Connection connection;

    public void beginTransaction(){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void endTransaction(){
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction(){
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void rollbackTransactionTag(){
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            PreparedStatement alterSeq = connection.prepareStatement("ALTER SEQUENCE tags_id_seq RESTART WITH 1");
            alterSeq.executeUpdate();
            alterSeq.close();
            PreparedStatement update = connection.prepareStatement("UPDATE tags SET id = DEFAULT");
            update.executeUpdate();
            update.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void rollbackTransactionAsset(){
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            PreparedStatement alterSeq = connection.prepareStatement("ALTER SEQUENCE images_id_seq RESTART WITH 1");
            alterSeq.executeUpdate();
            alterSeq.close();
            PreparedStatement update = connection.prepareStatement("UPDATE images SET id = DEFAULT");
            update.executeUpdate();
            update.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
