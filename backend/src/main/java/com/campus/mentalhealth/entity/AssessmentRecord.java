package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("assessment_record")
public class AssessmentRecord {
    @TableId(type = IdType.AUTO)
    private Long recordId;
    private Long assessmentId;
    private Long studentId;
    private Integer totalScore;
    private String resultLevel;
    private String answers;
    private LocalDateTime submitTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
