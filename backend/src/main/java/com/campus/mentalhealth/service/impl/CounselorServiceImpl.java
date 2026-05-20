package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.Counselor;
import com.campus.mentalhealth.mapper.CounselorMapper;
import com.campus.mentalhealth.service.CounselorService;
import org.springframework.stereotype.Service;

@Service
public class CounselorServiceImpl extends ServiceImpl<CounselorMapper, Counselor> implements CounselorService {
}
