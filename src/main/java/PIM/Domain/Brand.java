package PIM.Domain;

public class Brand {
    private int id;
    private String name;

    public Brand() {}

    //Constructor
    public Brand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return
                "Brand{" + "id=" + id
                        + ", name=" + name
                        + '}';
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}


