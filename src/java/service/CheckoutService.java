/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.*;
import model.*;

import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.text.NumberFormat;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class CheckoutService {
    
    private CartDao cartDao;
    private ProductDao productDao;
    private OrderDao orderDao;
    private PaymentDao paymentDao;

    public CheckoutService() {
        this.cartDao = new CartDao();
        this.productDao = new ProductDao();
        this.orderDao = new OrderDao();
        this.paymentDao = new PaymentDao();
    }

    public String processCODCheckout(User acc, String name, String phone, String city, String district,
                                     String commune, String address) {
        List<Cart> cartList = cartDao.getCartByUid(acc.getId());
        long total = cartDao.calculateTotalCartPrice(acc.getId());

        LocalDateTime currentDate = LocalDateTime.now();
        Order order = new Order();
        order.setUserId(acc);
        order.setName(name);
        order.setPhone(phone);
        order.setProvince(city);
        order.setDistrict(district);
        order.setCommune(commune);
        order.setDetailedAddress(address);
        order.setDate(Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant()));
        order.setTotal(total);
        order.setStatusid(orderDao.getStatusById(1));

        // Add order details from cart items
        List<OrderDetail> orderDetails = cartList.stream().map(cart -> {
            OrderDetail detail = new OrderDetail();
            detail.setPid(productDao.getProductById(cart.getPid()));
            detail.setNameProduct(cart.getName());
            ProductVariant variant = productDao.getProductVariantByID(cart.getVariantId());
            detail.setVariantId(variant);
            detail.setPrice(cart.getPrice());
            detail.setQuantity(cart.getQuantity());
            detail.setTotal(cart.getTotalOneProduct());
            return detail;
        }).toList();
        
        order.setOrderDetails(orderDetails);

        // Update product stock
        for (Cart cart : cartList) {
            productDao.updateProductQuantity(cart.getVariantId(), cart.getQuantity());
        }
        cartDao.clearCart(acc.getId());

        // Save order
        orderDao.addOrder(order);
        int orderId = orderDao.findLastOrderId(acc.getId());

        // Save payment information
        paymentDao.insertPayment(orderId, 1, new java.sql.Date(System.currentTimeMillis()), (int) total);

        // Send confirmation email
        try {
            sendEmail(acc.getEmail(), order, cartList);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "home";
    }

    private void sendEmail(String to, Order order, List<Cart> cartList) throws MessagingException, UnsupportedEncodingException {
        final String username = "HoLaTechSE1803@gmail.com";
        final String password = "xgdm ytoa shxw iwdk";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(MimeUtility.encodeText("Xác nhận đơn hàng", "UTF-8", "B"));

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Cảm ơn bạn đã mua sắm tại HoLaTech!\n")
                .append("Đơn hàng của bạn:\n\n")
                .append("Tên người nhận: ").append(order.getName()).append("\n")
                .append("Số điện thoại: ").append(order.getPhone()).append("\n")
                .append("Địa chỉ: ").append(order.getDetailedAddress()).append(", ")
                .append(order.getCommune()).append(", ")
                .append(order.getDistrict()).append(", ")
                .append(order.getProvince()).append("\n")
                .append("Phương thức thanh toán: Thanh toán khi nhận hàng.\n\n");

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (OrderDetail detail : order.getOrderDetails()) {
            emailContent.append("Tên sản phẩm: ").append(detail.getPid().getName())
                    .append("\nSố lượng: ").append(detail.getQuantity())
                    .append("\nGiá: ").append(currencyFormatter.format(detail.getPrice()))
                    .append("\nTổng: ").append(currencyFormatter.format(detail.getTotal()))
                    .append("\n\n");
        }
        emailContent.append("Tổng tiền đơn hàng: ").append(currencyFormatter.format(order.getTotal())).append("\n");

        message.setContent(emailContent.toString(), "text/plain; charset=UTF-8");

        Transport.send(message);
    }
}
