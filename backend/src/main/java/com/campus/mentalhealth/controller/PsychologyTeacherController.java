package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.PsychologyTeacher;
import com.campus.mentalhealth.service.PsychologyTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class PsychologyTeacherController {

    @Autowired
    private PsychologyTeacherService psychologyTeacherService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public Result<Page<PsychologyTeacher>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false) String keyword) {
        if (pageSize > 100) pageSize = 100;
        Page<PsychologyTeacher> page = new Page<>(pageNum, pageSize);
        QueryWrapper<PsychologyTeacher> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("teacher_name", keyword);
        }
        return Result.success(psychologyTeacherService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    public Result<PsychologyTeacher> getById(@PathVariable Long id) {
        return Result.success(psychologyTeacherService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody PsychologyTeacher teacher) {
        if (teacher.getPassword() == null || teacher.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode("123456"));
        } else {
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        }
        psychologyTeacherService.save(teacher);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody PsychologyTeacher teacher) {
        if (teacher.getPassword() != null && !teacher.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        }
        psychologyTeacherService.updateById(teacher);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!psychologyTeacherService.removeById(id)) return Result.error("心理教师不存在");
        return Result.success("删除成功");
    }

    @PutMapping("/resetPwd/{id}")
    @Transactional
    public Result<?> resetPwd(@PathVariable Long id) {
        PsychologyTeacher teacher = psychologyTeacherService.getById(id);
        if (teacher == null) return Result.error("心理教师不存在");
        teacher.setPassword(passwordEncoder.encode("123456"));
        psychologyTeacherService.updateById(teacher);
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
        PsychologyTeacher teacher = psychologyTeacherService.getById(userId);
        if (teacher == null) return Result.error("用户不存在");
        if (!passwordEncoder.matches(oldPwd, teacher.getPassword())) {
            return Result.error("密码不正确");
        }
        teacher.setPassword(passwordEncoder.encode(newPwd));
        psychologyTeacherService.updateById(teacher);
        return Result.success("密码修改成功");
    }
}
