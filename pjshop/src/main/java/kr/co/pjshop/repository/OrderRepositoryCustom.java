package kr.co.pjshop.repository;


import kr.co.pjshop.dto.MainPageOrderDto;
import kr.co.pjshop.dto.OrderDto;
import kr.co.pjshop.entity.SearchOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<OrderDto> searchAllOrder(Pageable pageable);

    Page<OrderDto> searchAllOrderByCondition(SearchOrder searchOrder, Pageable pageable);

    Page<MainPageOrderDto> mainPageSearchAllOrder(Pageable pageable, String loginId);

    Page<MainPageOrderDto> mainPageSearchAllOrderByCondition(SearchOrder searchOrder, Pageable pageable, String loginId);
}
