package com.campus.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("counselor")
public class Counselor {
    @TableId(type = IdType.AUTO)
    private Long counselorId;
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String counselorName;
    private String gender;
    private Integer age;
    private String phoneNum;
    private String email;
    private String avatar;
    private String department;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
