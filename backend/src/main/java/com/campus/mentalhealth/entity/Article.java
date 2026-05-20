package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long articleId;
    private String title;
    private String content;
    private String category;
    private Long authorId;
    private String authorName;
    private Integer viewCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
