package kr.co.pjshop.repository;


import kr.co.pjshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}