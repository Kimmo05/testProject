package kr.co.pjshop.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.pjshop.constant.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String address;
    private LocalDateTime regTime;

    @QueryProjection
    public MemberDto(Long id, String name, String email, Role role, String address, LocalDateTime regTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.address = address;
        this.regTime = regTime;
    }
}// Querydsl을 위한 Dto

