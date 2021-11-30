package kr.co.pjshop.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class MemberPageDto {
    Page<MemberDto> memberBoards;
    int homeStartPage;
    int homeEndPage;
    //현재 페이지의 번호(page)
    //페이지당 보여지는 데이터의 수(perPageNum)
    //검색의 종류(searchType)
    //검색의 키워드(keyword)
}
