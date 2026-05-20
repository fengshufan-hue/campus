package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.ConsultationSchedule;
import com.campus.mentalhealth.entity.PsychologyTeacher;
import com.campus.mentalhealth.service.ConsultationScheduleService;
import com.campus.mentalhealth.service.PsychologyTeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class ConsultationScheduleController {

    private static final Logger log = LoggerFactory.getLogger(ConsultationScheduleController.class);

    @Autowired
    private ConsultationScheduleService consultationScheduleService;
    @Autowired
    private PsychologyTeacherService psychologyTeacherService;

    @GetMapping("/list")
    public Result<Page<ConsultationSchedule>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false) Long teacherId,
                                                     @RequestParam(required = false) String startDate,
                                                     @RequestParam(required = false) String endDate) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<ConsultationSchedule> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ConsultationSchedule> wrapper = new QueryWrapper<>();
        if (teacherId != null) {
            wrapper.eq("teacher_id", teacherId);
        }
        if (startDate != null && !startDate.isEmpty()) {
            try {
                wrapper.ge("schedule_date", LocalDate.parse(startDate));
            } catch (Exception e) {
                return Result.error("日期格式不正确，请使用yyyy-MM-dd格式");
            }
        }
        if (endDate != null && !endDate.isEmpty()) {
            try {
                wrapper.le("schedule_date", LocalDate.parse(endDate));
            } catch (Exception e) {
                return Result.error("日期格式不正确，请使用yyyy-MM-dd格式");
            }
        }
        wrapper.orderByAsc("schedule_date", "start_time");
        return Result.success(consultationScheduleService.page(page, wrapper));
    }

    @GetMapping("/open")
    public Result<?> getOpenSchedules() {
        QueryWrapper<ConsultationSchedule> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1).orderByAsc("schedule_date", "start_time");
        List<ConsultationSchedule> schedules = consultationScheduleService.list(wrapper);
        // Populate teacherName
        List<Long> teacherIds = schedules.stream().map(ConsultationSchedule::getTeacherId).distinct().collect(Collectors.toList());
        if (!teacherIds.isEmpty()) {
            QueryWrapper<PsychologyTeacher> tw = new QueryWrapper<>();
            tw.in("teacher_id", teacherIds);
            Map<Long, String> nameMap = psychologyTeacherService.list(tw).stream()
                    .collect(Collectors.toMap(
                            PsychologyTeacher::getTeacherId,
                            t -> t.getTeacherName() != null ? t.getTeacherName() : "心理教师",
                            (a, b) -> a));
            schedules.forEach(s -> s.setTeacherName(nameMap.getOrDefault(s.getTeacherId(), "心理教师")));
        }
        return Result.success(schedules);
    }

    @GetMapping("/{id}")
    public Result<ConsultationSchedule> getById(@PathVariable Long id) {
        return Result.success(consultationScheduleService.getById(id));
    }

    @PostMapping
    @Transactional
    public Result<?> save(@RequestBody ConsultationSchedule schedule) {
        if (schedule.getTeacherId() == null) return Result.error("教师ID不能为空");
        if (schedule.getScheduleDate() == null) return Result.error("日期不能为空");
        if (schedule.getStartTime() == null || schedule.getEndTime() == null) return Result.error("时间不能为空");
        if (schedule.getMaxCount() == null || schedule.getMaxCount() <= 0) return Result.error("人数上限必须大于0");
        if (schedule.getBookedCount() == null) schedule.setBookedCount(0);
        if (schedule.getStatus() == null) schedule.setStatus(1);
        consultationScheduleService.save(schedule);
        return Result.success("添加成功");
    }

    @PutMapping
    @Transactional
    public Result<?> update(@RequestBody ConsultationSchedule schedule) {
        if (schedule.getScheduleId() == null) return Result.error("时段ID不能为空");
        consultationScheduleService.updateById(schedule);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!consultationScheduleService.removeById(id)) return Result.error("时段不存在");
        return Result.success("删除成功");
    }
}
