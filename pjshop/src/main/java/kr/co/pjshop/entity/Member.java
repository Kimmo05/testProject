package kr.co.pjshop.entity;


import kr.co.pjshop.constant.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="members")
@Getter @Setter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String loginId;

    private String email;

    private String password;

    @Embedded
    private MemberAddress memberAddress;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String birthday;

    private int visitCount;

    private int orderCount;

    private String phoneNumber;

    private String homePhoneNumber;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Mileage> mileageList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<DeliveryAddress> deliveryAddressList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();

    public Member(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }
    @Builder
    public Member(Long id, String loginId, String password, String name, String homePhoneNumber, String phoneNumber, String email, String birthday) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.homePhoneNumber = homePhoneNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
    }


}
