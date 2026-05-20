package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Intervention;
import com.campus.mentalhealth.service.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/intervention")
public class InterventionController {

    @Autowired
    private InterventionService interventionService;

    @GetMapping("/list")
    public Result<Page<Intervention>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false) Long studentId,
                                             @RequestParam(required = false) Long counselorId,
                                             @RequestParam(required = false) Long warningId) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Intervention> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Intervention> wrapper = new QueryWrapper<>();
        if (studentId != null) {
            wrapper.eq("student_id", studentId);
        }
        if (counselorId != null) {
            wrapper.eq("counselor_id", counselorId);
        }
        if (warningId != null) {
            wrapper.eq("warning_id", warningId);
        }
        wrapper.orderByDesc("intervention_date");
        return Result.success(interventionService.page(page, wrapper));
    }

    @PostMapping
    public Result<?> save(@RequestBody Intervention intervention) {
        interventionService.save(intervention);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody Intervention intervention) {
        interventionService.updateById(intervention);
        return Result.success("更新成功");
    }

    @GetMapping("/{id}")
    public Result<Intervention> getById(@PathVariable Long id) {
        return Result.success(interventionService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!interventionService.removeById(id)) return Result.error("干预记录不存在");
        return Result.success("删除成功");
    }
}
