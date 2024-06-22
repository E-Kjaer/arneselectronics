package Interfaces.PIM;

public interface PIMTOOMSInterface {
    // Interface between OMS and PIM
    // Gets the relevant metadata for the desired products. Accepts a list of product ids.
    // Used for generating a confirmation email.
    public String getProductsData(String products);
}
