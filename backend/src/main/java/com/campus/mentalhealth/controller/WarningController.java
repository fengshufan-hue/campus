package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Warning;
import com.campus.mentalhealth.service.WarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warning")
public class WarningController {

    @Autowired
    private WarningService warningService;

    @GetMapping("/list")
    public Result<Page<Warning>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) Long studentId,
                                        @RequestParam(required = false) Long counselorId,
                                        @RequestParam(required = false) Integer status,
                                        @RequestParam(required = false) String level) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Warning> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Warning> wrapper = new QueryWrapper<>();
        if (studentId != null) {
            wrapper.eq("student_id", studentId);
        }
        if (counselorId != null) {
            wrapper.and(w -> w.isNull("counselor_id").or().eq("counselor_id", counselorId));
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (level != null && !level.isEmpty()) {
            wrapper.eq("warning_level", level);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(warningService.page(page, wrapper));
    }

    @PostMapping
    public Result<?> save(@RequestBody Warning warning) {
        warningService.save(warning);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody Warning warning) {
        warningService.updateById(warning);
        return Result.success("更新成功");
    }

    @PutMapping("/assign")
    @Transactional
    public Result<?> assign(@RequestParam Long warningId, @RequestParam Long counselorId) {
        if (warningId == null || counselorId == null) {
            return Result.error("参数不能为空");
        }
        Warning warning = warningService.getById(warningId);
        if (warning == null) {
            return Result.error("预警不存在");
        }
        warning.setCounselorId(counselorId);
        warning.setStatus(1);
        warningService.updateById(warning);
        return Result.success("分配成功");
    }

    @GetMapping("/{id}")
    public Result<Warning> getById(@PathVariable Long id) {
        return Result.success(warningService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!warningService.removeById(id)) return Result.error("预警不存在");
        return Result.success("删除成功");
    }
}
