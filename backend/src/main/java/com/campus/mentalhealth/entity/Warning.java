package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("warning")
public class Warning {
    @TableId(type = IdType.AUTO)
    private Long warningId;
    private Long studentId;
    private String warningLevel;
    private String warningReason;
    private String sourceType;
    private Long sourceId;
    private Integer status;
    private Long counselorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
