package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import java.util.List;

public interface OrderService {
    void addOrder(Order o);
    void updateOrder(Order o);
    void deleteOrder(int id);
    List<Order> getAllOrders();
    Order findById(int id);
    double computeTotal(Order o);
    void addItem(Order o, OrderItem item);
    void removeItem(Order o, OrderItem item);
}
