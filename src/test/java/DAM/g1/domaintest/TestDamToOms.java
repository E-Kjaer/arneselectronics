package DAM.g1.domaintest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
import DAM.g1.domain.interfacemanagement.DAMToOMS.DamToOms;
import DAM.g1.domain.interfacemanagement.DAMToShop.DamToShop;

import org.junit.jupiter.api.Test;


public class TestDamToOms {

    AccessDatabase accessDatabase;
    DamToOms damToOms;

    @BeforeEach
    public void init(){
        this.accessDatabase = mock(AccessDatabase.class);
        this.damToOms = new DamToOms(accessDatabase);
        when(accessDatabase.getAssetsFromTags(new ArrayList<>(Arrays.asList(new Tag(null,"Grey"))))).thenReturn(new ArrayList<>(Arrays.asList(
            new Asset(9,"path/to/iPhoneimage",0),
            new Asset(10,"path/to/iMacImage",0),
            new Asset(1,"/some/path1",0)
        )));
        when(accessDatabase.getAssetsFromTags(new ArrayList<>(Arrays.asList(new Tag(null,"Grey"), new Tag(null,"Yellow"))))).thenReturn(new ArrayList<>(Arrays.asList(
            new Asset(1,"/some/path1",0)
        )));


        ///
        ///


        when(accessDatabase.getTagsFromAsset(new Asset(9, "path/to/iPhoneimage", 0))).thenReturn(new ArrayList<>(Arrays.asList(
            new Tag(null,"Grey"),
            new Tag(null,"Small")
        )));
        when(accessDatabase.getTagsFromAsset(new Asset(10,"path/to/iMacImage",0))).thenReturn(new ArrayList<>(Arrays.asList(
            new Tag(null,"Laptop"),
            new Tag(null,"Grey")
        )));
        when(accessDatabase.getTagsFromAsset(new Asset(1,"/some/path1",0))).thenReturn(new ArrayList<>(Arrays.asList(
            new Tag(null,"Yellow"),
            new Tag(null,"Brown"),
            new Tag(null,"Black"),
            new Tag(null,"Grey"),
            new Tag(null,"Small"),
            new Tag(null,"Tractor")
        )));
    }

    @AfterEach
    public void tearDown(){
        this.accessDatabase = null;
        this.damToOms = null;
    }

    @Test
    public void testGetImagesByTagsSingleTag(){
        String output = damToOms.getProductImage("{\"tags\":[\"Grey\"]}");
        assertEquals("{\"images\":[{\"path\":\"path/to/iPhoneimage\",\"tags\":[\"Grey\",\"Small\"]},{\"path\":\"path/to/iMacImage\",\"tags\":[\"Laptop\",\"Grey\"]},{\"path\":\"/some/path1\",\"tags\":[\"Yellow\",\"Brown\",\"Black\",\"Grey\",\"Small\",\"Tractor\"]}]}",
        output);
    }
    @Test
    public void testGetImagesByTagsMultipleTags(){
        String output = damToOms.getProductImage("{\"tags\":[\"Grey\",\"Yellow\"]}");
        assertEquals("{\"images\":[{\"path\":\"/some/path1\",\"tags\":[\"Yellow\",\"Brown\",\"Black\",\"Grey\",\"Small\",\"Tractor\"]}]}",
        output);
    }
    @Test
    public void testGetImagesByTagsInvalidInput(){
        String output = damToOms.getProductImage("dadawdjioawdaw{[{[]{[{{{{wdawdkado]}}]}}]}");
        assertEquals("{\"error\":\"invalid_input\"}", output);
    }


}
