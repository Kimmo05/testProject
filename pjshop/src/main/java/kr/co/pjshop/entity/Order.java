package kr.co.pjshop.entity;

import kr.co.pjshop.constant.DeliveryStatus;
import kr.co.pjshop.constant.OrderStatus;
import kr.co.pjshop.exception.DeliveryException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private int totalPrice;

    private String payment;

    @ColumnDefault("100")
    private int usedMileagePrice;


    public void setMember(Member member) {
        this.member = member;
        member.getOrderList().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public static Order createOrder(Member member,Delivery delivery,List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setPayment("카드결제");
        order.setTotalPrice(order.getTotalPrice());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        if (this.delivery.getDeliveryStatus() == DeliveryStatus.COMPLETE) {
            throw new DeliveryException("이미 배송완료된 상품입니다.");
        } else {
//            this.setOrderStatus(OrderStatus.CANCEL);
            this.delivery.setDeliveryStatus(DeliveryStatus.CANCEL);
            for (OrderItem orderItem : orderItems) {
                orderItem.cancel();
            }
            this.orderStatus = OrderStatus.CANCEL;
            for (OrderItem orderItem : orderItems) {
                orderItem.cancel();
            }
        }

    }
}