package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Admin;
import com.campus.mentalhealth.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public Result<Page<Admin>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(required = false) String keyword) {
        if (pageSize > 100) pageSize = 100;
        Page<Admin> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("admin_name", keyword);
        }
        return Result.success(adminService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    public Result<Admin> getById(@PathVariable Long id) {
        return Result.success(adminService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Admin admin) {
        if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode("123456"));
        } else {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        adminService.save(admin);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody Admin admin) {
        // If password is being updated, encrypt it
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        adminService.updateById(admin);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!adminService.removeById(id)) return Result.error("用户不存在");
        return Result.success("删除成功");
    }

    @PutMapping("/resetPwd/{id}")
    @Transactional
    public Result<?> resetPwd(@PathVariable Long id) {
        Admin admin = adminService.getById(id);
        if (admin == null) return Result.error("用户不存在");
        admin.setPassword(passwordEncoder.encode("123456"));
        adminService.updateById(admin);
        return Result.success(admin.getAdminName() + "的密码已重置为123456");
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
        Admin admin = adminService.getById(userId);
        if (admin == null) return Result.error("用户不存在");
        if (!passwordEncoder.matches(oldPwd, admin.getPassword())) {
            return Result.error("密码不正确");
        }
        admin.setPassword(passwordEncoder.encode(newPwd));
        adminService.updateById(admin);
        return Result.success("密码修改成功");
    }
}
