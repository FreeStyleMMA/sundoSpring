package com.sundostudio.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sundostudio.dto.EquipmentDto;
import com.sundostudio.mapper.EquipmentMapper;

import lombok.Setter;
@Service
public class EquipmentServiceImpl implements EquipmentService {
	@Setter(onMethod_ = @Autowired)
	private EquipmentMapper mapper;

	 @Override
	    public void upload(EquipmentDto equipment) {
	        mapper.upload(equipment);
	    }
}
