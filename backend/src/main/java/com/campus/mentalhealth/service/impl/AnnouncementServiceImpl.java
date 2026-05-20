package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.Announcement;
import com.campus.mentalhealth.mapper.AnnouncementMapper;
import com.campus.mentalhealth.service.AnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {
}
