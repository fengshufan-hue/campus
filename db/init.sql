-- ============================================================
-- 校园心理健康服务平台 - 数据库初始化脚本
-- Database: campus_mental_health
-- ============================================================

DROP DATABASE IF EXISTS `campus_mental_health`;
CREATE DATABASE `campus_mental_health` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `campus_mental_health`;

-- -----------------------------------------------------------
-- 1. 系统管理员表 (admin)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
    `admin_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `password` VARCHAR(100) NOT NULL DEFAULT '$2a$10$4cXgTAXCibuxtcjjKvUgDOihALDF7v54h46dYGkeNtszwtSp37DGq' COMMENT '密码',
    `admin_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `phone_num` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员表';

-- -----------------------------------------------------------
-- 2. 辅导员信息表 (counselor)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `counselor`;
CREATE TABLE `counselor` (
    `counselor_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '辅导员ID',
    `password` VARCHAR(100) NOT NULL DEFAULT '$2a$10$4cXgTAXCibuxtcjjKvUgDOihALDF7v54h46dYGkeNtszwtSp37DGq' COMMENT '密码',
    `counselor_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `phone_num` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `department` VARCHAR(100) DEFAULT NULL COMMENT '所属院系',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`counselor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='辅导员信息表';

-- -----------------------------------------------------------
-- 3. 心理教师信息表 (psychology_teacher)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `psychology_teacher`;
CREATE TABLE `psychology_teacher` (
    `teacher_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '心理教师ID',
    `password` VARCHAR(100) NOT NULL DEFAULT '$2a$10$4cXgTAXCibuxtcjjKvUgDOihALDF7v54h46dYGkeNtszwtSp37DGq' COMMENT '密码',
    `teacher_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `phone_num` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `specialty` VARCHAR(200) DEFAULT NULL COMMENT '专业方向',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理教师信息表';

-- -----------------------------------------------------------
-- 4. 学生信息表 (student)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
    `student_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '学号',
    `password` VARCHAR(100) NOT NULL DEFAULT '$2a$10$4cXgTAXCibuxtcjjKvUgDOihALDF7v54h46dYGkeNtszwtSp37DGq' COMMENT '密码',
    `student_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `phone_num` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `college` VARCHAR(100) DEFAULT NULL COMMENT '学院',
    `major` VARCHAR(100) DEFAULT NULL COMMENT '专业',
    `grade` VARCHAR(20) DEFAULT NULL COMMENT '年级',
    `class_name` VARCHAR(50) DEFAULT NULL COMMENT '班级',
    `counselor_id` BIGINT DEFAULT NULL COMMENT '辅导员ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`student_id`),
    KEY `fk_student_counselor` (`counselor_id`),
    CONSTRAINT `fk_student_counselor` FOREIGN KEY (`counselor_id`) REFERENCES `counselor` (`counselor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

-- -----------------------------------------------------------
-- 5. 心理测评问卷表 (assessment)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `assessment`;
CREATE TABLE `assessment` (
    `assessment_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '测评ID',
    `title` VARCHAR(200) NOT NULL COMMENT '测评名称',
    `description` TEXT COMMENT '测评说明',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    `pass_score` INT DEFAULT 60 COMMENT '及格分数线',
    `teacher_id` BIGINT DEFAULT NULL COMMENT '创建教师ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`assessment_id`),
    KEY `fk_assessment_teacher` (`teacher_id`),
    CONSTRAINT `fk_assessment_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `psychology_teacher` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理测评问卷表';

-- -----------------------------------------------------------
-- 6. 测评题目表 (assessment_question)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `assessment_question`;
CREATE TABLE `assessment_question` (
    `question_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    `assessment_id` BIGINT NOT NULL COMMENT '所属测评ID',
    `question_content` VARCHAR(500) NOT NULL COMMENT '题目内容',
    `option_a` VARCHAR(200) NOT NULL COMMENT '选项A',
    `option_b` VARCHAR(200) NOT NULL COMMENT '选项B',
    `option_c` VARCHAR(200) NOT NULL COMMENT '选项C',
    `option_d` VARCHAR(200) NOT NULL COMMENT '选项D',
    `score_a` INT DEFAULT 1 COMMENT '选项A分值',
    `score_b` INT DEFAULT 2 COMMENT '选项B分值',
    `score_c` INT DEFAULT 3 COMMENT '选项C分值',
    `score_d` INT DEFAULT 4 COMMENT '选项D分值',
    `question_order` INT NOT NULL COMMENT '题目序号',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`question_id`),
    KEY `fk_question_assessment` (`assessment_id`),
    CONSTRAINT `fk_question_assessment` FOREIGN KEY (`assessment_id`) REFERENCES `assessment` (`assessment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测评题目表';

-- -----------------------------------------------------------
-- 7. 测评结果记录表 (assessment_record)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `assessment_record`;
CREATE TABLE `assessment_record` (
    `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `assessment_id` BIGINT NOT NULL COMMENT '测评ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `total_score` INT NOT NULL COMMENT '总分',
    `result_level` VARCHAR(20) DEFAULT NULL COMMENT '结果等级：正常/轻度/中度/重度',
    `answers` TEXT COMMENT '答题详情(JSON)',
    `submit_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`record_id`),
    KEY `fk_record_assessment` (`assessment_id`),
    KEY `fk_record_student` (`student_id`),
    CONSTRAINT `fk_record_assessment` FOREIGN KEY (`assessment_id`) REFERENCES `assessment` (`assessment_id`),
    CONSTRAINT `fk_record_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测评结果记录表';

-- -----------------------------------------------------------
-- 8. 咨询排班表 (consultation_schedule)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `consultation_schedule`;
CREATE TABLE `consultation_schedule` (
    `schedule_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '排班ID',
    `teacher_id` BIGINT NOT NULL COMMENT '教师ID',
    `schedule_date` DATE NOT NULL COMMENT '排班日期',
    `start_time` TIME NOT NULL COMMENT '开始时间',
    `end_time` TIME NOT NULL COMMENT '结束时间',
    `max_count` INT NOT NULL DEFAULT 1 COMMENT '最大预约人数',
    `booked_count` INT NOT NULL DEFAULT 0 COMMENT '已预约人数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用 1-开放 2-已满',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '咨询地点',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`schedule_id`),
    KEY `fk_schedule_teacher` (`teacher_id`),
    CONSTRAINT `fk_schedule_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `psychology_teacher` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询排班表';

-- -----------------------------------------------------------
-- 9. 咨询预约表 (consultation_booking)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `consultation_booking`;
CREATE TABLE `consultation_booking` (
    `booking_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    `schedule_id` BIGINT NOT NULL COMMENT '排班ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `teacher_id` BIGINT DEFAULT NULL COMMENT '教师ID',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '预约原因',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待确认 1-已确认 2-已取消 3-已完成',
    `teacher_reply` VARCHAR(500) DEFAULT NULL COMMENT '教师回复',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`booking_id`),
    KEY `fk_booking_schedule` (`schedule_id`),
    KEY `fk_booking_student` (`student_id`),
    CONSTRAINT `fk_booking_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `consultation_schedule` (`schedule_id`),
    CONSTRAINT `fk_booking_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询预约表';

-- -----------------------------------------------------------
-- 10. 咨询记录表 (consultation_record)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `consultation_record`;
CREATE TABLE `consultation_record` (
    `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `booking_id` BIGINT NOT NULL COMMENT '预约ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `teacher_id` BIGINT NOT NULL COMMENT '教师ID',
    `consult_date` DATETIME DEFAULT NULL COMMENT '咨询时间',
    `consult_topic` VARCHAR(500) DEFAULT NULL COMMENT '咨询主题',
    `consult_content` TEXT COMMENT '咨询内容',
    `consult_summary` TEXT COMMENT '咨询总结',
    `follow_up` VARCHAR(500) DEFAULT NULL COMMENT '后续建议',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`record_id`),
    KEY `fk_crecord_booking` (`booking_id`),
    KEY `fk_crecord_student` (`student_id`),
    KEY `fk_crecord_teacher` (`teacher_id`),
    CONSTRAINT `fk_crecord_booking` FOREIGN KEY (`booking_id`) REFERENCES `consultation_booking` (`booking_id`),
    CONSTRAINT `fk_crecord_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
    CONSTRAINT `fk_crecord_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `psychology_teacher` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询记录表';

-- -----------------------------------------------------------
-- 11. 预警记录表 (warning)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `warning`;
CREATE TABLE `warning` (
    `warning_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预警ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `warning_level` VARCHAR(20) NOT NULL COMMENT '预警等级：低/中/高',
    `warning_reason` VARCHAR(500) NOT NULL COMMENT '预警原因',
    `source_type` VARCHAR(20) DEFAULT 'assessment' COMMENT '来源类型：assessment-测评, manual-手动',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源记录ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待处理 1-处理中 2-已处理',
    `counselor_id` BIGINT DEFAULT NULL COMMENT '负责辅导员ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`warning_id`),
    KEY `fk_warning_student` (`student_id`),
    KEY `fk_warning_counselor` (`counselor_id`),
    CONSTRAINT `fk_warning_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
    CONSTRAINT `fk_warning_counselor` FOREIGN KEY (`counselor_id`) REFERENCES `counselor` (`counselor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表';

-- -----------------------------------------------------------
-- 12. 干预记录表 (intervention)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `intervention`;
CREATE TABLE `intervention` (
    `intervention_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '干预ID',
    `warning_id` BIGINT NOT NULL COMMENT '预警ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `counselor_id` BIGINT NOT NULL COMMENT '辅导员ID',
    `intervention_type` VARCHAR(50) NOT NULL COMMENT '干预类型：谈话/家访/转介/其他',
    `intervention_content` TEXT COMMENT '干预内容',
    `intervention_result` VARCHAR(500) DEFAULT NULL COMMENT '干预结果',
    `intervention_date` DATETIME DEFAULT NULL COMMENT '干预时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`intervention_id`),
    KEY `fk_intervention_warning` (`warning_id`),
    KEY `fk_intervention_student` (`student_id`),
    KEY `fk_intervention_counselor` (`counselor_id`),
    CONSTRAINT `fk_intervention_warning` FOREIGN KEY (`warning_id`) REFERENCES `warning` (`warning_id`),
    CONSTRAINT `fk_intervention_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
    CONSTRAINT `fk_intervention_counselor` FOREIGN KEY (`counselor_id`) REFERENCES `counselor` (`counselor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='干预记录表';

-- -----------------------------------------------------------
-- 13. 知识科普文章表 (article)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
    `article_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类：压力管理/情绪调节/人际关系/自我认知/其他',
    `author_id` BIGINT DEFAULT NULL COMMENT '作者ID(心理教师)',
    `author_name` VARCHAR(50) DEFAULT NULL COMMENT '作者姓名',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-草稿 1-已发布',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识科普文章表';

-- -----------------------------------------------------------
-- 14. 公告信息表 (announcement)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
    `announcement_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `author` VARCHAR(50) DEFAULT NULL COMMENT '发布者',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '类型：通知/活动/其他',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-草稿 1-已发布',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`announcement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告信息表';

-- -----------------------------------------------------------
-- 15. 反馈评价表 (feedback)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
    `feedback_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    `booking_id` BIGINT NOT NULL COMMENT '预约ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `teacher_id` BIGINT NOT NULL COMMENT '教师ID',
    `rating` INT NOT NULL COMMENT '评分(1-5)',
    `content` VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
    `is_anonymous` TINYINT NOT NULL DEFAULT 0 COMMENT '是否匿名：0-否 1-是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`feedback_id`),
    KEY `fk_feedback_booking` (`booking_id`),
    KEY `fk_feedback_student` (`student_id`),
    KEY `fk_feedback_teacher` (`teacher_id`),
    CONSTRAINT `fk_feedback_booking` FOREIGN KEY (`booking_id`) REFERENCES `consultation_booking` (`booking_id`),
    CONSTRAINT `fk_feedback_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
    CONSTRAINT `fk_feedback_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `psychology_teacher` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈评价表';

-- -----------------------------------------------------------
-- 16. 学生心理档案表 (student_archive)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `student_archive`;
CREATE TABLE `student_archive` (
    `archive_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '档案ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `mental_status` VARCHAR(20) DEFAULT '正常' COMMENT '心理状态：正常/关注/预警',
    `last_assessment_score` INT DEFAULT NULL COMMENT '最近测评得分',
    `last_assessment_date` DATETIME DEFAULT NULL COMMENT '最近测评日期',
    `consultation_count` INT NOT NULL DEFAULT 0 COMMENT '咨询次数',
    `warning_count` INT NOT NULL DEFAULT 0 COMMENT '预警次数',
    `remarks` TEXT COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`archive_id`),
    KEY `fk_archive_student` (`student_id`),
    CONSTRAINT `fk_archive_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生心理档案表';


-- ============================================================
-- 初始化数据
-- ============================================================

-- 系统管理员
INSERT INTO `admin` (`admin_name`, `gender`, `age`, `phone_num`, `email`) VALUES
('系统管理员', '男', 35, '13800000001', 'admin@campus.edu.cn');

-- 辅导员
INSERT INTO `counselor` (`counselor_name`, `gender`, `age`, `phone_num`, `email`, `department`) VALUES
('张老师', '女', 32, '13800000002', 'zhang@campus.edu.cn', '计算机学院'),
('李老师', '男', 28, '13800000003', 'li@campus.edu.cn', '信息工程学院');

-- 心理教师
INSERT INTO `psychology_teacher` (`teacher_name`, `gender`, `age`, `phone_num`, `email`, `title`, `specialty`) VALUES
('王教授', '女', 40, '13800000004', 'wang@campus.edu.cn', '副教授', '青少年心理健康'),
('赵老师', '男', 35, '13800000005', 'zhao@campus.edu.cn', '讲师', '心理咨询与治疗');

-- 学生
INSERT INTO `student` (`student_name`, `gender`, `age`, `phone_num`, `email`, `college`, `major`, `grade`, `class_name`, `counselor_id`) VALUES
('小明', '男', 20, '13800000006', 'xiaoming@campus.edu.cn', '计算机学院', '软件工程', '2023', '软工1班', 1),
('小红', '女', 19, '13800000007', 'xiaohong@campus.edu.cn', '计算机学院', '计算机科学与技术', '2023', '计科1班', 1),
('小刚', '男', 21, '13800000008', 'xiaogang@campus.edu.cn', '信息工程学院', '网络工程', '2023', '网工1班', 2);

-- 心理测评问卷
INSERT INTO `assessment` (`title`, `description`, `status`, `pass_score`, `teacher_id`) VALUES
('大学生心理健康自评量表(SCL-90简版)', '本量表用于评估大学生近一周的心理健康状况，请根据实际情况如实作答。', 1, 60, 1),
('大学生压力感知量表', '本量表用于评估大学生在学习、生活、人际等方面的压力水平。', 1, 50, 1),
('焦虑自评量表(SAS)', '本量表用于评估个体的焦虑水平，请根据最近一周的感受作答。', 1, 55, 2);

-- 测评题目(以SCL-90简版为例，10题)
INSERT INTO `assessment_question` (`assessment_id`, `question_content`, `option_a`, `option_b`, `option_c`, `option_d`, `score_a`, `score_b`, `score_c`, `score_d`, `question_order`) VALUES
(1, '你是否感到情绪低落或沮丧？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 1),
(1, '你是否感到紧张或不安？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 2),
(1, '你是否感到孤独？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 3),
(1, '你的睡眠质量如何？', '很好', '一般', '较差', '很差', 1, 2, 3, 4, 4),
(1, '你是否感到食欲减退？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 5),
(1, '你是否对日常活动失去兴趣？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 6),
(1, '你是否感到自卑或缺乏自信？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 7),
(1, '你是否感到精力不足或疲乏？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 8),
(1, '你是否难以集中注意力？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 9),
(1, '你是否感到人际交往困难？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 10);

-- 压力感知量表题目(5题)
INSERT INTO `assessment_question` (`assessment_id`, `question_content`, `option_a`, `option_b`, `option_c`, `option_d`, `score_a`, `score_b`, `score_c`, `score_d`, `question_order`) VALUES
(2, '你是否感到学业压力过大？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 1),
(2, '你是否感到经济上有压力？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 2),
(2, '你是否感到人际关系压力大？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 3),
(2, '你是否感到未来前景不明朗？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 4),
(2, '你是否感到难以平衡学习与生活？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 5);

-- 焦虑自评量表题目(5题)
INSERT INTO `assessment_question` (`assessment_id`, `question_content`, `option_a`, `option_b`, `option_c`, `option_d`, `score_a`, `score_b`, `score_c`, `score_d`, `question_order`) VALUES
(3, '你是否感到无缘无故地紧张？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 1),
(3, '你是否感到害怕或恐慌？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 2),
(3, '你是否感到心跳加速或胸闷？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 3),
(3, '你是否感到手脚发抖或出汗？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 4),
(3, '你是否感到难以放松？', '从不', '偶尔', '经常', '总是', 1, 2, 3, 4, 5);

-- 咨询排班（未来30天每天多个时段）
INSERT INTO `consultation_schedule` (`teacher_id`, `schedule_date`, `start_time`, `end_time`, `location`, `max_count`, `booked_count`, `status`)
SELECT teacher_id, DATE_ADD(CURDATE(), INTERVAL n DAY) AS schedule_date, start_time, end_time, location, 3, 0, 1
FROM (
  SELECT 1 AS teacher_id, '09:00:00' AS start_time, '10:00:00' AS end_time, '心理中心101室' AS location
  UNION ALL SELECT 1, '10:00:00', '11:00:00', '心理中心101室'
  UNION ALL SELECT 1, '14:00:00', '15:00:00', '心理中心101室'
  UNION ALL SELECT 1, '15:00:00', '16:00:00', '心理中心101室'
  UNION ALL SELECT 2, '09:00:00', '10:00:00', '心理中心102室'
  UNION ALL SELECT 2, '10:00:00', '11:00:00', '心理中心102室'
  UNION ALL SELECT 2, '14:00:00', '15:00:00', '心理中心102室'
  UNION ALL SELECT 2, '15:00:00', '16:00:00', '心理中心102室'
) AS slots
CROSS JOIN (
  SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
  UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14
  UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19
  UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24
  UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28 UNION ALL SELECT 29
) AS days;

-- 公告
INSERT INTO `announcement` (`title`, `content`, `author`, `type`, `status`, `publish_time`) VALUES
('关于开展心理健康宣传月活动的通知', '为进一步提高学生心理健康意识，学校将于本月开展心理健康宣传月活动，包括心理讲座、团体辅导、心理测评等。', '系统管理员', '通知', 1, NOW()),
('心理中心咨询时间调整通知', '因教师培训安排，心理中心咨询时间临时调整为每周二至周五下午14:00-17:00，请同学们合理安排预约时间。', '系统管理员', '通知', 1, NOW());

-- 知识科普文章
INSERT INTO `article` (`title`, `content`, `category`, `author_id`, `author_name`, `view_count`, `status`) VALUES
('如何应对考试焦虑', '<h3>一、考试焦虑的表现</h3><p>考试焦虑是大学生常见的心理问题，主要表现为：</p><ul><li>考前紧张、失眠、食欲减退</li><li>考中注意力不集中、大脑空白</li><li>考后过度担忧成绩、自我否定</li></ul><h3>二、产生原因</h3><p>考试焦虑的产生通常与以下因素有关：</p><ol><li><strong>完美主义倾向</strong>：对自己要求过高，害怕失败</li><li><strong>准备不足</strong>：复习不充分导致信心不足</li><li><strong>过往负面经历</strong>：曾经的考试失利造成心理阴影</li><li><strong>外部压力</strong>：家长期望、同伴竞争等</li></ol><h3>三、应对策略</h3><p>1. <strong>认知调整</strong>：认识到适度焦虑有助于发挥，不要过度担忧</p><p>2. <strong>充分准备</strong>：制定合理的复习计划，循序渐进</p><p>3. <strong>放松训练</strong>：深呼吸、冥想、渐进性肌肉放松</p><p>4. <strong>积极暗示</strong>：用"我已经准备好了"替代"我肯定考不好"</p><p>5. <strong>寻求帮助</strong>：如果焦虑严重影响生活，请及时联系心理中心</p>', '压力管理', 1, '王教授', 128, 1),
('大学生人际交往技巧', '<h3>一、人际交往的重要性</h3><p>良好的人际关系对心理健康至关重要。大学是我们建立社会支持网络的重要阶段，和谐的人际关系可以帮助我们：</p><ul><li>缓解孤独感和思乡情绪</li><li>获得情感支持和实际帮助</li><li>提升自我认同和幸福感</li><li>培养沟通能力和团队合作精神</li></ul><h3>二、实用交往技巧</h3><p><strong>1. 学会倾听</strong></p><p>倾听是沟通的基础。与人交谈时，保持眼神交流，不要急于打断对方，用点头或简短回应表示你在认真听。</p><p><strong>2. 表达感受</strong></p><p>用"我"句式表达感受，如"当你...时，我感到..."，避免指责对方。</p><p><strong>3. 尊重差异</strong></p><p>每个人成长环境不同，价值观和习惯也会有差异。学会包容和理解，不要强求一致。</p><p><strong>4. 建立边界</strong></p><p>懂得说"不"，保护好自己的时间和精力，同时也要尊重他人的边界。</p><h3>三、常见人际问题</h3><p>宿舍矛盾、恋爱困惑、师生沟通等问题在大学中很常见。遇到困难时，不要独自承受，可以向辅导员、心理老师或信任的朋友寻求支持。</p>', '人际关系', 1, '王教授', 95, 1),
('情绪管理入门指南', '<h3>一、认识情绪</h3><p>情绪是我们对外界刺激的自然反应，没有"好"情绪和"坏"情绪之分。所有情绪都有其存在的意义：</p><ul><li><strong>愤怒</strong>：提示我们的边界被侵犯</li><li><strong>悲伤</strong>：帮助我们处理失去</li><li><strong>焦虑</strong>：提醒我们关注潜在威胁</li><li><strong>快乐</strong>：强化积极行为</li></ul><h3>二、情绪管理的方法</h3><p><strong>1. 情绪觉察</strong></p><p>当情绪出现时，先停下来，问自己："我现在感受到什么？"给情绪命名是管理情绪的第一步。</p><p><strong>2. 接纳情绪</strong></p><p>不要压抑或否认情绪，允许自己感受它。告诉自己："我现在感到焦虑，这是正常的。"</p><p><strong>3. 情绪调节</strong></p><ul><li>深呼吸：缓慢地吸气4秒，屏气4秒，呼气6秒</li><li>运动：跑步、瑜伽、打球等有氧运动能有效改善情绪</li><li>写日记：把感受写下来，有助于理清思路</li><li>正念冥想：专注于当下，减少对过去的懊悔和对未来的担忧</li></ul><p><strong>4. 寻求支持</strong></p><p>当情绪难以自我调节时，及时寻求专业帮助是明智的选择。心理中心的老师可以为你提供专业的指导和支持。</p><h3>三、情绪日记模板</h3><p>建议每天花5分钟记录：</p><p>- 今天的主要情绪是什么？</p><p>- 什么事件引发了这种情绪？</p><p>- 我是如何应对的？</p><p>- 下次可以怎么做更好？</p>', '情绪调节', 2, '赵老师', 76, 1);

-- ============================================================
-- 测试数据：测评记录 + 预警记录
-- ============================================================

-- 小明(学生ID=1, 辅导员ID=1): 高分提交 -> 触发高等级预警 (score=40, 全D)
INSERT INTO `assessment_record` (`assessment_id`, `student_id`, `total_score`, `result_level`, `answers`, `submit_time`) VALUES
(1, 1, 40, '重度', '{"1":"D","2":"D","3":"D","4":"D","5":"D","6":"D","7":"D","8":"D","9":"D","10":"D"}', NOW());

-- 小红(学生ID=2, 辅导员ID=1): 中分提交 -> 触发中等级预警 (score=22)
INSERT INTO `assessment_record` (`assessment_id`, `student_id`, `total_score`, `result_level`, `answers`, `submit_time`) VALUES
(1, 2, 22, '轻度', '{"1":"B","2":"B","3":"B","4":"C","5":"B","6":"B","7":"C","8":"B","9":"B","10":"B"}', NOW());

-- 小刚(学生ID=3, 辅导员ID=2): 低分提交 -> 不触发预警 (score=11, 正常)
INSERT INTO `assessment_record` (`assessment_id`, `student_id`, `total_score`, `result_level`, `answers`, `submit_time`) VALUES
(1, 3, 11, '正常', '{"1":"A","2":"A","3":"A","4":"B","5":"A","6":"A","7":"A","8":"A","9":"A","10":"A"}', NOW());

-- 预警记录（测评自动生成的预警）
-- 小明的预警：高等级，未分配
INSERT INTO `warning` (`student_id`, `warning_level`, `warning_reason`, `source_type`, `source_id`, `status`, `counselor_id`) VALUES
(1, '高', '心理测评得分过高：40分', 'assessment', 1, 0, NULL);

-- 小红的预警：中等级，未分配
INSERT INTO `warning` (`student_id`, `warning_level`, `warning_reason`, `source_type`, `source_id`, `status`, `counselor_id`) VALUES
(2, '中', '心理测评得分偏高：22分', 'assessment', 2, 0, NULL);
