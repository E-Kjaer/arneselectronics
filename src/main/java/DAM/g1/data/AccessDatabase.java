package DAM.g1.data;

import java.sql.*;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import DAM.g1.AppUser;
import DAM.g1.Asset;
import DAM.g1.Tag;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class AccessDatabase {

    public AccessDatabase(){
        this.stp = new SetupConnection();
        connection = stp.getConnection();
        this.transactionManager = new TransactionManager(connection);
    }

    public AccessDatabase(SetupConnection stp){
        this.stp = stp;
        connection = stp.getConnection();
        this.transactionManager = new TransactionManager(connection);
    }

    private String hash(String inputPassword){
        StringBuilder buildHexString = new StringBuilder();
        try {
            MessageDigest encryption = MessageDigest.getInstance("SHA-256");
            byte[] bytes = encryption.digest(inputPassword.getBytes());
            for(byte b : bytes){
                String hexVal = Integer.toHexString(0xff & b);
                if(hexVal.length()==1){
                    buildHexString.append('0');
                }
                buildHexString.append(hexVal);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return buildHexString.toString();
    }

    private SetupConnection stp;

    private TransactionManager transactionManager;

    private Connection connection;

    public TransactionManager getTransactionManager(){
        return transactionManager;
    }

    public boolean isConnected(){
        if(connection==null){
            return false;
        }
        return true;
    }

    public void rollback(){
        transactionManager.rollbackTransaction();
    }
    public void begin(){
        transactionManager.beginTransaction();
    }
    public void end(){
        transactionManager.endTransaction();
    }

    private void cleanup(PreparedStatement stmt){
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Asset getImageByProductId(int productId) {
        PreparedStatement statement = null;
        try {
            String query = "SELECT image_id, path FROM image_products JOIN images ON image_id = images.id WHERE product_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet rs = statement.executeQuery();
            ArrayList<Asset> assets = new ArrayList<>();
            while (rs.next()) {
                int imageId = rs.getInt("image_id");
                String path = rs.getString("path");
                Asset asset = new Asset(imageId, path, 0);
                assets.add(asset);
            }
            return assets.get(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public AppUser getUser(String username, String password) throws LoginException {
        PreparedStatement statement = null;
        try {
            String query = "SELECT * FROM users WHERE name = ? AND password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, hash(password));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String pwd = rs.getString("password");
                if (name != null && pwd != null) {
                    return new AppUser(name, pwd);
                } else {
                    // Handle the case where name or password is null
                    throw new LoginException("Name or password is null");
                }
            } else {
                throw new LoginException("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally{
            cleanup(statement);
        }
        return null;
    }

    public void closeConnection(){
        try {
            if (connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Connection closed succesfully");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection" + e.getMessage());
        }
    }

    public void uploadAsset(Asset asset) throws NoRowsAffectedException{
        PreparedStatement stmt = null;
        try {
            String query = "INSERT INTO images (path,original_ref) VALUES (?,?)";

            stmt = connection.prepareStatement(query);

            stmt.setString(1, asset.getPath());
            if(asset.getOriginal()==0){
                stmt.setNull(2, 0);
            }else{
                stmt.setInt(2, asset.getOriginal());
            }

            if(stmt.executeUpdate()<=0){
                throw new NoRowsAffectedException("No rows affected");
            }

            stmt.close();

        } catch(SQLException e){
            throw new NoRowsAffectedException("No rows affected");
        } finally{
            cleanup(stmt);
        }
    }

    
    public void uploadTag(Tag tag) throws NoRowsAffectedException{
        PreparedStatement stmt = null;
        try {
            String query = "INSERT INTO tags (key,value) VALUES (?,?)";

            stmt = connection.prepareStatement(query);

            stmt.setString(1, tag.getKey());
            stmt.setString(2, tag.getValue());

            if(stmt.executeUpdate()<=0){
                throw new NoRowsAffectedException("No rows affected");
            }

            stmt.close();

        } catch (SQLException e) {
            throw new NoRowsAffectedException("No rows affected");
        }  finally{
            cleanup(stmt);
        }
    }

    public ArrayList<Tag> getTagsFromAsset(Asset asset){
        PreparedStatement stmt = null;
        ArrayList<Tag> returnList = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT tags.id,tags.key,tags.value FROM tags JOIN image_tags AS it ON tags.id=it.tag_id JOIN images ON images.id=it.image_id AND images.id=?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, asset.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                returnList.add(new Tag(rs.getInt(1), rs.getString(2),rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            cleanup(stmt);
        } 
        return returnList;
    }

    public ArrayList<Asset> getAssetsFromTags(ArrayList<Tag> tags){
        ArrayList<Asset> returnList = new ArrayList<>();
        PreparedStatement stmt = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT images.id,images.path,images.original_ref FROM images");
            
            for (int i = 0;i<tags.size();i++){

                sb.append(" JOIN image_tags AS it" + i + " ON images.id=it" + i + ".image_id JOIN tags AS t" + i + " ON t" + i + ".id=it" + i + ".tag_id AND t" + i + ".value = ?");
            }

            stmt = connection.prepareStatement(sb.toString());

            for(int i = 1;i<=tags.size();i++){

                stmt.setString(i, tags.get(i-1).getValue());

            }

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                returnList.add(new Asset(rs.getInt(1),rs.getString(2),rs.getInt(3),tags));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            cleanup(stmt);
        }
        return returnList;
    }

    public void addTagToAsset(Asset asset, Tag tag) throws NoRowsAffectedException{
        PreparedStatement stmt = null;
        try {
            String query = "INSERT INTO image_tags (image_id,tag_id) VALUES (?,?)";

            stmt = connection.prepareStatement(query);

            stmt.setInt(1,asset.getId());
            stmt.setInt(2,tag.getId());

            if(stmt.executeUpdate()<=0){
                throw new NoRowsAffectedException("No rows affected");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new NoRowsAffectedException("No rows affected");
        }finally{
            cleanup(stmt);;
        }
    }

    public ArrayList<Asset> getAllAssets(){
        ArrayList<Asset> assets = new ArrayList<Asset>();
        PreparedStatement stmt = null;
        try {
            String query = "SELECT * FROM images";
            stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                assets.add(new Asset(rs.getInt(1),rs.getString(2),rs.getInt(3)));
            }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            cleanup(stmt);
        }
        return assets;
    }

    public ArrayList<Tag> getAllTags(){
        PreparedStatement stmt = null;
        ArrayList<Tag> tags = new ArrayList<Tag>();
        try {
            String query = "SELECT * FROM tags";
            stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                tags.add(new Tag(rs.getInt(1),rs.getString(2),rs.getString(3)));
            }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            cleanup(stmt);
        }
        return tags;
    }

    public void deleteAsset(Asset asset) throws NoRowsAffectedException{
        PreparedStatement stmt = null;
        try {
            String query = "DELETE FROM images WHERE id=?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, asset.getId());
            if(stmt.executeUpdate()<=0){
                throw new NoRowsAffectedException("No rows affected");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NoRowsAffectedException("No rows affected");
        } finally{
            cleanup(stmt);
        }
    }

    public void deleteTag(Tag tag) throws NoRowsAffectedException{
        PreparedStatement stmt = null;
        try {
            String query = "DELETE FROM tags WHERE id=?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, tag.getId());
            if(stmt.executeUpdate()<=0){
                throw new NoRowsAffectedException("No rows affected");
            }
            stmt.close();
        } catch (SQLException e) {
            throw new NoRowsAffectedException("No rows affected");
        } finally{
            cleanup(stmt);
        }
    }

    public void removeTagFromAsset(Asset asset, Tag tag) throws NoRowsAffectedException{
        PreparedStatement stmt = null;
        try {
            String query = "DELETE FROM image_tags WHERE image_id=? AND tag_id=?";

            stmt = connection.prepareStatement(query);

            stmt.setInt(1, asset.getId());
            stmt.setInt(2,tag.getId());

            if(stmt.executeUpdate()<=0){
                throw new NoRowsAffectedException("No rows affected");
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NoRowsAffectedException("No rows affected");
        } finally {
            cleanup(stmt);
        }
    }
    


    

}
