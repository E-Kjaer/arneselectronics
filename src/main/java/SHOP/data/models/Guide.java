package SHOP.data.models;

public class Guide {
    private String title;
    private String author;
    private String date;
    private String image;
    private String body;

    public Guide() {
    }

    public Guide(String title, String author, String date, String image, String body) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.image = image;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
