package com.example.demae.service;

import com.example.demae.dto.menu.MenuRequestDto;
import com.example.demae.dto.menu.MenuResponseDto;
import com.example.demae.entity.Menu;
import com.example.demae.entity.Picture;
import com.example.demae.repository.MenuRepository;
import com.example.demae.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demae.entity.Store;
import com.example.demae.repository.StoreRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
     private final MenuRepository menuRepository;
     private final StoreRepository storeRepository;
     private final AwsS3Service awsS3Service;

    public String createMenu(Long storeId, List<MultipartFile> fies, int price,String name , String email) throws IOException {
        Store store = storeRepository.findById(storeId).orElseThrow(()->new IllegalArgumentException("본인 가게가 없습니다."));
        if(!store.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("본인의 가게가 아닙니다.");
        }
        Menu menu = new Menu(price,name,store);
        Menu save = menuRepository.save(menu);
        awsS3Service.uploadFiles(fies,save.getId());
        return "성공";
    }

    public List<MenuResponseDto> AllMenu(Long storeId) {
        List<Menu> storeCheck = menuRepository.findByStoreId(storeId);
        List<MenuResponseDto> menuResponseDto = new ArrayList<>();;
        for (Menu menu : storeCheck) {
            List<String> pictureUrls = awsS3Service.getObjectUrlsForMenu(menu.getId());
            menuResponseDto.add(new MenuResponseDto(menu, pictureUrls));
        }
        return menuResponseDto;
    }
    public MenuResponseDto selectMenu(Long storeId,Long MenuId) {
        List<Menu> storeCheck = menuRepository.findByStoreId(storeId);
        if(storeCheck.isEmpty()){
            throw new IllegalArgumentException("가게가 일치하지 않습니다.");
        }
        Menu checkMenu = menuRepository.findById(MenuId).orElseThrow(()->new IllegalArgumentException("메뉴가 없습니다."));
        List<String> picture = awsS3Service.getObjectUrlsForMenu(MenuId);
        return new MenuResponseDto(checkMenu,picture);
    }

    @Transactional
    public void patchMenu(Long storeId,Long menuId,MenuRequestDto menuRequestDto,String email) {
        Store findStore = storeRepository.findById(storeId).orElseThrow(()-> new IllegalArgumentException("가게 정보가 없습니다."));
        Menu menu = menuRepository.findById(menuId).orElseThrow(()->new IllegalArgumentException("메뉴가 없습니다."));
        if(!findStore.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("본인의 가게가 아닙니다.");
        }
        menu.update(menuRequestDto.getName(),menuRequestDto.getPrice());
    }

    public void deleteMenu(Long storeId,Long menuId,String email) {
        Store FindStore = storeRepository.findById(storeId).orElseThrow(()->new IllegalArgumentException("가게 정보가 없습니다"));
        Menu findMenu = menuRepository.findById(menuId).orElseThrow(()->new IllegalArgumentException("본인의 메뉴가 아닙니다."));
        if(!FindStore.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("본인의 가게가 아닙니다.");
        }
        menuRepository.delete(findMenu);
    }


    public void createPicture(Long storeId,Long menuId, List<MultipartFile> file,String email) throws IOException {
        Store store = storeRepository.findById(storeId).orElseThrow(()-> new IllegalArgumentException("가게 정보가 없습니다."));
        if(!store.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("본인의 가게가 아닙니다.");
        }
        awsS3Service.uploadFiles(file,menuId);
    }

    public void deletePicture(Long storeId, Long menuId,String email) {
        Store store = storeRepository.findById(storeId).orElseThrow(()-> new IllegalArgumentException("가게 정보가 없습니다."));
        if(!store.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("본인의 가게가 아닙니다.");
        }
        awsS3Service.deleteFilesByMenuId(menuId);
    }
}

