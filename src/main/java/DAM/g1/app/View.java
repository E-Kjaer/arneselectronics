package DAM.g1.app;

public enum View {
    LOGIN("login.fxml"),
    MAIN("main.fxml"),
    TEST("testFile.fxml"),
    VIEW_ALL("viewAll.fxml");


    private String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}
