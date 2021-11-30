package kr.co.pjshop.repository;


import kr.co.pjshop.dto.MemberDto;
import kr.co.pjshop.entity.SearchMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<MemberDto> searchAll(Pageable pageable);

    Page<MemberDto> searchByCondition(SearchMember search, Pageable pageable);
}
