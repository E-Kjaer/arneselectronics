package OMS.Domain;

import OMS.Database.OrderDBManager;

import javax.mail.MessagingException;
import java.io.File;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Objects;

public class Demo {

    public static void orderDemo() throws URISyntaxException, SQLException {
        //Cant get getting via ressource to work
        //File orderFile = new File(Objects.requireNonNull(OMS.class.getResource("/OMS/Domain/orderExample.json")).toURI());
        File orderFile = new File("src/main/resources/OMS/Domain/orderExample.json");
        Order order = new Order(orderFile);
        System.out.println(order);


        for (Product product : order.getProducts()) {
            int tempAmount = product.getAmount();
            product.setAmount(40);
            OrderDBManager.addProductToStock(product);
            product.setAmount(tempAmount);
        }

        try {
            OrderDBManager.addOrder(order);
            System.out.println("this is email:" + order.getShipping().getEmail());
            SendMail.sendMail(order);
        } catch (SQLException e) {
            System.out.println("Problem submitting order");
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
