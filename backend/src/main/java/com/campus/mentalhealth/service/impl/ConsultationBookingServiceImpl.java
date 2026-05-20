package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.ConsultationBooking;
import com.campus.mentalhealth.mapper.ConsultationBookingMapper;
import com.campus.mentalhealth.service.ConsultationBookingService;
import org.springframework.stereotype.Service;

@Service
public class ConsultationBookingServiceImpl extends ServiceImpl<ConsultationBookingMapper, ConsultationBooking> implements ConsultationBookingService {
}
