package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.ConsultationRecordEntity;
import com.campus.mentalhealth.mapper.ConsultationRecordMapper;
import com.campus.mentalhealth.service.ConsultationRecordService;
import org.springframework.stereotype.Service;

@Service
public class ConsultationRecordServiceImpl extends ServiceImpl<ConsultationRecordMapper, ConsultationRecordEntity> implements ConsultationRecordService {
}
