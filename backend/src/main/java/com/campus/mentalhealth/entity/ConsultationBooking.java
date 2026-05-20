package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("consultation_booking")
public class ConsultationBooking {
    @TableId(type = IdType.AUTO)
    private Long bookingId;
    private Long scheduleId;
    private Long studentId;
    private Long teacherId;
    private String reason;
    private Integer status;
    private String teacherReply;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
