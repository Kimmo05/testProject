package kr.co.pjshop.dto;

import kr.co.pjshop.constant.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileDto {
    private Long id;
    private String name;
    private String password;
    private String email;
    private Role role;
    private String phoneNumber;
    private LocalDateTime regTime;
}
