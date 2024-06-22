package DAM.g1.domain.interfacemanagement.DAMToShop;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DAM.g1.Asset;
import DAM.g1.Tag;
import DAM.g1.data.AccessDatabase;

public class DamToShop implements DAMTOSHOPInterface{
    
    public DamToShop(AccessDatabase accDb){
        this.accDb = accDb;
    }

    AccessDatabase accDb;

    @Override
    public String getImagesByTags(String tagString){
        //List to store tags when converted from JSONString
        ArrayList<Tag> tags = new ArrayList<Tag>();
        //obj to convert from json to Tag objects
        JSONObject json;
        try {
            json = new JSONObject(tagString);
        } catch (JSONException e) {
            e.printStackTrace();
            return "{\"error\":\"invalid_input\"}";
        }
        for(String key : json.keySet()){
            JSONArray values = json.getJSONArray(key);
            for(int i = 0;i<values.length();i++){
                //add tag objects to tags list
                tags.add(new Tag(null, values.getString(i)));
            }
        }
        //DEBUG
        for (Tag tag : tags){
            System.out.println(tag.toString());
        }
        //get associated assets from tags from db
        ArrayList<Asset> assets = accDb.getAssetsFromTags(tags);
        //DEBUG
        for (Asset asset : assets){
            System.out.println(asset.toString());
        }
        //main obj to store json that is returned
        JSONObject main = new JSONObject();
        //images array containing image objects
        JSONArray images = new JSONArray();
        for(Asset asset : assets){
            //get all tags associated with asset
            asset.setTags(accDb.getTagsFromAsset(asset));
            //define arbitrary image object
            JSONObject image = new JSONObject();
            //collection obj to store raw tag value
            ArrayList<String> tagsFromAssets = new ArrayList<>();
            for(Tag tag : asset.getTags()){
                //store each tag val in col obj
                tagsFromAssets.add(tag.getValue());
            }
            //put path(asset) w associated tags in image obj
            image.put("path",asset.getPath());
            image.put("tags",tagsFromAssets);
            //put in images parent obj
            images.put(image);
        }
        //put image json array in main json obj
        main.put("images",images);
        //return json string of images w paths and all associated tags
        return main.toString();
        
    }
    public static void main(String[] args) {
        DamToShop dts = new DamToShop(new AccessDatabase());
        System.out.println((dts.getImagesByTags("{\"Brand\":[\"Logitech\"]}")));
    }



}
