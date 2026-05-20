package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.ConsultationSchedule;
import com.campus.mentalhealth.mapper.ConsultationScheduleMapper;
import com.campus.mentalhealth.service.ConsultationScheduleService;
import org.springframework.stereotype.Service;

@Service
public class ConsultationScheduleServiceImpl extends ServiceImpl<ConsultationScheduleMapper, ConsultationSchedule> implements ConsultationScheduleService {
}
