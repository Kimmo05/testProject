package kr.co.pjshop.dto;


import kr.co.pjshop.entity.Mileage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class MileagePageDto {
    Page<Mileage> mileageBoards;
    int homeStartPage;
    int homeEndPage;
}
