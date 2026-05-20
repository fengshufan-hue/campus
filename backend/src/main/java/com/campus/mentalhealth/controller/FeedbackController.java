package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Feedback;
import com.campus.mentalhealth.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/list")
    public Result<Page<Feedback>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) Long teacherId) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Feedback> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        if (teacherId != null) {
            wrapper.eq("teacher_id", teacherId);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(feedbackService.page(page, wrapper));
    }

    @GetMapping("/my")
    public Result<Page<Feedback>> getMyFeedback(@RequestParam(required = false) Long studentId,
                                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Feedback> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        if (studentId != null) {
            wrapper.eq("student_id", studentId);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(feedbackService.page(page, wrapper));
    }

    @PostMapping
    public Result<?> save(@RequestBody Feedback feedback) {
        feedbackService.save(feedback);
        return Result.success("评价成功");
    }

    @GetMapping("/{id}")
    public Result<Feedback> getById(@PathVariable Long id) {
        return Result.success(feedbackService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!feedbackService.removeById(id)) return Result.error("评价不存在");
        return Result.success("删除成功");
    }
}
