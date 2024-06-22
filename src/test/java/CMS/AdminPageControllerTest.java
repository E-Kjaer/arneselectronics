package CMS;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class AdminPageControllerTest {

    @Test
    void updateContentPagesInfo() {
        AdminPageController adminPageController = new AdminPageController();
        String x = "Content Page 1";
        int y = DataMethods.getCreatedContentPages();
        adminPageController.updateContentPagesInfo(x);
        Assertions.assertEquals(y, DataMethods.getCreatedContentPages()-1); // Test that the number is incremented correctly
        Assertions.assertEquals(x, DataMethods.getCreatedContentPagesListPosition(0)); // Test that the string is inserted correctly
    }

    @Test
    void removeContentPagesInfo() {
        AdminPageController adminPageController = new AdminPageController();
        String x = "Content Page 1";
        int y = DataMethods.getCreatedContentPages();
        ArrayList<String> z = DataMethods.getCreatedContentPagesList();
        adminPageController.updateContentPagesInfo(x);
        adminPageController.removeContentPagesInfo(x);
        Assertions.assertEquals(y, DataMethods.getCreatedContentPages()); // Test that number is decremented correctly
        Assertions.assertEquals(z, DataMethods.getCreatedContentPagesList()); // Test that the string is removed correctly
    }
}