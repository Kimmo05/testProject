package kr.co.pjshop.entity;

import kr.co.pjshop.constant.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private MemberAddress memberAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

}
