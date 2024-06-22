package CMS;

import java.util.ArrayList;

public class DataMethods {
    private static String clickedContentPage = "0";

    private static int createdContentPages = 0;

    private static ArrayList<String> createdContentPagesList = new ArrayList<String>();

    private static boolean firstTimeSetUp = true;

    public static String getClickedContentPage() {
        return clickedContentPage;
    }

    public static synchronized void setClickedContentPage(String clickedContentPage) {
        DataMethods.clickedContentPage = clickedContentPage;
    }

    public static int getCreatedContentPages() {
        return createdContentPages;
    }

    public static synchronized void setCreatedContentPages(int createdContentPages) {
        DataMethods.createdContentPages = createdContentPages;
    }

    public static synchronized void incrementCreatedContentPages() {
        DataMethods.createdContentPages++;
    }

    public static synchronized void decrementCreatedContentPages() {
        DataMethods.createdContentPages--;
    }

    public static synchronized ArrayList<String> getCreatedContentPagesList() {
        return createdContentPagesList;
    }

    public static synchronized String getCreatedContentPagesListPosition(int listPosition) {
        return createdContentPagesList.get(listPosition);
    }

    public static synchronized void setCreatedContentPagesList(ArrayList<String> createdContentPagesList) {
        DataMethods.createdContentPagesList = createdContentPagesList;
    }

    public static synchronized void addCreatedContentPagesList(String createdContentPagesList) {
        DataMethods.createdContentPagesList.add(createdContentPagesList);
    }

    public static synchronized void removeCreatedContentPagesList(String createdContentPagesList) {
        DataMethods.createdContentPagesList.remove(createdContentPagesList);
    }

    public static boolean isFirstTimeSetUp() {
        return firstTimeSetUp;
    }

    public static void setFirstTimeSetUp(boolean firstTimeSetUp) {
        DataMethods.firstTimeSetUp = firstTimeSetUp;
    }
}
