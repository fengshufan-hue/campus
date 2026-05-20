package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("feedback")
public class Feedback {
    @TableId(type = IdType.AUTO)
    private Long feedbackId;
    private Long bookingId;
    private Long studentId;
    private Long teacherId;
    private Integer rating;
    private String content;
    private Integer isAnonymous;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
