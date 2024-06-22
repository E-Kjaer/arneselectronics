package DAM.g1.domain.interfacemanagement.AdminController;

public class SingletonAdminController {
    
    private static SingletonAdminController instance;
    private AdminController adminController;

    private SingletonAdminController(){}

    public static synchronized void setInstance(AdminController adminController){
        if(instance == null){
            instance = new SingletonAdminController();
            instance.setAdminController(adminController);
        }
    }
    public static SingletonAdminController getInstance() throws IllegalStateException{
        if(instance == null){
            throw new IllegalStateException("Object not instantiated");
        }
        return instance;
    }

    private void setAdminController(AdminController adminController){
        this.adminController = adminController;
    }

    public AdminController getAdminController(){
        return adminController;
    }
}
