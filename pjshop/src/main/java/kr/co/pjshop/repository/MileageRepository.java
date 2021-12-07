package kr.co.pjshop.repository;


import kr.co.pjshop.entity.Member;
import kr.co.pjshop.entity.Mileage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, Long> {
    Page<Mileage> findAllByMember(Member member, Pageable pageable);
}
