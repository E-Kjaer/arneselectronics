package DAM.g1.app;

import java.net.URL;
import java.util.ResourceBundle;

import DAM.g1.Asset;
import DAM.g1.Tag;
import DAM.g1.domain.interfacemanagement.AdminController.AdminController;
import DAM.g1.domain.interfacemanagement.AdminController.SingletonAdminController;
import DAM.g1.domain.interfacemanagement.DAMToPIM.DamToPim;
import DAM.g1.domain.interfacemanagement.DAMToShop.DamToShop;
import DAM.g1.domain.usermanagement.Authenticator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainController implements Initializable {

    public AdminController adminController = SingletonAdminController.getInstance().getAdminController();
    private Authenticator auth = adminController.getAuthenticator();

    private DamToShop damToShop;
    private DamToPim damToPim;

    @FXML
    private TextField tagInputField;
    @FXML
    private TextField deleteTagInputField;
    @FXML
    private TextField uploadAssetPathField;
    @FXML
    private Button uploadTagButton;
    @FXML
    private Button getProductImageButton;
    @FXML
    private Button viewAllButton;
    @FXML
    private Button testButton;
    @FXML
    private Button uploadAssetButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button deleteAssetButton;
    @FXML
    private Button deleteTagButton;
    @FXML
    private Button assetByTagButton;
    @FXML
    private TextField deleteAssetPathField;
    @FXML
    private TextField assignAssetPathField;
    @FXML
    private TextField assignTagInputField;
    @FXML
    private Button assignTagButton;
    @FXML
    private TextField tagKeyInputField;


    public MainController(DamToShop damToShop) {
        this.damToShop = damToShop;
    }

    public MainController(DamToPim damToPim) {
        this.damToPim = damToPim;
    }

    public MainController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void testFunc(ActionEvent event) {
        if (auth.hasAccess()) {
            System.out.println("access");
        } else {
            System.out.print("no access");
        }
    }

    @FXML
    public void assetByTagButtonClicked(ActionEvent event) {
        if (damToShop != null) {
            if (auth.hasAccess()) {
                String imagesByTags = damToShop.getImagesByTags("{\"tags\":[\"Grey\"]}");
                System.out.println("Images by Tags: " + imagesByTags);
            } else {
                System.out.print("No access");
            }
        } else {
            System.out.println("DamToShop instance is null. Cannot proceed.");
        }
    }

    @FXML
    public void getProductImageButtonClicked(ActionEvent event) {
        if (damToPim != null) {
            if (auth.hasAccess()) {
                String productImage = damToPim.getProductImage("{\"products\":[\"Product1\"]}", "{\"tags\":[\"Grey\"]}");
                System.out.println("Product Image: " + productImage);
            } else {
                System.out.print("No access");
            }
        } else {
            System.out.println("DamToPim instance is null. Cannot proceed.");
        }
    }

    @FXML
    public void uploadTag(ActionEvent event) {
        String tagKey = tagInputField.getText();
        if (tagKey != null && !tagKey.isEmpty()) {
            Tag tag = new Tag(null, tagKey);
            if (auth.hasAccess()) {
                boolean success = adminController.uploadTag(tag);
                if (success) {
                    System.out.println("Tag uploaded successfully.");
                } else {
                    System.out.println("Failed to upload tag.");
                }
            } else {
                System.out.print("No access");
            }
        } else {
            System.out.println("Tag input is empty.");
        }
    }


   @FXML
public void deleteTag(ActionEvent event) {
    String tagIdText = deleteTagInputField.getText();
    if (tagIdText != null && !tagIdText.isEmpty()) {
        try {
            int tagId = Integer.parseInt(tagIdText); // Convert input to integer
            if (auth.hasAccess()) {
                boolean success = adminController.deleteTag(new Tag(tagId, null));
                if (success) {
                    System.out.println("Tag deleted successfully.");
                } else {
                    System.out.println("Failed to delete tag.");
                }
            } else {
                System.out.print("No access");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid tag ID format.");
        }
    } else {
        System.out.println("Delete tag input is empty.");
    }
}


    @FXML
    public void assignTag(ActionEvent event) {
        String assetPath = assignAssetPathField.getText();
        String tagKey = assignTagInputField.getText();
        if (assetPath != null && !assetPath.isEmpty() && tagKey != null && !tagKey.isEmpty()) {
            Asset asset = new Asset(assetPath);
            Tag tag = new Tag(null, tagKey);
            if (auth.hasAccess()) {
                boolean success = adminController.addTags(asset);
                if (success) {
                    System.out.println("Tag assigned successfully.");
                } else {
                    System.out.println("Failed to assign tag.");
                }
            } else {
                System.out.print("No access");
            }
        } else {
            System.out.println("Asset path or tag input is empty.");
        }
    }



    @FXML
    public void uploadAsset(ActionEvent event) {
        String assetPath = uploadAssetPathField.getText();
        if (assetPath != null && !assetPath.isEmpty()) {
            Asset asset = new Asset(assetPath); 
            if (auth.hasAccess()) {
                boolean success = adminController.uploadAsset(asset);
                if (success) {
                    System.out.println("Asset uploaded successfully.");
                } else {
                    System.out.println("Failed to upload asset.");
                }
            } else {
                System.out.print("No access");
            }
        } else {
            System.out.println("Asset path input is empty.");
        }
    }


    @FXML
    public void deleteAsset(ActionEvent event) {
        String assetPath = deleteAssetPathField.getText();
        if (assetPath != null && !assetPath.isEmpty()) {
            if (auth.hasAccess()) {
                boolean success = adminController.deleteAsset(new Asset(assetPath));
                if (success) {
                    System.out.println("Asset deleted successfully.");
                } else {
                    System.out.println("Failed to delete asset.");
                }
            } else {
                System.out.print("No access");
            }
        } else {
            System.out.println("Asset path input is empty.");
        }
    }


    @FXML
    public void switchTo() {
        SceneSwitcher.switchTo(View.VIEW_ALL);
    }
}
