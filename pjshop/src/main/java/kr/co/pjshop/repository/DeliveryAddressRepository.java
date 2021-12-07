package kr.co.pjshop.repository;

import kr.co.pjshop.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    List<DeliveryAddress> findAllByMemberLoginId(String loginId);
}
