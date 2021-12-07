package kr.co.pjshop.service;


import kr.co.pjshop.constant.OrderStatus;
import kr.co.pjshop.entity.OrderItem;

public interface OrderItemService {
    OrderItem findOrderItemById(Long id);

    void chagneOrderStatus(Long id, OrderStatus orderStatus);
}
