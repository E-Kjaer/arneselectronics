package DAM.g1.domaintest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DAM.g1.Asset;
import DAM.g1.Tag;
import DAM.g1.data.AccessDatabase;
import DAM.g1.data.NoRowsAffectedException;
import DAM.g1.domain.interfacemanagement.AdminController.AdminController;

public class TestAdminController{

    public AdminController adminController;
    public AccessDatabase accDb;

    @BeforeEach
    public void init(){
        this.accDb = mock(AccessDatabase.class);
        this.adminController = new AdminController(accDb);
    }
    @AfterEach
    public void tearDown(){
        this.accDb = null;
        this.adminController = null;
    }

    @Test
    public void testAddTagsException() throws NoRowsAffectedException{
        doThrow(new NoRowsAffectedException("No rows affected.")).when(accDb).addTagToAsset(new Asset("arbitrary_path"), new Tag(null, "val1"));
        boolean output = adminController.addTags(new Asset("arbitrary_path", new ArrayList<>(Arrays.asList(new Tag(null,"val1"),new Tag(null, "val2")))));
        assertEquals(false, output);
    }
    @Test
    public void testAddTagsSuccess(){
        boolean output = adminController.addTags(new Asset("arbitrary_path", new ArrayList<>(Arrays.asList(new Tag(null,"val1"),new Tag(null, "val2")))));
        assertEquals(true, output);
    }

    @Test
    public void testRemoveTagsException() throws NoRowsAffectedException{
        doThrow(new NoRowsAffectedException("No rows affected.")).when(accDb).removeTagFromAsset(new Asset("arbitrary_path"), new Tag(null, "val1"));
        boolean output = adminController.removeTags(new Asset("arbitrary_path", new ArrayList<>(Arrays.asList(new Tag(null,"val1"),new Tag(null, "val2")))));
        assertEquals(false, output);
    }

    @Test
    public void testRemoveTagsSuccess(){
        boolean output = adminController.removeTags(new Asset("arbitrary_path", new ArrayList<>(Arrays.asList(new Tag(null,"val1"),new Tag(null, "val2")))));
        assertEquals(true, output);
    }
}