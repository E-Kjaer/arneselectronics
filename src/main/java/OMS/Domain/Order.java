package OMS.Domain;

import OMS.Database.ShippingDBManager;
import OMS.Database.OrderDBManager;
import SHOP.domain.order.Shipping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class Order {
	private BigInteger order_id;
	private List<Product> products;
	private Timestamp orderDate;
	private Status status;
	private String method;
	private boolean company;
	private Shipping shipping;
	private double totalPrice;

	public Order(File jsonFile) {
		status = Status.PENDING;
		System.out.println(jsonFile.toString());
		// parsing file "JSONExample.json"
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonFile);
			// typecasting obj to JSONObject
			System.out.println(jsonNode.toString());
			readOrder(jsonNode.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		calculatePrice();
    }

	public Order (String jsonString) {
		status = Status.PENDING;
		readOrder(jsonString);
		calculatePrice();
	}


	public Order(BigInteger order_id, Timestamp orderDate, Status status){
		this.order_id = order_id;
		this.orderDate = orderDate;
		this.status = status;
		calculatePrice();
	}

	public Order(BigInteger orderId, List<Product> products, Timestamp orderDate){
		this.order_id = orderId;
		this.products = products;
		this.orderDate = orderDate;
		status = Status.PENDING;
		calculatePrice();
	}

	public Order(BigInteger id, double totalPrice, Timestamp date, Status status) {
		this.order_id = id;
		this.totalPrice = totalPrice;
		orderDate = date;
		this.status = status;
		//calculatePrice();
	}

	private void readOrder(String json_orderInfo) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			JSONObject jsonObject = new JSONObject(json_orderInfo);
			//Getting products
			JSONArray jsonArray = (JSONArray) jsonObject.get("lines");
			products = new ArrayList<>();

			for (int i = 0; i < jsonArray.length();i++) {
				Product product = objectMapper.readValue(jsonArray.get(i).toString(), Product.class);
				System.out.println(product);
				products.add(product);
			}

			method = jsonObject.getJSONObject("payment").getString("method");
			company = jsonObject.getBoolean("company");


			shipping = objectMapper.readValue(jsonObject.getJSONObject("shipping").toString(), Shipping.class);
			orderDate = new Timestamp(jsonObject.getLong("timestamp"));

		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public void setStatus(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	public Timestamp getOrderDate(){
		return orderDate;
	}


	public List<Product> getProducts(){
		return products;
	}

	public long getOrderIdLong()  {
		return order_id.longValue();
	}

	public BigInteger getOrder_id() {
		return order_id;
	}

	public void setOrder_id(BigInteger order_id) {
		this.order_id = order_id;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	@Override
	public String toString(){

		StringBuilder temp = new StringBuilder("Order ID: " + order_id + "\n\t");
		temp.append("Order date: ").append(orderDate).append("\n\t");
		temp.append("Status: ").append(status).append("\n\t");
		temp.append("Products: [");
		for (Product product : products) {
			temp.append("\n\t ").append(product);
		}
		temp.append("         ]\n");

		return temp.toString();
	}

	public void calculatePrice() {
		for (Product product : products) {
			totalPrice += product.getPrice();
		}
	}

	public double getTotalPrice(){
		return totalPrice;
	}
}


