package kr.co.pjshop.dto;

import kr.co.pjshop.constant.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileDto {
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String city;
    private String street;
    private String zipcode;
    private String[] homePhoneNumber;
    private String[] phoneNumber;
    private String[] birthday;
    private String email;
    private Role role;
    private LocalDateTime regTime;
    private int mileage;
}
