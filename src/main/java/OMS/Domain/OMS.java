package OMS.Domain;

import OMS.Database.ShippingDBManager;
import OMS.Database.OrderDBManager;
import OMS.Domain.Interfaces.OMSTOSHOPInterface;
import org.json.simple.parser.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static OMS.Database.OrderDBManager.getStockCount;


public class OMS implements OMSTOSHOPInterface {
	public static ArrayList<Order> listOfOrders = new ArrayList<>();

	private static void initialize() throws MessagingException, IOException, URISyntaxException, ParseException, SQLException {
		//Load stock/stock and customerDB from database, do this in class constructors
		Locale.setDefault(Locale.US);
		System.out.println("OMS Initializing");
		System.out.println("Running demo");
		Demo.orderDemo();
	}

	public static void main(String[] args) throws MessagingException, IOException, URISyntaxException, ParseException, SQLException {
		System.out.println("Running OMS main");
		initialize();
		//DBManager.updateStatus();
		//submitOrder2();

	}

	private Status checkStatus(Order order){
		return order.getStatus();
	}

	@Override
	public boolean submitOrder(String jsonString) {
		//Get product_id and amount from Json string
		Order order = new Order(jsonString);
		try {
			SendMail.sendMail(order);
			OrderDBManager.addOrder(order);
			ShippingDBManager.createShipping(order.getShipping(), order.getOrderDate());

		} catch (MessagingException | SQLException e) {
			throw new RuntimeException(e);
		}

		return true;
	}

	@Override
	public int getProductCount(String productId) {
		return getStockCount(new String[]{productId}).get(productId);
	}

	@Override
	public Map<String, Integer> getProductCounts(String[] productIds) {
		return getStockCount(productIds);
	}

	@Override
	public String getOrderStatus(String orderId) {
		return null;
	}

}