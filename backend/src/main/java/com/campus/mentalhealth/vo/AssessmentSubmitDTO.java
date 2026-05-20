package com.campus.mentalhealth.vo;

import lombok.Data;

import java.util.Map;

@Data
public class AssessmentSubmitDTO {
    private Long assessmentId;
    private Long studentId;
    private Map<String, Object> answers;
}
