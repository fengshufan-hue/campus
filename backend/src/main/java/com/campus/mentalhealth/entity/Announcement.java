package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("announcement")
public class Announcement {
    @TableId(type = IdType.AUTO)
    private Long announcementId;
    private String title;
    private String content;
    private String author;
    private String type;
    private Integer status;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
