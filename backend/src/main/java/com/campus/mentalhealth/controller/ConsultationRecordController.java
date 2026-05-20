package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.ConsultationRecordEntity;
import com.campus.mentalhealth.service.ConsultationRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultation/record")
public class ConsultationRecordController {

    private static final Logger log = LoggerFactory.getLogger(ConsultationRecordController.class);

    @Autowired
    private ConsultationRecordService consultationRecordService;

    @GetMapping("/list")
    public Result<Page<ConsultationRecordEntity>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                                         @RequestParam(required = false) Long studentId,
                                                         @RequestParam(required = false) Long teacherId) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<ConsultationRecordEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ConsultationRecordEntity> wrapper = new QueryWrapper<>();
        if (studentId != null) {
            wrapper.eq("student_id", studentId);
        }
        if (teacherId != null) {
            wrapper.eq("teacher_id", teacherId);
        }
        wrapper.orderByDesc("consult_date");
        return Result.success(consultationRecordService.page(page, wrapper));
    }

    @PostMapping
    @Transactional
    public Result<?> save(@RequestBody ConsultationRecordEntity record) {
        if (record.getStudentId() == null) {
            return Result.error("请选择学生");
        }
        if (record.getTeacherId() == null) {
            return Result.error("教师ID不能为空");
        }
        if (record.getBookingId() == null) {
            return Result.error("请选择预约");
        }
        try {
            record.setCreateTime(java.time.LocalDateTime.now());
            record.setUpdateTime(java.time.LocalDateTime.now());
            consultationRecordService.save(record);
            return Result.success("保存成功");
        } catch (Exception e) {
            log.error("保存咨询记录失败", e);
            return Result.error("保存失败，请稍后重试");
        }
    }

    @PutMapping
    @Transactional
    public Result<?> update(@RequestBody ConsultationRecordEntity record) {
        if (record.getRecordId() == null) {
            return Result.error("记录ID不能为空");
        }
        try {
            record.setUpdateTime(java.time.LocalDateTime.now());
            consultationRecordService.updateById(record);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新咨询记录失败", e);
            return Result.error("更新失败，请稍后重试");
        }
    }

    @GetMapping("/{id}")
    public Result<ConsultationRecordEntity> getById(@PathVariable Long id) {
        return Result.success(consultationRecordService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!consultationRecordService.removeById(id)) return Result.error("记录不存在");
        return Result.success("删除成功");
    }
}
