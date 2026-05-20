package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("psychology_teacher")
public class PsychologyTeacher {
    @TableId(type = IdType.AUTO)
    private Long teacherId;
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String teacherName;
    private String gender;
    private Integer age;
    private String phoneNum;
    private String email;
    private String avatar;
    private String title;
    private String specialty;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
