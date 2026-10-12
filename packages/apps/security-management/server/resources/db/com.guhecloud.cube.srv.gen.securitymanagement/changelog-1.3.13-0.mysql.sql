-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `processing_records` (
                                      `id` bigint(20) NOT NULL,
                                      `processed_by` varchar(100) DEFAULT NULL COMMENT '处理人',
                                      `processed_time` datetime DEFAULT NULL COMMENT '处理时间',
                                      `processed_result` varchar(50) DEFAULT NULL COMMENT '处理结果',
                                      `deleted` bigint(20) DEFAULT NULL,
                                      `tenant` int(11) DEFAULT NULL,
                                      `create_by` varchar(255) DEFAULT NULL,
                                      `create_time` datetime DEFAULT NULL,
                                      `update_by` varchar(255) DEFAULT NULL,
                                      `update_time` datetime DEFAULT NULL,
                                      `call_id` varchar(50) DEFAULT NULL COMMENT '关联键',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `bed_file` (
                            `id` bigint(20) NOT NULL,
                            `bed` varchar(50) DEFAULT NULL COMMENT '床号',
                            `current_visit_id` varchar(50) DEFAULT NULL COMMENT '当前就诊ID',
                            `date_hospitalization` datetime DEFAULT NULL COMMENT '住院日期',
                            `discharge_date` datetime DEFAULT NULL COMMENT '出院日期',
                            `current_patient` varchar(50) DEFAULT NULL COMMENT '当前病人',
                            `bed_management_doctor` varchar(50) DEFAULT NULL COMMENT '管床医生',
                            `bed_nurse` varchar(100) DEFAULT NULL COMMENT '管床护士',
                            `patient_gender` int(2) DEFAULT NULL COMMENT '0女1男',
                            `attending_physician` varchar(50) DEFAULT NULL COMMENT '主治医生',
                            `pathological_information` text COMMENT '病理信息',
                            `medication_information` text COMMENT '用药信息',
                            `age` int(2) DEFAULT NULL COMMENT '年龄',
                            `deleted` bigint(20) DEFAULT NULL,
                            `tenant` int(11) DEFAULT NULL,
                            `create_by` varchar(255) DEFAULT NULL,
                            `create_time` datetime DEFAULT NULL,
                            `update_by` varchar(255) DEFAULT NULL,
                            `update_time` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `call_record` (
                               `id` bigint(20) NOT NULL,
                               `call_time` datetime DEFAULT NULL COMMENT '呼叫时间',
                               `device_id` varchar(100) DEFAULT NULL COMMENT '设备id',
                               `device_name` varchar(50) DEFAULT NULL COMMENT '呼叫设备',
                               `area_name` varchar(255) DEFAULT NULL COMMENT '区域',
                               `call_type` varchar(50) DEFAULT NULL COMMENT '呼叫类型',
                               `call_duration` int(255) DEFAULT NULL COMMENT '通话时长',
                               `record_id` varchar(50) DEFAULT NULL COMMENT '记录id',
                               `bed` varchar(50) DEFAULT NULL COMMENT '床位',
                               `deleted` bigint(20) DEFAULT NULL,
                               `tenant` int(11) DEFAULT NULL,
                               `create_by` varchar(255) DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               `update_by` varchar(255) DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `surgical_demonstration` (
                                          `id` bigint(20) NOT NULL,
                                          `name` varchar(100) DEFAULT NULL COMMENT '名称',
                                          `operating_room` varchar(100) DEFAULT NULL COMMENT '手术室',
                                          `chief_surgeon` varchar(100) DEFAULT NULL COMMENT '主刀医生',
                                          `date_operation` datetime DEFAULT NULL COMMENT '手术日期',
                                          `start_time` datetime DEFAULT NULL COMMENT '开始日期',
                                          `end_time` datetime DEFAULT NULL COMMENT '结束日期',
                                          `video_duration` int(2) DEFAULT NULL COMMENT '时长',
                                          `course_classification` varchar(50) DEFAULT NULL COMMENT '课程分类',
                                          `subject` varchar(100) DEFAULT NULL COMMENT '科目',
                                          `modality` varchar(50) DEFAULT NULL COMMENT '形式',
                                          `playback_count` int(255) DEFAULT NULL COMMENT '播放次数',
                                          `deleted` bigint(20) DEFAULT NULL,
                                          `tenant` int(11) DEFAULT NULL,
                                          `create_by` varchar(255) DEFAULT NULL,
                                          `create_time` datetime DEFAULT NULL,
                                          `update_by` varchar(255) DEFAULT NULL,
                                          `update_time` datetime DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;