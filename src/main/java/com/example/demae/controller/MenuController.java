package com.example.demae.controller;

import com.example.demae.dto.menu.MenuRequestDto;
import com.example.demae.dto.menu.MenuResponseDto;
import com.example.demae.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demae.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class MenuController {
	private final MenuService menuService;
  
    @GetMapping("/{storeId}/menu")
    public String home(Model model,@PathVariable Long storeId){
        model.addAttribute("storeId", storeId);
        return "menu";
    }
    @PostMapping("/{storeId}/createMenu")
    public String createMenu(@RequestParam("name") String name,
                             @RequestParam("price") int price,
                             @RequestParam("files") List<MultipartFile> file,
                             @AuthenticationPrincipal UserDetailsImpl userDetails,
                             @PathVariable Long storeId,
                             Model model) throws IOException {
        String email = userDetails.getUser().getEmail();
        String message = menuService.createMenu(storeId,file,price,name,email);
        if(!message.equals("성공")){
            throw new IllegalArgumentException("실패했습니다.");
        }
        model.addAttribute("storeId",storeId);
        return "showMenuPage";
    }
    @GetMapping("/{storeId}/allMenu")
    public String AllMenu(Model model,@PathVariable Long storeId,
                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<MenuResponseDto> allMenu = menuService.AllMenu(storeId);
        model.addAttribute("menuList", allMenu);
        if (userDetails.getUser().getRole().name().equals("ADMIN") &&
                userDetails.getUser().getStore().getId() == storeId) {
            return "showMenuPage";
        }
        return "showMenuPageUser";
    }
    @GetMapping("/{storeId}/selectMenu/{menuId}")
    public String selectMenu(Model model,
                             @PathVariable Long storeId,
                             @PathVariable Long menuId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        MenuResponseDto selectMenu = menuService.selectMenu(storeId,menuId);
        model.addAttribute("menuOne", selectMenu);
        if (userDetails.getUser().getRole().name().equals("ADMIN") &&
                userDetails.getUser().getStore().getId() == storeId) {
            return "showSelectMenu";
        }
        return "showSelectMenuUser";
    }
    @PatchMapping("/{storeId}/patchMenu/{menuId}")
    public String patchMemu(@PathVariable Long storeId,
                            @PathVariable Long menuId,
                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                            @RequestBody MenuRequestDto menuRequestDto){
        String email = userDetails.getUser().getEmail();
        menuService.patchMenu(storeId,menuId,menuRequestDto,email);

        return "showMenuPage";
    }
    @DeleteMapping("/{storeId}/deleteMenu/{menuId}")
    public String deleteMenu(@PathVariable Long storeId,
                             @PathVariable Long menuId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        String email = userDetails.getUser().getEmail();
        menuService.deleteMenu(storeId,menuId,email);
        return "showMenuPage";
    }
    @PostMapping("/{storeId}/selectMenu/{menuId}/createPicture")
    public String createPicture(@PathVariable Long storeId,
                                @PathVariable Long menuId,
                                @RequestParam("files") List<MultipartFile> file,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String email = userDetails.getUser().getEmail();
        menuService.createPicture(storeId,menuId,file,email);
        return "showMenuPage";
    }
    @DeleteMapping("/{storeId}/selectMenu/{menuId}/deletePicture")
    public String deletePicture(@PathVariable Long storeId,
                                @PathVariable Long menuId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        String email = userDetails.getUser().getEmail();
        menuService.deletePicture(storeId,menuId,email);
        return "showMenuPage";
    }
}
