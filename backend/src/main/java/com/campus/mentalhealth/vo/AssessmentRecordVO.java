package com.campus.mentalhealth.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssessmentRecordVO {
    private String studentName;
    private String assessmentName;
    private Integer totalScore;
    private String resultLevel;
    private LocalDateTime submitTime;
}
