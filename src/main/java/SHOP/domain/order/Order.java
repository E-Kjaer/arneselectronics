package SHOP.domain.order;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    private boolean company;
    private ArrayList<OrderLine> lines;
    private Shipping shipping;
    private Payment payment;
    private Date timestamp;

    public Order(boolean company, ArrayList<OrderLine> lines, Shipping shipping, Payment payment, Date timestamp) {
        this.company = company;
        this.lines = lines;
        this.shipping = shipping;
        this.payment = payment;
        this.timestamp = timestamp;
    }

    public Order() {
    }

    public boolean isCompany() {
        return company;
    }

    public void setCompany(boolean company) {
        this.company = company;
    }

    public ArrayList<OrderLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<OrderLine> lines) {
        this.lines = lines;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
