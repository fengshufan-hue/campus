package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("student_archive")
public class StudentArchive {
    @TableId(type = IdType.AUTO)
    private Long archiveId;
    private Long studentId;
    private String mentalStatus;
    private Integer lastAssessmentScore;
    private LocalDateTime lastAssessmentDate;
    private Integer consultationCount;
    private Integer warningCount;
    private String remarks;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
