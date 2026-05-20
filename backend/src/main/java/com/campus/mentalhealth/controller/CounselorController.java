package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Counselor;
import com.campus.mentalhealth.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public Result<Page<Counselor>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(required = false) String keyword) {
        if (pageSize > 100) pageSize = 100;
        Page<Counselor> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Counselor> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("counselor_name", keyword);
        }
        return Result.success(counselorService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    public Result<Counselor> getById(@PathVariable Long id) {
        return Result.success(counselorService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Counselor counselor) {
        if (counselor.getPassword() == null || counselor.getPassword().isEmpty()) {
            counselor.setPassword(passwordEncoder.encode("123456"));
        } else {
            counselor.setPassword(passwordEncoder.encode(counselor.getPassword()));
        }
        counselorService.save(counselor);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody Counselor counselor) {
        if (counselor.getPassword() != null && !counselor.getPassword().isEmpty()) {
            counselor.setPassword(passwordEncoder.encode(counselor.getPassword()));
        }
        counselorService.updateById(counselor);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!counselorService.removeById(id)) return Result.error("辅导员不存在");
        return Result.success("删除成功");
    }

    @PutMapping("/resetPwd/{id}")
    @Transactional
    public Result<?> resetPwd(@PathVariable Long id) {
        Counselor counselor = counselorService.getById(id);
        if (counselor == null) return Result.error("辅导员不存在");
        counselor.setPassword(passwordEncoder.encode("123456"));
        counselorService.updateById(counselor);
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
        Counselor counselor = counselorService.getById(userId);
        if (counselor == null) return Result.error("用户不存在");
        if (!passwordEncoder.matches(oldPwd, counselor.getPassword())) {
            return Result.error("密码不正确");
        }
        counselor.setPassword(passwordEncoder.encode(newPwd));
        counselorService.updateById(counselor);
        return Result.success("密码修改成功");
    }
}
