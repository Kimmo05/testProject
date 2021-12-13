package kr.co.pjshop.service;


import kr.co.pjshop.constant.OrderStatus;
import kr.co.pjshop.entity.OrderItem;
import kr.co.pjshop.exception.ItemNotFoundException;
import kr.co.pjshop.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItem findOrderItemById(Long id) {
        OrderItem findOrderItem = orderItemRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("해당하는 상품이 존재하지 않습니다")
        );
        return findOrderItem;
    }


    @Override
    @Transactional
    public void chagneOrderStatus(Long id, OrderStatus orderStatus) {
        OrderItem findOrderItem = orderItemRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("해당하는 상품이 존재하지 않습니다")
        );
        findOrderItem.setOrderStatus(orderStatus);
    }


}
