package OMS.Domain;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;


public class SendMail {

    public static void sendMail(Order order) throws MessagingException {
        String recipient = order.getShipping().getEmail();
        System.out.println("preparing to send email");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        String myAccountEmail = "arneelectronics@gmail.com";
        String password = "fkvy fpnh nmhl cknt";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

            Message message = prepareMessasge(session, myAccountEmail, recipient, order);
            System.out.println("Email Confirmation Message");
            Transport.send(message);


        //Message message = prepareMessasge(session, myAccountEmail, recipient, order);
        //System.out.println("message was sent");


    }

    private static Message prepareMessasge(Session session, String myAccountEmail, String recipient, Order order) throws MessagingException {
        try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(myAccountEmail));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                message.setSubject("Order Confirmation");

                // Build email content
                StringBuilder content = new StringBuilder();
                content.append("Dear Customer,\n\n");
                content.append("Thank you for your order. Here are the details:\n\n");

                // Append product information
                content.append("Products:\n");
                for (Product product : order.getProducts()) {
                    content.append(product.getAmount()).append(": ").append(product).append("\n");
                }

                // Append order date
                content.append("\nOrder Date: ").append(order.getOrderDate()).append("\n");

                // Append order status
                content.append("\nOrder Status: ").append(order.getStatus()).append("\n\n");

                // Set email content
                message.setText(content.toString());

                System.out.println("Message1");

                return message;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        }
    }

    public static void sendMail(String email) throws MessagingException {
        String recipient = email;
        System.out.println("preparing to send email");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        String myAccountEmail = "arneelectronics@gmail.com";
        String password = "fkvy fpnh nmhl cknt";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });


            Message message = prepareMessasge(session, myAccountEmail, recipient);
            System.out.println("PENDING TO SHIPPED MESSAGE");
            Transport.send(message);


        //Message message = prepareMessasge(session, myAccountEmail, recipient, order);
        //System.out.println("message was sent");


    }

    private static Message prepareMessasge(Session session, String myAccountEmail, String recipient) throws MessagingException {
        try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(myAccountEmail));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                message.setSubject("Order is shipped");

                // Build email content
                StringBuilder content = new StringBuilder();
                content.append("Dear Customer,\n\n");
                content.append("Your order has hereby been shipped from our warehouse\n\n");


                // Set email content
                message.setText(content.toString());

                System.out.println("PrepareMessage Statement");

                return message;


        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        }
    }

}
