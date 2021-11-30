package kr.co.pjshop.controller;

import kr.co.pjshop.dto.ItemSearchDto;
import kr.co.pjshop.dto.MainItemDto;
import kr.co.pjshop.dto.MyPageDto;
import kr.co.pjshop.dto.MyPageOrderStatusDto;
import kr.co.pjshop.service.ItemService;
import kr.co.pjshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;
    private final MemberService memberService;
    @GetMapping(value = "/admin/main")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        return "admin_main";
    }

    @GetMapping(value = "/")
    public String mainPage(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

    @GetMapping(value = "/itemMain")
    public String itemmain(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMain";

    }

}