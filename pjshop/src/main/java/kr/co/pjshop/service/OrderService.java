package kr.co.pjshop.service;


import kr.co.pjshop.constant.DeliveryStatus;
import kr.co.pjshop.constant.OrderStatus;
import kr.co.pjshop.dto.*;
import kr.co.pjshop.entity.*;
import kr.co.pjshop.exception.LoginIdNotFoundException;
import kr.co.pjshop.repository.ItemImgRepository;
import kr.co.pjshop.repository.ItemRepository;
import kr.co.pjshop.repository.MemberRepository;
import kr.co.pjshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String loginId, PaymentAddressDto paymentAddressDto, PaymentPriceDto paymentPriceDto){

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Optional<Member> member = memberRepository.findByloginId(loginId);
        Delivery delivery = new Delivery();

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setCity(paymentAddressDto.getCity());
        memberAddress.setStreet(paymentAddressDto.getStreet());
        memberAddress.setZipcode(paymentAddressDto.getZipcode());
        delivery.setMemberAddress(memberAddress);
        delivery.setDeliveryStatus(DeliveryStatus.READY);


        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(member.get(),delivery, orderItemList);
        Mileage mileage = new Mileage();
        mileage.setMileageContent("구매 적립금");
        mileage.setMileagePrice(paymentPriceDto.getTobepaid_price() / 100);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String loginId, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(loginId, pageable);
        Long totalCount = orderRepository.countOrder(loginId);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String loginId){
        Optional<Member> curMember = memberRepository.findByloginId(loginId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.get().getLoginId(), savedMember.getLoginId())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String loginId){

        Optional<Member> member = memberRepository.findByloginId(loginId);
        List<OrderItem> orderItemList = new ArrayList<>();
        Delivery delivery = new Delivery();
        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member.get(),delivery,orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

}