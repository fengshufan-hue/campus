package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.Article;
import com.campus.mentalhealth.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public Result<Page<Article>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) String category) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Article> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(articleService.page(page, wrapper));
    }

    @GetMapping("/published")
    public Result<Page<Article>> getPublished(@RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(required = false) String category) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<Article> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(articleService.page(page, wrapper));
    }

    @PostMapping
    public Result<?> save(@RequestBody Article article) {
        if (article.getViewCount() == null) article.setViewCount(0);
        articleService.save(article);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody Article article) {
        articleService.updateById(article);
        return Result.success("更新成功");
    }

    @PutMapping("/publish/{id}")
    @Transactional
    public Result<?> publish(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) return Result.error("文章不存在");
        article.setStatus(1);
        articleService.updateById(article);
        return Result.success("发布成功");
    }

    @PutMapping("/view/{id}")
    @Transactional
    public Result<?> incrementView(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) return Result.error("文章不存在");
        article.setViewCount(article.getViewCount() == null ? 1 : article.getViewCount() + 1);
        articleService.updateById(article);
        return Result.success(article);
    }

    @GetMapping("/{id}")
    public Result<Article> getById(@PathVariable Long id) {
        return Result.success(articleService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!articleService.removeById(id)) return Result.error("文章不存在");
        return Result.success("删除成功");
    }
}
