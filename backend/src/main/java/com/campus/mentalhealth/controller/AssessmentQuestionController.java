package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.AssessmentQuestion;
import com.campus.mentalhealth.service.AssessmentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessment/question")
public class AssessmentQuestionController {

    @Autowired
    private AssessmentQuestionService assessmentQuestionService;

    @GetMapping("/list/{assessmentId}")
    public Result<List<AssessmentQuestion>> list(@PathVariable Long assessmentId) {
        QueryWrapper<AssessmentQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("assessment_id", assessmentId).orderByAsc("question_order");
        return Result.success(assessmentQuestionService.list(wrapper));
    }

    @PostMapping
    public Result<?> save(@RequestBody AssessmentQuestion question) {
        assessmentQuestionService.save(question);
        return Result.success("添加成功");
    }

    @PostMapping("/batch")
    public Result<?> saveBatch(@RequestBody List<AssessmentQuestion> questions) {
        assessmentQuestionService.saveBatch(questions);
        return Result.success("批量添加成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody AssessmentQuestion question) {
        assessmentQuestionService.updateById(question);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!assessmentQuestionService.removeById(id)) return Result.error("题目不存在");
        return Result.success("删除成功");
    }

    @DeleteMapping("/byAssessment/{assessmentId}")
    public Result<?> deleteByAssessment(@PathVariable Long assessmentId) {
        QueryWrapper<AssessmentQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("assessment_id", assessmentId);
        assessmentQuestionService.remove(wrapper);
        return Result.success("删除成功");
    }
}
