package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("intervention")
public class Intervention {
    @TableId(type = IdType.AUTO)
    private Long interventionId;
    private Long warningId;
    private Long studentId;
    private Long counselorId;
    private String interventionType;
    private String interventionContent;
    private String interventionResult;
    private LocalDateTime interventionDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
