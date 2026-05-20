package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("consultation_schedule")
public class ConsultationSchedule {
    @TableId(type = IdType.AUTO)
    private Long scheduleId;
    private Long teacherId;
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxCount;
    private Integer bookedCount;
    private Integer status;
    private String location;
    @TableField(exist = false)
    private String teacherName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
