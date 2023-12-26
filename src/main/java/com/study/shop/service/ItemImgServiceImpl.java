package com.study.shop.service;

import com.study.shop.entity.ItemImg;
import com.study.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional  // DB 작업 처리하다가 에러 하나라도 발생하면 로직 수행 전 상태로 모두 되돌려줌
@RequiredArgsConstructor
@Log4j2
public class ItemImgServiceImpl implements ItemImgService {

    @Value("${itemImgLocation}")    // C:/Study/Project_documents/shop_documents/item_image
    private String itemImgLocation;
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Override
    public void saveItemImg(ItemImg itemImg, MultipartFile multipartFile) throws Exception {
        String originImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (!StringUtils.isEmpty(originImgName)) {  // 파일이 업로드 됐으면 (파일의 비어있지 않으면)
            // 상품 이미지를 등록했다면 저장할 경로, 파일 이름, 파일의 바이트 배열을 파라미터로 uploadFile 메서드 호출
            // uploadFile 메서드의 fileUploadFullUrl은 "C:/Study/Project_documents/shop_documents/item_image/UUID.jpg" 이런 식으로 되고 이 경로에 이미지 저장
            // 호출 결과 반환 값으로 로컬에 저장된 파일 이름(UUID.jpg)을 imgName 변수에 저장
            imgName = fileService.uploadFile(itemImgLocation, originImgName, multipartFile.getBytes());
            // 로컬에 저장한 상품 이미지를 웹에서 조회할 경로
            // ex) www.naver.com/images/item/UUID.jpg
            imgUrl = "/images/item/" + imgName;
        }

        itemImg.updateItemImg(originImgName, imgName, imgUrl);  // 이미지 정보 업데이트
        itemImgRepository.save(itemImg);    // 업데이트된 이미지 엔티티를 DB에 저장
    }

    @Override
    public void updateItemImg(Long itemImgId, MultipartFile multipartFile) throws Exception {
        // 상품 이미지를 수정한 경우 상품 이미지 업데이트
        if (!multipartFile.isEmpty()) {
            // 상품 이미지 id를 이용해 기존에 저장했던 상품 이미지 엔티티 조회
            // savedItemImg를 조회했기 때문에 영속성 컨텍스트에서 관리함
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            // 기존에 등록된 상품 이미지 파일이 있을 경우(파일 이름이 비어있지 않은 경우) 해당 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getName());
            }

            String originImgName = multipartFile.getOriginalFilename();
            // 업데이트한 상품 이미지 파일 업로드
            String imgName = fileService.uploadFile(itemImgLocation, originImgName, multipartFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            // 변경된 상품 이미지 정보 세팅
            // * itemImgRepository.save(savedItemImg) 호출 안 하는 이유?
            // savedItemImg 엔티티는 위에서 조회했기 때문에 영속성 컨텍스트에 의해 관리되는 "영속 상태"임
            // 영속 상태라면 데이터를 변경하는 것만으로 더티체킹(변경감지)이 동작 => 트랜잭션 끝날 때 자동으로 update 쿼리 실행돼서 DB에 반영 됨.
            savedItemImg.updateItemImg(originImgName, imgName, imgUrl);
        }
    }
}
