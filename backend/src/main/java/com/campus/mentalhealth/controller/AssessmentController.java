package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Assessment;
import com.campus.mentalhealth.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assessment")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/list")
    public Result<Page<Assessment>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Integer status) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Assessment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Assessment> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(assessmentService.page(page, wrapper));
    }

    @GetMapping("/enabled")
    public Result<?> getEnabled() {
        QueryWrapper<Assessment> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        return Result.success(assessmentService.list(wrapper));
    }

    @GetMapping("/{id}")
    public Result<Assessment> getById(@PathVariable Long id) {
        return Result.success(assessmentService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Assessment assessment) {
        assessmentService.save(assessment);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody Assessment assessment) {
        assessmentService.updateById(assessment);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!assessmentService.removeById(id)) return Result.error("测评不存在");
        return Result.success("删除成功");
    }
}
