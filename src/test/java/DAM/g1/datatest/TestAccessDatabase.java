package DAM.g1.datatest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.security.auth.login.LoginException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DAM.g1.AppUser;
import DAM.g1.Asset;
import DAM.g1.Tag;
import DAM.g1.data.AccessDatabase;
import DAM.g1.data.NoRowsAffectedException;
import DAM.g1.data.SetupConnection;

import org.junit.jupiter.api.Assertions;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TestAccessDatabase {

    //it is essential that the test database is setup correctly to facilitate correct testing environment
    /*makes these querys on the database to fulfill requirements for testing (given the tables are created from the 
    db.sql in the resources folder) (some items may be redundant, but please insert all elements to be sure):

     *  INSERT INTO images VALUES (DEFAULT,'/some/path/1',null),(DEFAULT,'/some/path/2',null),(DEFAULT,'/some/path/3',null),(DEFAULT,'/some/path/4',null),(DEFAULT,'/some/path/5',null),(DEFAULT,'pathThatExistsInDB',null)
     *  INSERT INTO tags VALUES (DEFAULT,'TAG','1'),(DEFAULT,'TAG','2'),(DEFAULT,'TAG','3'),(DEFAULT,'TAG','4'),(DEFAULT,'TAG','5'),(DEFAULT,'tagThatExists','InDB')
     *  INSERT INTO image_tags VALUES (1,1),(1,2),(1,3),(1,4),(2,5),(3,5),(2,2),(3,2)
     *  INSERT INTO users VALUES (DEFAULT,'admin','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');
     * 
     *  maybe use these:
     *  ALTER SEQUENCE images_id_seq RESTART WITH 1;
        UPDATE images SET id = DEFAULT;
        ALTER SEQUENCE tags_id_seq RESTART WITH 1;
        UPDATE tags SET id = DEFAULT;
        given the serial id is not functioning properly

     * 
     *  make sure syntax is correct when inserting might be a mistake or two
    */

    //a lot of the methods from accessdatabase class need only an id to perform operations, hence a lot of random text strings in objects

    private SetupConnection setupConnection;
    private AccessDatabase accDb;

    //Set up test database with alternate db
    @BeforeEach
    public void setUp(){
        setupConnection = new SetupConnection("C:\\Gitlab\\dam\\dam\\damsystem\\src\\main\\resources\\test-db-config.properties");
        accDb = new AccessDatabase(setupConnection);
    }
    //close the connection and set objects as null to not get error
    @AfterEach
    public void tearDown(){
        accDb.closeConnection();
        accDb = null;
        setupConnection = null;
    }



    //test correct outcome for users not found, that is, a LoginException is cast.
    @Test
    public void testGetUserNotFound(){
        LoginException exThrown = assertThrows(LoginException.class, () -> {
            accDb.getUser("admdwdawin","passwddwadword");
        });

        assertEquals("User not found", exThrown.getMessage());
    }
    //Test that the user is not null of found
    @Test
    public void testGetUserFound(){
        AppUser user = null;
        try {
            user = accDb.getUser("admin", "password");
        } catch (LoginException e) {
            e.printStackTrace();
        }

        assertNotNull(user);
    }

    //Test that uploadasset works
    @Test
    public void testUploadAsset(){
        accDb.begin();
        try {
            accDb.uploadAsset(new Asset("path/not/present/in/db"));
        } catch (NoRowsAffectedException e) {
            e.printStackTrace();
            assertTrue(false);
        } finally{
            accDb.getTransactionManager().rollbackTransactionAsset();
        }
    }
    //Test that a NoRowsAffectedException is thrown if the asset already exists in the db
    @Test
    public void testUploadAssetException(){
        accDb.begin();
        Exception exThrown = assertThrows(Exception.class, () -> {
            accDb.uploadAsset(new Asset("pathThatExistsInDB"));
        });
        accDb.getTransactionManager().rollbackTransactionAsset();
        assertEquals(NoRowsAffectedException.class, exThrown.getClass());
    }

    //Test uploading tag works
    @Test
    public void testUploadTag(){
        accDb.begin();
        try {
            accDb.uploadTag(new Tag("unique","tag"));
        } catch (NoRowsAffectedException e) {
            e.printStackTrace();
            assertTrue(false);
        } finally{
            accDb.getTransactionManager().rollbackTransactionTag();
        }
    }
    //test that exception is thrown if tag already exists
    @Test
    public void testUploadTagException(){
        accDb.begin();
        Exception exThrown = assertThrows(Exception.class, () -> {
            accDb.uploadTag(new Tag("tagThatExists","InDB"));
        });
        accDb.getTransactionManager().rollbackTransactionTag();
        assertEquals(NoRowsAffectedException.class, exThrown.getClass());
    }

    //test that all tags associated to asset are returned
    @Test
    public void testGetTagsFromAsset(){
        ArrayList<Tag> actualTags = accDb.getTagsFromAsset(new Asset(1,"/some/path/1",0));
        ArrayList<Tag> targetTags = new ArrayList<>(Arrays.asList(
            new Tag(1,"TAG","1"),
            new Tag(2,"TAG", "2"),
            new Tag(3,"TAG","3"),
            new Tag(4, "TAG", "4")
        ));
        assertEquals(targetTags, actualTags);
    }
    //test that the list will be empty if untrue path is provided
    @Test
    public void testGetTagsFromAssetEmpty(){
        ArrayList<Tag> actualTags = accDb.getTagsFromAsset(new Asset(9191919,"/some/path1/that/does/not/exist",0));
        ArrayList<Tag> targetTags = new ArrayList<>();
        assertEquals(targetTags, actualTags);
    }

    //test that all assets that have all tags, and only all tags are returned
    @Test
    public void testGetAssetsFromTags(){
        ArrayList<Asset> actualAssets = accDb.getAssetsFromTags(new ArrayList<>(Arrays.asList(
            new Tag(2,"TAG","2"),
            new Tag(5,"TAG", "5")
        )));
        ArrayList<Asset> targetAssets = new ArrayList<>(Arrays.asList(
            new Asset(2,"/some/path/2",0),
            new Asset(3,"/some/path/3",0)
        ));
        assertEquals(targetAssets, actualAssets);
    }
    //test that returned asset list is empty if invalid tag is provided to the method
    @Test
    public void testGetAssetsFromTagsEmpty(){
        ArrayList<Asset> actualAssets = accDb.getAssetsFromTags(new ArrayList<>(Arrays.asList(
            new Tag(544, "awdwad", "awdkawd")
        )));
        ArrayList<Asset> targetAssets = new ArrayList<>();
        assertEquals(targetAssets, actualAssets);
    }

    //test that adding new tag to asset relation works.
    @Test
    public void testAddTagToAsset(){
        accDb.begin();
        try {
            accDb.addTagToAsset(new Asset(5, "/some/path/5", 0), new Tag(5,"TAG","5"));
        } catch (NoRowsAffectedException e) {
            e.printStackTrace();
            assertTrue(false);

        } finally{
            accDb.rollback();
        }
    }
    //test that adding existing tag/asset relation to image_tags table results in an exception
    @Test
    public void testAddTagToAssetException(){
        accDb.begin();
        Exception exThrown = assertThrows(Exception.class, () -> {
            accDb.addTagToAsset(new Asset(3, "some/random/path/", 0), new Tag(2,"doesntMatter","doesntmatter"));
        });
        accDb.rollback();
        assertEquals(NoRowsAffectedException.class, exThrown.getClass());
    }

    //test that all assets are returned
    @Test
    public void testGetAllAssets(){
        ArrayList<Asset> actualAssets = accDb.getAllAssets();
        ArrayList<Asset> targetAssets = new ArrayList<>(Arrays.asList(
            //0 in original represents null in database which all values for original_ref are in test db
            new Asset(1,"/some/path/1",0),
            new Asset(2,"/some/path/2",0),
            new Asset(3,"/some/path/3",0),
            new Asset(4,"/some/path/4",0),
            new Asset(5,"/some/path/5",0),
            new Asset(6,"pathThatExistsInDB",0)
        ));
        assertEquals(targetAssets, actualAssets);
    }
    //test that all tags are returned
    @Test
    public void testGetAllTags(){
        ArrayList<Tag> actualTags = accDb.getAllTags();
        ArrayList<Tag> targetTags = new ArrayList<>(Arrays.asList(
            //0 in original represents null in database which all values for original_ref are in test db
            new Tag(1,"TAG","1"),
            new Tag(2,"TAG","2"),
            new Tag(3,"TAG","3"),
            new Tag(4,"TAG","4"),
            new Tag(5,"TAG","5"),
            new Tag(6,"tagThatExists","InDB")
        ));
        assertEquals(targetTags, actualTags);
    }
    //test that deleting an asset is possible (given no associations in joined table)
    @Test
    public void testDeleteAsset(){
        accDb.begin();
        try {
            accDb.deleteAsset(new Asset(4, "/some/path/4", 0));
        } catch (NoRowsAffectedException e) {
            e.printStackTrace();
            assertTrue(false);
        } finally{
            accDb.getTransactionManager().rollbackTransactionAsset();
        }
    }
    //test that deleting an asset which is not there causes an exception to be thrown
    @Test
    public void testDeleteAssetException(){
        accDb.begin();
        Exception exThrown = assertThrows(Exception.class, () -> {
            accDb.deleteAsset(new Asset(2233,"assetThatDoesNotExist",0));;
        });
        assertEquals(NoRowsAffectedException.class, exThrown.getClass());
        accDb.getTransactionManager().rollbackTransactionAsset();
    }
    //test that deleting a tag is possible
    @Test
    public void testDeleteTag(){
        accDb.begin();
        try {
            accDb.deleteTag(new Tag(6,"tagThatExists","InDB"));
        } catch (NoRowsAffectedException e) {
            e.printStackTrace();
            assertTrue(false);
        } finally{
            accDb.getTransactionManager().rollbackTransactionTag();;
        }
    }
    //test that deleting a tag which is not there causes an exception to be thrown
    @Test
    public void testDeleteTagException(){
        accDb.begin();
        Exception exThrown = assertThrows(Exception.class, () ->{
            accDb.deleteTag(new Tag(523, "randomTagThatDoesntExist","random"));
        });
        accDb.getTransactionManager().rollbackTransactionTag();
        assertEquals(NoRowsAffectedException.class, exThrown.getClass());
    }
    //test removal of existing association
    @Test
    public void testRemoveTagFromAsset(){
        accDb.begin();
        try {
            accDb.removeTagFromAsset(new Asset(1,"/some/path/1",0), new Tag(1,"TAG","1"));
        } catch (NoRowsAffectedException e) {
            e.printStackTrace();
            assertTrue(false);
        } finally{
            accDb.rollback();
        }
    }
    //try to remove association which doesnt ecist
    @Test
    public void removeTagFromAssetException(){
        accDb.begin();
        Exception exThrown = assertThrows(Exception.class, () ->{
            accDb.removeTagFromAsset(new Asset(33,"adwadwad",0), new Tag(55,"adwwa","wdaw"));
        });
        assertEquals(NoRowsAffectedException.class, exThrown.getClass());
        accDb.rollback();
    }

    
    
}
