package com.sundostudio.controller;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sundostudio.dto.EquipmentDto;
import com.sundostudio.service.EquipmentService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j	
@RestController
@RequestMapping("/equipments/*")
@CrossOrigin(origins = "http://localhost:3000")


public class EquipmentController {
	@Setter(onMethod_ = @Autowired)
	private EquipmentService service;

	@PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String upload(
	@RequestPart("equipment") EquipmentDto equipment,
	@RequestPart("image") MultipartFile imageFile) throws Exception {
	log.info("업로드 요청 도착: " + equipment);
	log.info("업로드 파일 이름: " + imageFile.getOriginalFilename());
	
	// 1. UUID 생성 + 확장자 추출
	String uuid = UUID.randomUUID().toString();
	String originalFilename = imageFile.getOriginalFilename();
	String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
	String savedFileName = uuid + ext;

	// 2. 저장 경로 (서버 내 폴더)
	String saveDir = "C:/upload/";
	File saveFile = new File(saveDir + savedFileName);

	// 3. 폴더 없으면 생성
	if (!saveFile.getParentFile().exists()) {
	    saveFile.getParentFile().mkdirs();
	}

	// 4. 파일 저장
	imageFile.transferTo(saveFile);

	// 5. 이미지 경로 세팅 (DB 저장용)
	equipment.setImagePath("/upload/" + savedFileName);

	// 6. 서비스 호출해서 DB 저장
	service.upload(equipment);

	return "업로드 성공";
	
	
	}

}
