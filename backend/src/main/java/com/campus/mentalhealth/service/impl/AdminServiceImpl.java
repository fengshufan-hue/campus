package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.Admin;
import com.campus.mentalhealth.mapper.AdminMapper;
import com.campus.mentalhealth.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
