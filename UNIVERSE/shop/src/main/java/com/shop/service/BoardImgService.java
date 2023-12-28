package com.shop.service;

import com.shop.entity.BoardImg;
import com.shop.repository.BoardImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
@Service
@RequiredArgsConstructor
@Transactional
public class BoardImgService {

        @Value("${boardImgLocation}")
        private String boardImgLocation;
        private final BoardImgRepository boardImgRepository;
        private final FileService fileService;

        public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception{
            String oriImgName = boardImgFile.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";

            if (!StringUtils.isEmpty(oriImgName)){
                imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
                // 저장된 상품 이미지를 불러올 경로를 설정
                imgUrl = "/images/board/" + imgName;
            }

            // 상품 이미지 정보를 저장
            boardImg.updateBoardImg(oriImgName, imgName, imgUrl);
            boardImgRepository.save(boardImg);
        }

        // 상품 이미지 변경하는 메서드
        // 기존 이미지의 id와 새로 업로드하는 이미지 파일을 매개변수로 전달
        public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception{
            // 새로 업로드된 이미지가 공백이 아니면
            if (!boardImgFile.isEmpty()){
                // 기존 이미지 id를 이용하여 저장된 데이터가 있는지 확인
                BoardImg savedBoardImg = boardImgRepository.findById(boardImgId)
                        .orElseThrow(EntityNotFoundException::new);

                // 기존 이미지 파일 삭제
                if(!StringUtils.isEmpty(savedBoardImg.getImgName())){
                    fileService.deleteFile(boardImgLocation + "/" + savedBoardImg.getImgName());
                }

                // 새로운 이미지 업로드
                String oriImgName = boardImgFile.getOriginalFilename();
                String imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
                String imgUrl = "/images/board/" + imgName;
                savedBoardImg.updateBoardImg(oriImgName, imgName, imgUrl);
            }
        }
}
