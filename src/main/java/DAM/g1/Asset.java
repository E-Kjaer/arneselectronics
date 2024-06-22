package DAM.g1;

import java.util.ArrayList;

public class Asset {
    
    public Asset(int id, String path, int original){
        this.id = id;
        this.path = path;
        this.original = original;
    }

    public Asset(int id, String path, int original, ArrayList<Tag> tags){
        this.id = id;
        this.path = path;
        this.original = original;
        this.tags = tags;
    }

    public Asset(String path, int original, ArrayList<Tag> tags){
        this.path = path;
        this.original = original;
        this.tags = tags;
    }

    public Asset(String path){
        this.path = path;
    }

    public Asset(String path, ArrayList<Tag> tags){
        this.path = path;
        this.tags = tags;
    }


    public Asset(Object object, String assetPath) {
        //TODO Auto-generated constructor stub
    }


    private int id;

    private String path;

    private int original;

    private ArrayList<Tag> tags;

    public int getId(){
        return id;
    }

    public String getPath(){
        return path;
    }

    public int getOriginal(){
        return original;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }

    public void setTags(ArrayList<Tag> tags){
        this.tags = tags;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + id + ", Path: " + path + ", original_ref: " + original + ", Tags: ");
        if(tags!=null){
            for (Tag tag : tags){
                sb.append(tag.toString());
            }
        }
        return sb.toString();
    }

    @Override 
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;
        Asset asset = (Asset) obj;
        //Doesn't check for list of tags since it can be arbitrary
        if(asset.getId()==this.id && asset.getOriginal()==this.original && asset.getPath().equals(this.path)){
            return true;
        }
        return false;
    }


}
