package kr.co.pjshop.controller;


import kr.co.pjshop.dto.*;
import kr.co.pjshop.entity.Member;
import kr.co.pjshop.entity.SearchMember;
import kr.co.pjshop.service.MemberServiceImpl;
import kr.co.pjshop.service.MileageServiceImpl;
import kr.co.pjshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberServiceImpl;
    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;
  private final MileageServiceImpl mileageServiceImpl;

    @GetMapping(value = "/new")
    public String memberForm(){

        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(MemberInfoDto memberInfoDto){

        Long memberId = memberServiceImpl.joinUser(memberInfoDto);
        mileageServiceImpl.joinUserMileage(memberId);


        return "redirect:/";
    }



    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request, @RequestParam(value = "error", required = false) String error, Model model) {
        String referer = request.getHeader("Referer");

        if (referer != null) {
            request.getSession().setAttribute("prevPage", referer);
        } else {
            referer = "/";
            request.getSession().setAttribute("prevPage", referer);
        }
        model.addAttribute("error", error);
        return "/member/memberLoginForm";
    }
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    @GetMapping("/admin/userList")
    public String pageList(Model model, @PageableDefault(size = 4) Pageable pageable, SearchMember searchMember) {
        MemberPageDto memberPageDto = new MemberPageDto();

        if (searchMember.getSearchKeyword() == null) {
            memberPageDto = memberServiceImpl.findAllMemberByPaging(pageable);
        } else {
            memberPageDto = memberServiceImpl.findAllMemberByConditionByPaging(searchMember, pageable);
        }

        int homeStartPage = memberPageDto.getHomeStartPage();
        int homeEndPage = memberPageDto.getHomeEndPage();
        Page<MemberDto> memberBoards = memberPageDto.getMemberBoards();

        model.addAttribute("startPage", homeStartPage);
        model.addAttribute("endPage", homeEndPage);
        model.addAttribute("memberList", memberBoards);
        model.addAttribute("searchCondition", searchMember.getSearchCondition());
        model.addAttribute("searchKeyword", searchMember.getSearchKeyword());

        return "member/memberList";
    }
    @GetMapping("/admin/userList/user/{id}")
    public String pageUser(@PathVariable Long id, Model model) {
        model.addAttribute("Member", memberServiceImpl.findMemberById(id));

        return "member/memberpage";
    }

    @ResponseBody
    @DeleteMapping("/admin/userList/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberServiceImpl.deleteById(id);

        return "회원 삭제 완료";
    }

    @ResponseBody
    @DeleteMapping("/admin/userList")
    public String deleteChecked(@RequestParam(value = "idList", required = false) List<Long> idList) {
        int size = idList.size();

        for (int i = 0; i < size; i++) {
            memberServiceImpl.deleteById(idList.get(i));
        }
        return "선택된 회원 삭제 완료";
    }

    @GetMapping(value = {"/mypage", "/mypage/{page}"})
    public String getMyPage(Principal principal, Model model,@PathVariable("page") Optional<Integer> page,
                            @ModelAttribute("member") Member member) {
        String loginId = principal.getName();

        MyPageDto myPageDto = memberServiceImpl.showMySimpleInfo(loginId);
//        MyPageOrderStatusDto myPageOrderStatusDto = orderService.showOrderStatus(loginId);
        ProfileDto myProfileDto = memberServiceImpl.showProfileData(loginId); //내정보 수정 부분

        model.addAttribute("member", myPageDto);
//        model.addAttribute("orderStatus", myPageOrderStatusDto);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);
        model.addAttribute("member", myProfileDto);
        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "member/membermypage";
    }


    @GetMapping("/main/restrict")
    public String getRestrictPage() {
        return "restrict";
    }

    @GetMapping("/defaultUrl")
    public String loginRedirectPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        referer = "/";
        request.getSession().setAttribute("prevPage", referer);

        return "redirect:index";
    }

    @ResponseBody
    @PostMapping("/main/register/doublecheck")
    public String idDoubleCheckPage(@RequestParam(value = "registerId") String registerId) {
        if (memberServiceImpl.doubleCheckId(registerId)) {
            return "사용할 수 없는 아이디입니다.";
        } else {
            return "사용할 수 있는 아이디입니다.";
        }
    }



    @PutMapping("/update")
    public String editDataPage(Principal principal, @ModelAttribute("member") ProfileDto profileDto) {

        memberServiceImpl.updateProfile(principal.getName(), profileDto);

        return "redirect:/mypage";
    }
}