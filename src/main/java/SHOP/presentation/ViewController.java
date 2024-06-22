package SHOP.presentation;

import SHOP.data.models.Guide;
import SHOP.data.models.Product;
import SHOP.domain.Domain;
import SHOP.presentation.subcontrollers.*;
import SHOP.presentation.subcontrollers.PDP;
import SHOP.presentation.subcontrollers.PLP_Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.util.*;

public class ViewController {
    // SINGLETON
    private static ViewController instance = new ViewController();

    // DOMAIN CONNECTION
    private Domain domain;

    // UI-OBJECTS FROM FXML
    @FXML
    private BorderPane borderPane;

    // UI-OBJECTS TO BE INSERTED
    private Checkout checkout;
    private Homepage homepage;
    private PLP plp;
    private PDP pdp;
    private Header header;
    private Footer footer;
    private Basket basket;
    private Company_Checkout companyCheckout;
    private ContentPageList contentPageList;
    private BlogPostDisplay blogPostDisplay;
    private Contact_Us contact;
    private Basket_CustomerType basket_CustomerType;

    // VARIOUS VARIABLES
    private Map<String, String> categoryTexts;

    private ViewController() {
        System.out.println("ViewController Constructor");
        // Sets up data-connection
        domain = new Domain();

        // Sets up each UI-component
        header = new Header();
        footer = new Footer();
        plp = new PLP();
        pdp = new PDP();
        homepage = new Homepage();
        checkout = new Checkout();
        basket = new Basket(domain.getBasket());
        basket_CustomerType = new Basket_CustomerType();
        companyCheckout = new Company_Checkout();
        contentPageList = new ContentPageList(domain.getGuides());
        contact = new Contact_Us();
        blogPostDisplay = new BlogPostDisplay();

        // Sets up various variables
        categoryTexts = getCategoryTexts();
    }

    //Gets logo from Domain
    public String getLogo() {
        return domain.getLogo();
    }

    public void initialize() {
        // Sizes the element to the screen-size
        borderPane.setMaxWidth(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        checkout.setPrefWidth(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2);
        companyCheckout.setPrefWidth(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2);
        basket.setPrefWidth(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2);

        // Show the initial elements
        borderPane.setBottom(footer);
        borderPane.setTop(header);
        borderPane.setCenter(homepage);

        // Initizalize elements
        homepage.loadHighlightedProducts(domain, 4);
        checkout.setBasket(domain.getBasket());
        companyCheckout.setBasket(domain.getBasket());
        categoryTexts = getCategoryTexts();
        header.setLogo(this.getLogo());
        homepage.setLogo(this.getLogo());
    }

    // Gets singleton instance of this class
    public static ViewController getInstance() {
        return instance;
    }

    // Gets the FXMLLoader for the startpage and is the startpoint for loading the whole app
    public FXMLLoader getInitialGUILoader() {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("startpage.fxml"));
        loader.setController(ViewController.getInstance());
        return loader;
    }

    // Get categories along with texts
    public Map<String, String> getCategoryTexts() {
        Map<String, String> categoryTexts = new HashMap<>();
        categoryTexts.put("Computers & Office", "See our wide selection of computers. \n We have PC components, software, computer accessories, and much more!");
        categoryTexts.put("Phones & Smart Devices", "Explore the wide range of mobiles, tablets, and smartwatches from well-known brands \n such as Apple, Samsung, Huawei, Motorola, and OnePlus.");
        categoryTexts.put("TV & Smart Home", "At Arnes Electronics, you'll find everything you need when it comes to TV, Sound and Smart Home");
        categoryTexts.put("Gaming", "We got everything you need to game \n from cool headsets and keyboards to the latest games, computers, and consoles");
        return categoryTexts;
    }

    // Show PLP by category
    public void showPLPbyCategory(String category) {
        plp.setCategory(category, categoryTexts.get(category));

        ArrayList<Product> products = domain.getProductsByCategory(category);
        plp.setBrandsFromProducts(products);
        plp.setProductList(products);
        this.showPLP(plp.defaultSort(products));
    }

    // Show search results in PLP
    public void showPLPSearch(String search) {
        List<Product> products = domain.getProductsBySearch(search);
        plp.setBrandsFromProducts(products);
        plp.setCategory("Searching for " + search , "We found " + products.size() + " products");
        plp.setProductList((ArrayList<Product>) products);
        this.showPLP(plp.defaultSort(products));
    }

    public void updatePLP() {
        this.showPLP(plp.updateProducts(plp.getProductList()));
        System.out.println("FILTER!");
    }

    // Shows PLP
    private void showPLP(List<Product> products) {
        // Converts products to their PLP counterpart
        ArrayList<PLP_Product> plp_products = new ArrayList<PLP_Product>();

        for (Product product : products) {
            PLP_Product plpProduct = new PLP_Product();

            plpProduct.setProductID(product.getId());
            plpProduct.setProductName(product.getName());
            plpProduct.setProductPrice(product.getPrice());
            if (product.getImage() != null) plpProduct.setProductImage(product.getImage());
            plpProduct.setLayout(domain.getPLPLayout());

            plp_products.add(plpProduct);
        }

        plp.setProducts(plp_products);

        // Show PLP
        borderPane.setLeft(null);
        borderPane.setRight(null);
        borderPane.setCenter(plp);
    }

    // Shows PDP
    public void showPDP(int productId) {
        //Gets the product
        Product product = domain.getProduct(productId);

        // Sets up the PDP with product, layout etc...
        pdp.setProduct(product);
        pdp.setBasket(domain.getBasket());
        pdp.setLayout(domain.getPDPLayout(product.getCategory()));
        pdp.showProduct();

        // Show PDP
        borderPane.setLeft(null);
        borderPane.setRight(null);
        borderPane.setCenter(pdp);
    }

    // Show homepage
    public void showHomePage() {
        borderPane.setLeft(null);
        borderPane.setRight(null);
        borderPane.setCenter(homepage);
    }

    // Show checkout
    public void showCheckout(){

        basket.setBasketProducts(domain.getAllProducts());
        borderPane.setLeft(basket_CustomerType);
        borderPane.setCenter(null);
        borderPane.setRight(basket);

    }

    // Show customer checkout
    public void showCustomerCheckout(){
        basket.setBasketProducts(domain.getAllProducts());
        borderPane.setLeft(checkout);
        borderPane.setCenter(null);
        borderPane.setRight(basket);
    }

    // Show company checkout
    public void showCompanyCheckout(){
        basket.setBasketProducts(domain.getAllProducts());
        borderPane.setLeft(companyCheckout);
        borderPane.setCenter(null);
        borderPane.setRight(basket);
    }

    // Show content pages
    public void showContentPageList(){
        borderPane.setLeft(null);
        borderPane.setRight(null);
        borderPane.setCenter(contentPageList);
    }

    // Show a blogpost for the given guide
    public void showBlogPost(Guide guide){
        blogPostDisplay.setText(guide);
        borderPane.setLeft(null);
        borderPane.setRight(null);
        borderPane.setCenter(blogPostDisplay);
    }

    // Show contact us-page
    public void showContactUs () {
        borderPane.setRight(null);
        borderPane.setCenter(contact);
        borderPane.setLeft(null);
    }

    // Update the basket-label showing the amount of products in the basket
    public void updateBasketAmountInHeader() {
        Collection<Integer> quantityValues = domain.getBasket().getAllProducts().values();
        int amount = 0;
        for (Integer quantity : quantityValues) {
            amount += quantity;
        }

        header.setAmount(amount);
    }
}
