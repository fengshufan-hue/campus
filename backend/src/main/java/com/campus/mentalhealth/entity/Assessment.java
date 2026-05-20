package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("assessment")
public class Assessment {
    @TableId(type = IdType.AUTO)
    private Long assessmentId;
    private String title;
    private String description;
    private Integer status;
    private Integer passScore;
    private Long teacherId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
