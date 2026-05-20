package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.Warning;
import com.campus.mentalhealth.mapper.WarningMapper;
import com.campus.mentalhealth.service.WarningService;
import org.springframework.stereotype.Service;

@Service
public class WarningServiceImpl extends ServiceImpl<WarningMapper, Warning> implements WarningService {
}
