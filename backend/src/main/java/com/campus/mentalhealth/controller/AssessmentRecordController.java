package com.campus.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.mentalhealth.common.Result;
import com.campus.mentalhealth.entity.AssessmentQuestion;
import com.campus.mentalhealth.entity.AssessmentRecord;
import com.campus.mentalhealth.entity.Assessment;
import com.campus.mentalhealth.entity.Warning;
import com.campus.mentalhealth.service.AssessmentQuestionService;
import com.campus.mentalhealth.service.AssessmentRecordService;
import com.campus.mentalhealth.service.AssessmentService;
import com.campus.mentalhealth.service.StudentService;
import com.campus.mentalhealth.service.WarningService;
import com.campus.mentalhealth.vo.AssessmentRecordVO;
import com.campus.mentalhealth.vo.AssessmentSubmitDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/assessment/record")
public class AssessmentRecordController {

    private static final Logger log = LoggerFactory.getLogger(AssessmentRecordController.class);

    @Autowired
    private AssessmentRecordService assessmentRecordService;
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private WarningService warningService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private AssessmentQuestionService assessmentQuestionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/list")
    public Result<Page<AssessmentRecordVO>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(required = false) Long studentId,
                                                 @RequestParam(required = false) Long counselorId) {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Page<AssessmentRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AssessmentRecord> wrapper = new QueryWrapper<>();
        if (studentId != null) {
            wrapper.eq("student_id", studentId);
        }
        if (counselorId != null) {
            QueryWrapper<com.campus.mentalhealth.entity.Student> sw = new QueryWrapper<>();
            sw.eq("counselor_id", counselorId);
            List<com.campus.mentalhealth.entity.Student> students = studentService.list(sw);
            List<Long> studentIds = students.stream()
                    .map(com.campus.mentalhealth.entity.Student::getStudentId)
                    .collect(java.util.stream.Collectors.toList());
            if (studentIds.isEmpty()) {
                return Result.success(new Page<>(pageNum, pageSize));
            }
            wrapper.in("student_id", studentIds);
        }
        wrapper.orderByDesc("submit_time");
        Page<AssessmentRecord> recordPage = assessmentRecordService.page(page, wrapper);

        // 批量查询关联数据，避免N+1查询问题
        Set<Long> studentIds = recordPage.getRecords().stream()
                .map(AssessmentRecord::getStudentId)
                .collect(java.util.stream.Collectors.toSet());
        Set<Long> assessmentIds = recordPage.getRecords().stream()
                .map(AssessmentRecord::getAssessmentId)
                .collect(java.util.stream.Collectors.toSet());

        Map<Long, com.campus.mentalhealth.entity.Student> studentMap = studentIds.isEmpty() ?
                java.util.Collections.emptyMap() :
                studentService.listByIds(studentIds).stream()
                        .collect(java.util.stream.Collectors.toMap(
                                com.campus.mentalhealth.entity.Student::getStudentId,
                                java.util.function.Function.identity()));
        Map<Long, Assessment> assessmentMap = assessmentIds.isEmpty() ?
                java.util.Collections.emptyMap() :
                assessmentService.listByIds(assessmentIds).stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Assessment::getAssessmentId,
                                java.util.function.Function.identity()));

        List<AssessmentRecordVO> voList = recordPage.getRecords().stream().map(record -> {
            AssessmentRecordVO vo = new AssessmentRecordVO();
            com.campus.mentalhealth.entity.Student student = studentMap.get(record.getStudentId());
            vo.setStudentName(student != null ? student.getStudentName() : "");
            Assessment assessment = assessmentMap.get(record.getAssessmentId());
            vo.setAssessmentName(assessment != null ? assessment.getTitle() : "");
            vo.setTotalScore(record.getTotalScore());
            vo.setResultLevel(record.getResultLevel());
            vo.setSubmitTime(record.getSubmitTime());
            return vo;
        }).collect(java.util.stream.Collectors.toList());

        Page<AssessmentRecordVO> voPage = new Page<>(pageNum, pageSize, recordPage.getTotal());
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @PostMapping("/submit")
    @Transactional
    public Result<?> submit(@RequestBody AssessmentSubmitDTO dto) {
        if (dto.getAssessmentId() == null || dto.getStudentId() == null) {
            return Result.error("测评ID和学生ID不能为空");
        }

        AssessmentRecord record = new AssessmentRecord();
        record.setAssessmentId(dto.getAssessmentId());
        record.setStudentId(dto.getStudentId());
        record.setSubmitTime(LocalDateTime.now());

        try {
            int totalScore = 0;
            Map<String, JsonNode> answerNodeMap = objectMapper.convertValue(dto.getAnswers(),
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, JsonNode.class));

            QueryWrapper<AssessmentQuestion> qw = new QueryWrapper<>();
            qw.eq("assessment_id", dto.getAssessmentId());
            List<AssessmentQuestion> questions = assessmentQuestionService.list(qw);

            for (AssessmentQuestion question : questions) {
                JsonNode node = answerNodeMap.get(String.valueOf(question.getQuestionId()));
                if (node != null) {
                    String option;
                    if (node.isTextual()) {
                        option = node.asText();
                    } else if (node.has("answer")) {
                        option = node.get("answer").asText();
                    } else {
                        continue;
                    }
                    switch (option.toLowerCase()) {
                        case "a": totalScore += question.getScoreA() != null ? question.getScoreA() : 0; break;
                        case "b": totalScore += question.getScoreB() != null ? question.getScoreB() : 0; break;
                        case "c": totalScore += question.getScoreC() != null ? question.getScoreC() : 0; break;
                        case "d": totalScore += question.getScoreD() != null ? question.getScoreD() : 0; break;
                    }
                }
            }

            String resultLevel;
            if (totalScore >= 30) {
                resultLevel = "重度";
            } else if (totalScore >= 25) {
                resultLevel = "中度";
            } else if (totalScore >= 20) {
                resultLevel = "轻度";
            } else {
                resultLevel = "正常";
            }

            record.setTotalScore(totalScore);
            record.setResultLevel(resultLevel);
            record.setAnswers(objectMapper.writeValueAsString(dto.getAnswers()));
        } catch (Exception e) {
            log.error("答案解析失败", e);
            return Result.error("答案解析失败");
        }

        assessmentRecordService.save(record);

        if (record.getTotalScore() != null && record.getTotalScore() >= 30) {
            Warning warning = new Warning();
            warning.setStudentId(record.getStudentId());
            warning.setWarningLevel("高");
            warning.setWarningReason("心理测评得分过高：" + record.getTotalScore() + "分");
            warning.setSourceType("assessment");
            warning.setSourceId(record.getRecordId());
            warning.setStatus(0);
            warning.setCounselorId(null);
            warningService.save(warning);
        } else if (record.getTotalScore() != null && record.getTotalScore() >= 20) {
            Warning warning = new Warning();
            warning.setStudentId(record.getStudentId());
            warning.setWarningLevel("中");
            warning.setWarningReason("心理测评得分偏高：" + record.getTotalScore() + "分");
            warning.setSourceType("assessment");
            warning.setSourceId(record.getRecordId());
            warning.setStatus(0);
            warning.setCounselorId(null);
            warningService.save(warning);
        }

        return Result.success("提交成功");
    }

    @GetMapping("/{id}")
    public Result<AssessmentRecord> getById(@PathVariable Long id) {
        return Result.success(assessmentRecordService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!assessmentRecordService.removeById(id)) return Result.error("记录不存在");
        return Result.success("删除成功");
    }
}
