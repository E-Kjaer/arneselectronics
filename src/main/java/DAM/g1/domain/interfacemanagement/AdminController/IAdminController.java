package DAM.g1.domain.interfacemanagement.AdminController;

import java.util.ArrayList;

import DAM.g1.AppUser;
import DAM.g1.Asset;
import DAM.g1.Tag;

public interface IAdminController{

    //returns true for successful upload, and false if not (that is for INSERT, UPDATE or DELETE QUERIES)

    //upload specified asset (w/o tags)
    public boolean uploadAsset(Asset asset);
    //upload specified tag
    public boolean uploadTag(Tag tag);
    //tags to be added are to be contained within asset object (is the thought)
    public boolean addTags(Asset asset);
    //return all asset paths within DB.
    public ArrayList<Asset> listAssets();
    //return all tags within DB
    public ArrayList<Tag> listTags();
    //return all assets including all their tags
    public ArrayList<Asset> listAll();
    //delete tag from DB
    public boolean deleteTag(Tag tag);
    //delete asset from DB
    public boolean deleteAsset(Asset asset);
    //tags to be removed are to be contained within asset object
    public boolean removeTags(Asset asset);
    //log in specified user
    public boolean login(AppUser user);
    //log out user
    public boolean logout();

}