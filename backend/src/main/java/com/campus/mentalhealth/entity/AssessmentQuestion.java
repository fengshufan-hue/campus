package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("assessment_question")
public class AssessmentQuestion {
    @TableId(type = IdType.AUTO)
    private Long questionId;
    private Long assessmentId;
    private String questionContent;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Integer scoreA;
    private Integer scoreB;
    private Integer scoreC;
    private Integer scoreD;
    private Integer questionOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
