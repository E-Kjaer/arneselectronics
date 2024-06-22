package DAM.g1.domain.interfacemanagement.DAMToPIM;

public interface DAMTOPIMInterface {
    // Interface between PIM and DAM
    // Key that identifies which assets to include.
    public String getProductImage(String products, String key);
    public String getImageByProductID(int productID);
}
