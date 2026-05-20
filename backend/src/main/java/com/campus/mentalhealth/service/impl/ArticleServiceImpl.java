package com.campus.mentalhealth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.mentalhealth.entity.Article;
import com.campus.mentalhealth.mapper.ArticleMapper;
import com.campus.mentalhealth.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}
