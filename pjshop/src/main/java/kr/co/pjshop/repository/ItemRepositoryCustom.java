package kr.co.pjshop.repository;


import kr.co.pjshop.dto.ItemSearchDto;
import kr.co.pjshop.dto.MainItemDto;
import kr.co.pjshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}