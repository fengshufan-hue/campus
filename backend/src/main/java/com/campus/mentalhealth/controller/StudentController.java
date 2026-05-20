package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Student;
import com.campus.mentalhealth.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public Result<Page<Student>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) Long counselorId) {
        if (pageSize > 100) pageSize = 100;
        Page<Student> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("student_name", keyword).or().like("student_id", keyword);
        }
        if (counselorId != null) {
            wrapper.eq("counselor_id", counselorId);
        }
        return Result.success(studentService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    public Result<Student> getById(@PathVariable Long id) {
        return Result.success(studentService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Student student) {
        if (student.getPassword() == null || student.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode("123456"));
        } else {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }
        studentService.save(student);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody Student student) {
        if (student.getPassword() != null && !student.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }
        studentService.updateById(student);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!studentService.removeById(id)) return Result.error("学生不存在");
        return Result.success("删除成功");
    }

    @PutMapping("/resetPwd/{id}")
    @Transactional
    public Result<?> resetPwd(@PathVariable Long id) {
        Student student = studentService.getById(id);
        if (student == null) return Result.error("学生不存在");
        student.setPassword(passwordEncoder.encode("123456"));
        studentService.updateById(student);
        return Result.success("密码已重置为123456");
    }

    @PutMapping("/changePwd")
    @Transactional
    public Result<?> changePwd(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Long userId = (Long) request.getAttribute("userId");
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");
        if (oldPwd == null || oldPwd.isEmpty()) {
            return Result.error("请输入旧密码");
        }
        if (newPwd == null || newPwd.length() < 6) {
            return Result.error("新密码至少6位");
        }
        Student student = studentService.getById(userId);
        if (student == null) return Result.error("用户不存在");
        if (!passwordEncoder.matches(oldPwd, student.getPassword())) {
            return Result.error("密码不正确");
        }
        student.setPassword(passwordEncoder.encode(newPwd));
        studentService.updateById(student);
        return Result.success("密码修改成功");
    }
}
