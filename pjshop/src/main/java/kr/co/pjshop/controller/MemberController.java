package kr.co.pjshop.controller;


import kr.co.pjshop.dto.*;
import kr.co.pjshop.entity.Member;
import kr.co.pjshop.entity.SearchMember;
import kr.co.pjshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
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
            memberPageDto = memberService.findAllMemberByPaging(pageable);
        } else {
            memberPageDto = memberService.findAllMemberByConditionByPaging(searchMember, pageable);
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
        model.addAttribute("Member", memberService.findMemberById(id));

        return "member/memberpage";
    }

    @ResponseBody
    @DeleteMapping("/admin/userList/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteById(id);

        return "회원 삭제 완료";
    }

    @ResponseBody
    @DeleteMapping("/admin/userList")
    public String deleteChecked(@RequestParam(value = "idList", required = false) List<Long> idList) {
        int size = idList.size();

        for (int i = 0; i < size; i++) {
            memberService.deleteById(idList.get(i));
        }
        return "선택된 회원 삭제 완료";
    }

    @GetMapping("/mypage")
    public String getMyPage(Principal principal, Model model) {
//        String email = principal.getName();
//
//        MyPageDto myPageDto = memberService.showMySimpleInfo(email);
//
//        model.addAttribute("member", myPageDto);

        return "member/membermypage";
    }
    @PutMapping("/update")
    public String editDataPage(Principal principal, @ModelAttribute("member") ProfileDto profileDto) {

        memberService.updateProfile(principal.getName(), profileDto);

        return "redirect:/member/membermypage";

    }
}