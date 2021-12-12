package kr.co.pjshop.controller;


import kr.co.pjshop.constant.OrderStatus;
import kr.co.pjshop.dto.*;
import kr.co.pjshop.entity.DeliveryAddress;
import kr.co.pjshop.entity.Member;
import kr.co.pjshop.entity.SearchOrder;
import kr.co.pjshop.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberServiceImpl memberServiceImpl;
    private final DeliveryAddressServiceImpl deliveryAddressServiceImpl;
    private final ItemService itemService;

    private final CartService cartService;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto
            , BindingResult bindingResult, Principal principal,PaymentAddressDto paymentAddressDto, PaymentPriceDto paymentPriceDto){

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String loginId = principal.getName();
        Long orderId;

        try {
            orderId = orderService.order(orderDto,loginId,paymentAddressDto,paymentPriceDto);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId , Principal principal){

        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
    @GetMapping("/cart/payment")
    public String getPaymentDataPage(ItemSearchDto itemSearchDto, Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailList);
        String loginId = principal.getName();
        MyPageDto myPageDto = memberServiceImpl.showMySimpleInfo(loginId);
        List<DeliveryAddress> deliveryAddressList = deliveryAddressServiceImpl.getDeliveryAddressByLoginId(loginId);
        model.addAttribute("addressList", deliveryAddressList);
        model.addAttribute("member", myPageDto);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);

        return "order/orderCheckout";
    }

}