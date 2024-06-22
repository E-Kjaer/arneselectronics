package DAM.g1.domain.interfacemanagement.DAMToPIM;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DAM.g1.Asset;
import DAM.g1.Tag;
import DAM.g1.data.AccessDatabase;
import DAM.g1.domain.interfacemanagement.DAMToShop.DamToShop;

public class DamToPim implements DAMTOPIMInterface{
    
    public DamToPim(AccessDatabase accDb){
        this.accDb = accDb;
    }

    AccessDatabase accDb;

    @Override
    public String getImageByProductID(int product_id) {
        Asset asset = accDb.getImageByProductId(product_id);
        return asset.getPath();
    }

    //for now only implemented for key param as if products were a tag aswell
    @Override
    public String getProductImage(String products, String key){
        ArrayList<Tag> tags = new ArrayList<Tag>();
        //obj to convert from json to Tag objects
        JSONObject json;
        try {
            json = new JSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return "{\"error\":\"invalid_input\"}";
        }
        //get keys from json object, iterate through each key and get associated values (likely only 1 key)
        for(String aKey : json.keySet()){
            JSONArray values = json.getJSONArray(aKey);
            for(int i = 0;i<values.length();i++){
                //add tag objects to tags list
                tags.add(new Tag(null, values.getString(i)));
            }
        }
        //get associated assets from tags from db
        ArrayList<Asset> assets = accDb.getAssetsFromTags(tags);
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
        //JSON EXAMPLE - SØG EFTER ALLE IMAGES MED TAGGET 'GREY'!!!
        DamToShop dts = new DamToShop(new AccessDatabase());
        String returnVal = dts.getImagesByTags("{\"tags\":[\"Grey\"]}");
        System.out.println(returnVal);
    }

}
