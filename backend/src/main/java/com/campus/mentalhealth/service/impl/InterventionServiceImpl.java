package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.Intervention;
import com.campus.mentalhealth.mapper.InterventionMapper;
import com.campus.mentalhealth.service.InterventionService;
import org.springframework.stereotype.Service;

@Service
public class InterventionServiceImpl extends ServiceImpl<InterventionMapper, Intervention> implements InterventionService {
}
