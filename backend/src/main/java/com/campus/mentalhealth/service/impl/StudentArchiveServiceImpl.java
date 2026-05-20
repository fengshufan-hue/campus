package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.StudentArchive;
import com.campus.mentalhealth.mapper.StudentArchiveMapper;
import com.campus.mentalhealth.service.StudentArchiveService;
import org.springframework.stereotype.Service;

@Service
public class StudentArchiveServiceImpl extends ServiceImpl<StudentArchiveMapper, StudentArchive> implements StudentArchiveService {
}
