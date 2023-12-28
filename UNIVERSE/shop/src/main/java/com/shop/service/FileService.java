package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    // 파일 업로드 메서드
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws  Exception{
        // 고유한 파일 이름을 생성
        // UUID : 범용으로 사용되는 고유 식별자를 의미
        // 128비트, 16진수
        UUID uuid = UUID.randomUUID();
        // 원본 파일명에서 마지막, 이후의 문자열을 추출(확장자를 가져옴)
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // UUID와 확장자를 결합하여 새로운 파일명을 생성
        String savedFileName = uuid.toString() + extension;
        // 파일이 업로드될 전체 경로를 생성
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        // 입출력스트림을 생성
        // 파일 경로에 해당하는 파일을 생성 또는 덮어쓰기
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        // fileData의 내용을 파일 출력 스트림에 기록
        fos.write(fileData);
        // 파일 출력스트림 닫기
        fos.close();
        // 저장된 파일명을 반환
        return savedFileName;
    }

    // 파일 삭제 메서드
    public void deleteFile(String filePath) throws Exception{
        // 파일이 저장되어있는 경로를 이용하여 파일 객체 생성
        File deleteFile = new File(filePath);
        // 파일이 존재하면
        if (deleteFile.exists()){
            // 파일을 삭제
            deleteFile.delete();
            // 메세지를 출력
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
