package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long studentId;
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String studentName;
    private String gender;
    private Integer age;
    private String phoneNum;
    private String email;
    private String avatar;
    private String college;
    private String major;
    private String grade;
    private String className;
    private Long counselorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
