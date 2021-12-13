package kr.co.pjshop.controller;

import kr.co.pjshop.dto.*;
import kr.co.pjshop.entity.DeliveryAddress;
import kr.co.pjshop.service.DeliveryAddressServiceImpl;
import kr.co.pjshop.service.ItemService;
import kr.co.pjshop.service.MemberServiceImpl;
import kr.co.pjshop.service.MileageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;
    private final MemberServiceImpl memberServiceImpl;
    private final MileageServiceImpl mileageServiceImpl;
    private final DeliveryAddressServiceImpl deliveryAddressServiceImpl;
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
    @GetMapping("/main/mileage")
    public String getMileagePage(Principal principal, Model model, @PageableDefault(size = 5) Pageable pageable) {
        String loginId = principal.getName();

        int totalMileage = mileageServiceImpl.getTotalMileage(loginId);
        int totalUsedMileage = mileageServiceImpl.getTotalUsedMileage(loginId);

        MileagePageDto mileagePagingDto = mileageServiceImpl.getMileagePagingDto(loginId, pageable);

        model.addAttribute("totalmileage", totalMileage);
        model.addAttribute("usedmileage", totalUsedMileage);
        model.addAttribute("restmileage", totalMileage - totalUsedMileage);

        model.addAttribute("startPage", mileagePagingDto.getHomeStartPage());
        model.addAttribute("endPage", mileagePagingDto.getHomeEndPage());

        model.addAttribute("mileageList", mileagePagingDto.getMileageBoards());

        return "main/mileage";
    }
    @GetMapping("/main/address")
    public String getAddressPage(Principal principal, Model model) {
        String loginId = principal.getName();
        List<DeliveryAddress> deliveryAddressList = deliveryAddressServiceImpl.getDeliveryAddressByLoginId(loginId);

        model.addAttribute("addressList", deliveryAddressList);

        return "member/address";
    }

    @GetMapping("/main/address/register")
    public String getRegisterPage() {
        return "member/addressForm";
    }

    @PostMapping("/main/address/register_ok")
    public String registerAddressPage(Principal principal, @ModelAttribute AddressDto addressDto) {
        String loginId = principal.getName();

        deliveryAddressServiceImpl.registerAddress(loginId, addressDto);

        return "redirect:/main/address";
    }

    @ResponseBody
    @DeleteMapping("/main/address/delete")
    public String deleteAddressPage(@RequestParam(value = "addressIdList", required = false) List<Long> addressIdList) {
        for (Long addressId : addressIdList) {
            deliveryAddressServiceImpl.deleteAddressById(addressId);
        }

        return "주소 삭제 완료";
    }

    @GetMapping("/main/address/change/{id}")
    public String getChangeAddressPage(@PathVariable Long id, Model model) {
        AddressChangeDto addressChangeDto = deliveryAddressServiceImpl.showAddressToChange(id);

        model.addAttribute("address", addressChangeDto);

        return "main/change_address";
    }

    @PutMapping("/main/changeaddress_ok")
    public String changeAddressStatus(@ModelAttribute AddressChangeDto addressChangeDto) {

        deliveryAddressServiceImpl.updateDeliveryAddress(addressChangeDto.getId(), addressChangeDto);

        return "redirect:/main/address";
    }
}