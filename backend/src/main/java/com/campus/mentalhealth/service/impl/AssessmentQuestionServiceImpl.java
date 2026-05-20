package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.AssessmentQuestion;
import com.campus.mentalhealth.mapper.AssessmentQuestionMapper;
import com.campus.mentalhealth.service.AssessmentQuestionService;
import org.springframework.stereotype.Service;

@Service
public class AssessmentQuestionServiceImpl extends ServiceImpl<AssessmentQuestionMapper, AssessmentQuestion> implements AssessmentQuestionService {
}
