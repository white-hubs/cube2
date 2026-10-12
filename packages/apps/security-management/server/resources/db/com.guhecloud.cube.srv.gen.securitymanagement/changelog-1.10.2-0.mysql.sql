-- liquibase formatted sql

-- changeset key-personnel:create-key-person-table
CREATE TABLE IF NOT EXISTS `key_person` (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `person_code` varchar(32) NOT NULL COMMENT '人员编号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `phone` varchar(50) NOT NULL COMMENT '手机号原值',
  `id_card` varchar(64) DEFAULT NULL COMMENT '身份证号原值',
  `area_id` bigint(20) UNSIGNED NOT NULL COMMENT 'mdata区域id',
  `area_name` varchar(100) NOT NULL COMMENT '区域名称快照',
  `area_sequence` varchar(500) NOT NULL COMMENT '区域序列号',
  `risk_level` varchar(16) NOT NULL COMMENT '风险等级 HIGH/MEDIUM/LOW',
  `supervisor_user_id` bigint(20) UNSIGNED NOT NULL COMMENT '监管人员用户id',
  `supervisor_name` varchar(50) NOT NULL COMMENT '监管人员姓名快照',
  `supervision_reason` varchar(500) NOT NULL COMMENT '监管原因',
  `status` varchar(16) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态 ACTIVE/INACTIVE',
  `photo_file_id` bigint(20) UNSIGNED DEFAULT NULL COMMENT '人员照片fileId',
  `person_attachment_file_ids` json DEFAULT NULL COMMENT '人员附件fileId数组',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_key_person_code_tenant_deleted` (`person_code`, `tenant`, `deleted`),
  KEY `idx_key_person_keyword_tenant_deleted` (`name`, `phone`, `tenant`, `deleted`),
  KEY `idx_key_person_risk_status_tenant_deleted` (`risk_level`, `status`, `tenant`, `deleted`),
  KEY `idx_key_person_area_seq_tenant_deleted` (`area_sequence`, `tenant`, `deleted`),
  KEY `idx_key_person_supervisor_tenant_deleted` (`supervisor_user_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重点人员';

-- changeset key-personnel:create-key-person-supervision-report-table
CREATE TABLE IF NOT EXISTS `key_person_supervision_report` (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `person_id` bigint(20) UNSIGNED NOT NULL COMMENT '重点人员id',
  `content` varchar(1000) NOT NULL COMMENT '上报内容',
  `report_attachment_file_ids` json DEFAULT NULL COMMENT '上报附件fileId数组',
  `report_time` datetime NOT NULL COMMENT '上报时间',
  `reporter_user_id` bigint(20) UNSIGNED DEFAULT NULL COMMENT '上报人用户id',
  `reporter_name` varchar(50) DEFAULT NULL COMMENT '上报人姓名',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_key_person_report_person_time_tenant_deleted` (`person_id`, `report_time`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重点人员监管上报';
