package SHOP.domain.order;

public class Payment {
    private String method;

    public Payment(String method) {
        this.method = method;
    }

    public Payment() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
