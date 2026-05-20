package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.ConsultationBooking;
import com.campus.mentalhealth.entity.ConsultationSchedule;
import com.campus.mentalhealth.service.ConsultationBookingService;
import com.campus.mentalhealth.service.ConsultationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class ConsultationBookingController {

    @Autowired
    private ConsultationBookingService consultationBookingService;
    @Autowired
    private ConsultationScheduleService consultationScheduleService;

    @GetMapping("/list")
    public Result<Page<ConsultationBooking>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(required = false) Long studentId,
                                                    @RequestParam(required = false) Long teacherId,
                                                    @RequestParam(required = false) Integer status) {
        // Limit pageSize to prevent abuse
        if (pageSize > 100) pageSize = 100;

        Page<ConsultationBooking> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ConsultationBooking> wrapper = new QueryWrapper<>();
        if (studentId != null) {
            wrapper.eq("student_id", studentId);
        }
        if (teacherId != null) {
            QueryWrapper<ConsultationSchedule> sw = new QueryWrapper<>();
            sw.select("schedule_id").eq("teacher_id", teacherId);
            List<ConsultationSchedule> schedules = consultationScheduleService.list(sw);
            if (schedules.isEmpty()) {
                return Result.success(new Page<>(pageNum, pageSize));
            }
            List<Long> scheduleIds = schedules.stream().map(ConsultationSchedule::getScheduleId).collect(java.util.stream.Collectors.toList());
            wrapper.in("schedule_id", scheduleIds);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(consultationBookingService.page(page, wrapper));
    }

    @PostMapping
    @Transactional
    public Result<?> book(@RequestBody ConsultationBooking booking) {
        if (booking.getScheduleId() == null) {
            return Result.error("排班ID不能为空");
        }
        if (booking.getStudentId() == null) {
            return Result.error("学生ID不能为空");
        }

        ConsultationSchedule schedule = consultationScheduleService.getById(booking.getScheduleId());
        if (schedule == null) {
            return Result.error("排班不存在");
        }
        int status = schedule.getStatus() != null ? schedule.getStatus() : 0;
        if (status != 1) {
            return Result.error("该时段不可预约");
        }
        int bookedCount = schedule.getBookedCount() != null ? schedule.getBookedCount() : 0;
        int maxCount = schedule.getMaxCount() != null ? schedule.getMaxCount() : 1;
        if (bookedCount >= maxCount) {
            return Result.error("该时段已满");
        }

        // Atomic update: increment booked_count only if there's still room
        UpdateWrapper<ConsultationSchedule> uw = new UpdateWrapper<>();
        uw.eq("schedule_id", booking.getScheduleId())
          .eq("status", 1)
          .lt("booked_count", schedule.getMaxCount())
          .setSql("booked_count = booked_count + 1");
        boolean updated = consultationScheduleService.update(uw);
        if (!updated) {
            return Result.error("预约失败，该时段可能已满");
        }

        booking.setStatus(0);
        booking.setTeacherId(schedule.getTeacherId());
        consultationBookingService.save(booking);

        return Result.success("预约成功，等待确认");
    }

    @PutMapping("/confirm")
    @Transactional
    public Result<?> confirm(@RequestBody ConsultationBooking booking) {
        if (booking.getBookingId() == null) {
            return Result.error("预约ID不能为空");
        }
        ConsultationBooking existing = consultationBookingService.getById(booking.getBookingId());
        if (existing == null) {
            return Result.error("预约记录不存在");
        }
        consultationBookingService.updateById(booking);
        return Result.success("操作成功");
    }

    @GetMapping("/{id}")
    public Result<ConsultationBooking> getById(@PathVariable Long id) {
        return Result.success(consultationBookingService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public Result<?> delete(@PathVariable Long id) {
        ConsultationBooking booking = consultationBookingService.getById(id);
        if (booking == null) {
            return Result.error("预约记录不存在");
        }
        if (booking.getScheduleId() != null) {
            UpdateWrapper<ConsultationSchedule> uw = new UpdateWrapper<>();
            uw.eq("schedule_id", booking.getScheduleId())
              .gt("booked_count", 0)
              .setSql("booked_count = booked_count - 1");
            consultationScheduleService.update(uw);
        }
        consultationBookingService.removeById(id);
        return Result.success("取消成功");
    }
}
