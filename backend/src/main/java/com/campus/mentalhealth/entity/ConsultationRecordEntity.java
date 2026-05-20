package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("consultation_record")
public class ConsultationRecordEntity {
    @TableId(type = IdType.AUTO)
    private Long recordId;
    private Long bookingId;
    private Long studentId;
    private Long teacherId;
    private LocalDateTime consultDate;
    private String consultTopic;
    private String consultContent;
    private String consultSummary;
    private String followUp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
