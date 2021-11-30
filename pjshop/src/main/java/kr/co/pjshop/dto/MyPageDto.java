package kr.co.pjshop.dto;


import kr.co.pjshop.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageDto {
    private String name;
    private String email;
    private Role role;
}
