package service;

import dao.OrderDao;
import model.Order;

public class OrderService {

    private OrderDao orderDao;

    public OrderService() {
        orderDao = new OrderDao();
    }

    // Tạo đơn hàng mới
    public void addOrder(Order order) {
        orderDao.addOrder(order);
    }
}
