package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.AssessmentRecord;
import com.campus.mentalhealth.mapper.AssessmentRecordMapper;
import com.campus.mentalhealth.service.AssessmentRecordService;
import org.springframework.stereotype.Service;

@Service
public class AssessmentRecordServiceImpl extends ServiceImpl<AssessmentRecordMapper, AssessmentRecord> implements AssessmentRecordService {
}
