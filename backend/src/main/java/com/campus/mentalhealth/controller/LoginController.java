package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.mentalhealth.common.JwtUtil;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Admin;
import com.campus.mentalhealth.entity.Counselor;
import com.campus.mentalhealth.entity.PsychologyTeacher;
import com.campus.mentalhealth.entity.Student;
import com.campus.mentalhealth.service.AdminService;
import com.campus.mentalhealth.service.CounselorService;
import com.campus.mentalhealth.service.PsychologyTeacherService;
import com.campus.mentalhealth.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CounselorService counselorService;
    @Autowired
    private PsychologyTeacherService psychologyTeacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String role = params.get("role");
        String username = params.get("username");
        String password = params.get("password");

        if (role == null || username == null || password == null) {
            return Result.error("参数不完整：角色、账号、密码均为必填项");
        }

        Map<String, Object> userInfo = null;

        switch (role) {
            case "admin":
                Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq("admin_id", username));
                if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
                    userInfo = buildUserInfo(admin.getAdminId(), admin.getAdminName(), "admin");
                }
                break;
            case "counselor":
                Counselor counselor = counselorService.getOne(new QueryWrapper<Counselor>().eq("counselor_id", username));
                if (counselor != null && passwordEncoder.matches(password, counselor.getPassword())) {
                    userInfo = buildUserInfo(counselor.getCounselorId(), counselor.getCounselorName(), "counselor");
                }
                break;
            case "teacher":
                PsychologyTeacher teacher = psychologyTeacherService.getOne(new QueryWrapper<PsychologyTeacher>().eq("teacher_id", username));
                if (teacher != null && passwordEncoder.matches(password, teacher.getPassword())) {
                    userInfo = buildUserInfo(teacher.getTeacherId(), teacher.getTeacherName(), "teacher");
                }
                break;
            case "student":
                Student student = studentService.getOne(new QueryWrapper<Student>().eq("student_id", username));
                if (student != null && passwordEncoder.matches(password, student.getPassword())) {
                    userInfo = buildUserInfo(student.getStudentId(), student.getStudentName(), "student");
                }
                break;
            default:
                return Result.error("无效的用户类型");
        }

        if (userInfo != null) {
            return Result.success("登录成功", userInfo);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    private Map<String, Object> buildUserInfo(Long userId, String name, String role) {
        String token = JwtUtil.generateToken(userId, role);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("name", name);
        userInfo.put("role", role);
        userInfo.put("token", token);
        return userInfo;
    }
}
