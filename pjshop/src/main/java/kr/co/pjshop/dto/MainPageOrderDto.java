package kr.co.pjshop.dto;


import com.querydsl.core.annotations.QueryProjection;
import kr.co.pjshop.constant.OrderStatus;
import kr.co.pjshop.entity.QItemImg;
import kr.co.pjshop.entity.QMember;
import kr.co.pjshop.entity.QOrder;
import kr.co.pjshop.entity.QOrderItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class MainPageOrderDto {
    private Long id;
    private Long orderItemId;
    private Long itemId;
    private String name;
    private String itemNm;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private int price;
    private int count;
    private String  imgUrl;



    @QueryProjection
    public MainPageOrderDto(Long id, Long orderItemId, Long itemId, String name, String itemNm, LocalDateTime orderDate, OrderStatus orderStatus, int price, int count, String  imgUrl) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.itemId = itemId;
        this.name = name;
        this.itemNm = itemNm;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;

    }
}
