package DAM.g1;

import DAM.g1.app.App;
import DAM.g1.data.AccessDatabase;
import DAM.g1.domain.interfacemanagement.AdminController.AdminController;
import DAM.g1.domain.interfacemanagement.AdminController.SingletonAdminController;
import DAM.g1.domain.interfacemanagement.DAMToPIM.DamToPim;
import DAM.g1.domain.interfacemanagement.DAMToShop.DamToShop;

public class Main {
    public static AccessDatabase accDB = new AccessDatabase();

    public static AdminController adminController = new AdminController(accDB);

    public static DamToShop dts = new DamToShop(accDB);

    public static DamToPim dtp = new DamToPim(accDB);



    public static void main(String[] args) {
        try {
            SingletonAdminController.setInstance(adminController);

            // Launch the App
            App.launchApp();

        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            if (accDB != null) {
                accDB.closeConnection();
            }
        }
    }
}
