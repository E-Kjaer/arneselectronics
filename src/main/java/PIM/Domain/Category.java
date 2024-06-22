package PIM.Domain;

public class Category {
    private int id;
    private String name;
    private String description;
    private Category subCategory;

    public Category() {}

    //Subcategory Constructor
    public Category(int id, String name, String description, Category subCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subCategory = subCategory;
    }

    //Parentcategory Constructor
    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return
                "Category{" + "id=" + id
                        + ", name=" + name
                        + ", description=" + description
                        + ", subCategory=" + subCategory + '}';
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getSubCategory() {
        return subCategory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubCategory(Category subCategory) {
        this.subCategory = subCategory;
    }
}
