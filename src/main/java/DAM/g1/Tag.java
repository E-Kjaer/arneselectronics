package DAM.g1;

public class Tag{

    public Tag(int id, String key, String value){
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public Tag(String key, String value){
        this.key = key;
        this.value = value;
    }

    public Tag(int tagId, Object object) {
        //TODO Auto-generated constructor stub
    }

    private int id;

    private String key;

    private String value;

    public int getId(){
        return id;
    }

    public String getKey(){
        return key;       
    }

    public String getValue(){
        return value;
    }
    

    @Override
    public String toString(){
        return "{Id: " + id + ", key: " + key + ", value: " + value + "}"; 
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Tag tag = (Tag) obj;
        if (tag.getId()==this.id&& tag.getValue().equals(this.value)){
            return true;
        }
        return false;
    }

}