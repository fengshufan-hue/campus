package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.Assessment;
import com.campus.mentalhealth.mapper.AssessmentMapper;
import com.campus.mentalhealth.service.AssessmentService;
import org.springframework.stereotype.Service;

@Service
public class AssessmentServiceImpl extends ServiceImpl<AssessmentMapper, Assessment> implements AssessmentService {
}
