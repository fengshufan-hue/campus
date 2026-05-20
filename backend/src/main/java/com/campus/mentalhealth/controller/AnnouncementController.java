package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Announcement;
import com.campus.mentalhealth.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/list")
    public Result<Page<Announcement>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false) String keyword) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Announcement> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Announcement> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("publish_time");
        return Result.success(announcementService.page(page, wrapper));
    }

    @GetMapping("/published")
    public Result<Page<Announcement>> getPublished(@RequestParam(defaultValue = "1") Integer pageNum,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Announcement> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Announcement> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1).orderByDesc("publish_time");
        return Result.success(announcementService.page(page, wrapper));
    }

    @PostMapping
    public Result<?> save(@RequestBody Announcement announcement) {
        if (announcement.getPublishTime() == null) {
            announcement.setPublishTime(LocalDateTime.now());
        }
        announcementService.save(announcement);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody Announcement announcement) {
        announcementService.updateById(announcement);
        return Result.success("更新成功");
    }

    @PutMapping("/publish/{id}")
    @Transactional
    public Result<?> publish(@PathVariable Long id) {
        Announcement announcement = announcementService.getById(id);
        if (announcement == null) {
            return Result.error("公告不存在");
        }
        announcement.setStatus(1);
        announcement.setPublishTime(LocalDateTime.now());
        announcementService.updateById(announcement);
        return Result.success("发布成功");
    }

    @GetMapping("/{id}")
    public Result<Announcement> getById(@PathVariable Long id) {
        return Result.success(announcementService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!announcementService.removeById(id)) return Result.error("公告不存在");
        return Result.success("删除成功");
    }
}
