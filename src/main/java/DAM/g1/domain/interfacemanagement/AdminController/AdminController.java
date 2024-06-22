package DAM.g1.domain.interfacemanagement.AdminController;
import java.util.ArrayList;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;

import DAM.g1.AppUser;
import DAM.g1.Asset;
import DAM.g1.Tag;
import DAM.g1.data.AccessDatabase;
import DAM.g1.data.NoRowsAffectedException;
import DAM.g1.data.TransactionManager;
import DAM.g1.domain.usermanagement.Authenticator;
import DAM.g1.data.AccessDatabase;


public class AdminController implements IAdminController{
    
    public AdminController(AccessDatabase accDB){
        this.accDB = accDB;
        this.auth = new Authenticator(accDB);
        this.transactionManager = accDB.getTransactionManager();
    }

    private AccessDatabase accDB;
    private Authenticator auth;
    private TransactionManager transactionManager;

    public Authenticator getAuthenticator(){
        return auth;
    }

    @Override
    public boolean uploadAsset(Asset asset) {
        try {
            accDB.uploadAsset(asset);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean uploadTag(Tag tag) {
        try {
            accDB.uploadTag(tag);
        } catch (NoRowsAffectedException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addTags(Asset asset) {
        try {
            accDB.begin();
            for(Tag tag : asset.getTags()){
                /*might  facilite some errors if say 3/5 tags are added and maybe the fourth is already in the table causing a NoRowsAffected exception, which returns false, 
                however some operations on the db might have worked. So be sure to either reload UI (maybe a user task) to see which are added and which aren't, or make it
                impossible for user to add a tag to an image which already has it 1/2
                */
                /* Might be fixed now bc of transactionmanager implementation, please do test however lol */
                /*Yea its fixed now */

                accDB.addTagToAsset(asset, tag);
            }
            accDB.end();
        } catch (NoRowsAffectedException e) {
            accDB.rollback();
            return false;
        }
        return true;
    }


    @Override
    public ArrayList<Asset> listAssets() {
        return accDB.getAllAssets();
    }

    @Override
    public ArrayList<Tag> listTags() {
        return accDB.getAllTags();
    }

    @Override
    public ArrayList<Asset> listAll() {
        ArrayList<Asset> assets = accDB.getAllAssets();
        for(Asset asset : assets){
            asset.setTags(accDB.getTagsFromAsset(asset));
        }
        return assets;
    }

    @Override
    public boolean deleteTag(Tag tag) {
        try {
            accDB.deleteTag(tag);
        } catch (NoRowsAffectedException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteAsset(Asset asset) {
        try {
            accDB.deleteAsset(asset);
        } catch (NoRowsAffectedException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean removeTags(Asset asset) {
        ArrayList<Tag> tags = asset.getTags();
        try {
            accDB.begin();
            for (Tag tag: tags){
                accDB.removeTagFromAsset(asset, tag);
            }
            accDB.end();
        } catch (NoRowsAffectedException e) {
            e.printStackTrace();
            accDB.rollback();
            return false;
        }
        return true;
    }

    @Override
    public boolean login(AppUser user) {
        if (auth.login(user.getUsername(), user.getPassword())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean logout() {
        auth.logout();
        return true;
    }

    
}
