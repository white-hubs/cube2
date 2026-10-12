-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_warning_sign' ) AS table_exists;
CREATE TABLE  `sm_risk_warning_sign` (
  `id` bigint(20) NOT NULL,
  `sign_code` varchar(64) NOT NULL COMMENT '警示标志编号',
  `sign_name` varchar(100) NOT NULL COMMENT '警示标志名称',
  `sign_type` varchar(64) DEFAULT NULL COMMENT '标志类型',
  `icon_file_id` varchar(100) DEFAULT NULL COMMENT '图标文件ID',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_warning_sign_code_tenant_deleted` (`sign_code`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险警示标志';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_unit_type' ) AS table_exists;
CREATE TABLE `sm_risk_unit_type` (
  `id` bigint(20) NOT NULL,
  `type_code` varchar(64) NOT NULL COMMENT '单元类型编号',
  `type_name` varchar(100) NOT NULL COMMENT '单元类型名称',
  `default_identify_cycle_days` int(11) DEFAULT NULL COMMENT '默认辨识周期天数',
  `identify_dimension_desc` varchar(500) DEFAULT NULL COMMENT '辨识维度说明',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_unit_type_code_tenant_deleted` (`type_code`, `tenant`, `deleted`),
  UNIQUE KEY `uk_sm_risk_unit_type_name_tenant_deleted` (`type_name`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险单元类型';

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_assessment_method' ) AS table_exists;
CREATE TABLE `sm_risk_assessment_method` (
  `id` bigint(20) NOT NULL,
  `method_code` varchar(64) NOT NULL,
  `method_name` varchar(100) NOT NULL,
  `method_desc` varchar(500) DEFAULT NULL,
  `formula` varchar(200) DEFAULT NULL,
  `preset_flag` tinyint(1) NOT NULL DEFAULT 0,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_assessment_method_code_tenant_deleted` (`method_code`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估方法';

-- changeset lanchun:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_assessment_dimension' ) AS table_exists;
CREATE TABLE `sm_risk_assessment_dimension` (
  `id` bigint(20) NOT NULL,
  `method_id` bigint(20) NOT NULL,
  `dimension_code` varchar(64) NOT NULL,
  `dimension_name` varchar(100) NOT NULL,
  `score_range` varchar(50) DEFAULT NULL,
  `sort_no` int(11) DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_dimension_method` (`method_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估维度';

-- changeset lanchun:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_assessment_option' ) AS table_exists;
CREATE TABLE `sm_risk_assessment_option` (
  `id` bigint(20) NOT NULL,
  `dimension_id` bigint(20) NOT NULL,
  `option_name` varchar(500) NOT NULL COMMENT '评估选项判断标准',
  `score` decimal(18,4) NOT NULL DEFAULT 0,
  `sort_no` int(11) DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_option_dimension` (`dimension_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估维度选项';

-- changeset lanchun:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_assessment_level_rule' ) AS table_exists;
CREATE TABLE `sm_risk_assessment_level_rule` (
  `id` bigint(20) NOT NULL,
  `method_id` bigint(20) NOT NULL,
  `risk_level` varchar(32) NOT NULL,
  `min_score` decimal(18,4) DEFAULT NULL,
  `max_score` decimal(18,4) DEFAULT NULL,
  `color` varchar(32) DEFAULT NULL,
  `control_requirement` varchar(200) DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_level_rule_method` (`method_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估等级规则';

-- changeset lanchun:ddl-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_area' ) AS table_exists;
CREATE TABLE `sm_risk_area` (
  `id` bigint(20) NOT NULL,
  `area_code` varchar(64) NOT NULL,
  `area_name` varchar(100) NOT NULL,
  `department_id` varchar(100) DEFAULT NULL,
  `range_desc` varchar(500) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL COMMENT '备注',
  `owner_user_id` varchar(100) DEFAULT NULL,
  `area_type` varchar(64) DEFAULT NULL,
  `gis_geometry` longtext DEFAULT NULL,
  `inherent_risk_level` varchar(32) DEFAULT NULL COMMENT '区域固有风险等级，按风险单元固有风险等级取最高值',
  `control_risk_level` varchar(32) DEFAULT NULL COMMENT '区域现状风险等级，按风险单元控制风险等级取最高值',
  `risk_level` varchar(32) DEFAULT NULL COMMENT '区域当前风险等级，按风险单元当前等级取最高值',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_area_code_tenant_deleted` (`area_code`, `tenant`, `deleted`),
  KEY `idx_sm_risk_area_inherent_risk_tenant_deleted` (`inherent_risk_level`, `tenant`, `deleted`),
  KEY `idx_sm_risk_area_control_risk_tenant_deleted` (`control_risk_level`, `tenant`, `deleted`),
  KEY `idx_sm_risk_area_risk_tenant_deleted` (`risk_level`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险区域';

-- changeset lanchun:ddl-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_unit' ) AS table_exists;
CREATE TABLE `sm_risk_unit` (
  `id` bigint(20) NOT NULL,
  `unit_code` varchar(64) NOT NULL,
  `unit_name` varchar(100) NOT NULL,
  `area_id` bigint(20) DEFAULT NULL,
  `unit_type_id` bigint(20) DEFAULT NULL,
  `responsible_user_id` varchar(100) DEFAULT NULL,
  `identify_cycle_days` int(11) DEFAULT NULL,
  `identify_status` varchar(32) NOT NULL,
  `current_identify_round_no` varchar(100) DEFAULT NULL,
  `last_identify_submit_time` datetime DEFAULT NULL,
  `last_identify_review_time` datetime DEFAULT NULL,
  `next_identify_time` datetime DEFAULT NULL,
  `location_desc` varchar(300) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `inherent_risk_level` varchar(32) DEFAULT NULL,
  `control_risk_level` varchar(32) DEFAULT NULL,
  `risk_level` varchar(32) DEFAULT NULL COMMENT '单元当前风险等级，按风险对象当前等级取最高值',
  `control_status` varchar(32) DEFAULT NULL,
  `gis_point` longtext DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_unit_code_tenant_deleted` (`unit_code`, `tenant`, `deleted`),
  KEY `idx_sm_risk_unit_risk_tenant_deleted` (`risk_level`, `tenant`, `deleted`),
  KEY `idx_sm_risk_unit_area_status_tenant_deleted` (`area_id`, `identify_status`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险单元';

-- changeset lanchun:ddl-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_unit_sign_rel' ) AS table_exists;
CREATE TABLE `sm_risk_unit_sign_rel` (
  `id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `sign_id` bigint(20) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_unit_sign_rel_unit` (`unit_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险单元警示标志关系';

-- changeset lanchun:ddl-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_unit_identifier_rel' ) AS table_exists;
CREATE TABLE `sm_risk_unit_identifier_rel` (
  `id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_unit_identifier_rel_unit` (`unit_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险单元辨识人员关系';

-- changeset lanchun:ddl-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_object' ) AS table_exists;
CREATE TABLE `sm_risk_object` (
  `id` bigint(20) NOT NULL,
  `object_code` varchar(64) NOT NULL,
  `object_name` varchar(100) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `asset_id` bigint(20) DEFAULT NULL COMMENT '资产管理服务资产ID',
  `identify_round_no` varchar(100) DEFAULT NULL,
  `responsible_user_id` varchar(100) DEFAULT NULL,
  `status` varchar(32) NOT NULL,
  `inherent_risk_level` varchar(32) DEFAULT NULL,
  `control_risk_level` varchar(32) DEFAULT NULL,
  `hazard_desc` varchar(1000) DEFAULT NULL,
  `accident_types` varchar(500) DEFAULT NULL,
  `accident_cause` varchar(1000) DEFAULT NULL,
  `trigger_factor` varchar(1000) DEFAULT NULL,
  `emergency_measure` varchar(1000) DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_object_code_tenant_deleted` (`object_code`, `tenant`, `deleted`),
  KEY `idx_sm_risk_object_unit_round_status_tenant_deleted` (`unit_id`, `identify_round_no`, `status`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险对象';

-- changeset lanchun:ddl-14
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_identification_record' ) AS table_exists;
CREATE TABLE `sm_risk_identification_record` (
  `id` bigint(20) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `identify_round_no` varchar(100) NOT NULL,
  `operation_type` varchar(32) NOT NULL,
  `identifier_user_id` varchar(100) DEFAULT NULL,
  `submit_time` datetime NOT NULL,
  `snapshot_json` longtext DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_identification_record_unit` (`unit_id`, `identify_round_no`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险辨识记录';

-- changeset lanchun:ddl-15
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_review_record' ) AS table_exists;
CREATE TABLE `sm_risk_review_record` (
  `id` bigint(20) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `identify_round_no` varchar(100) NOT NULL,
  `review_mode` varchar(32) NOT NULL,
  `review_batch_no` varchar(100) DEFAULT NULL,
  `review_result` varchar(32) NOT NULL,
  `reject_reason` varchar(500) DEFAULT NULL,
  `review_comment` varchar(500) DEFAULT NULL,
  `reviewer_user_id` varchar(100) DEFAULT NULL,
  `review_time` datetime NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_review_record_object` (`object_id`, `identify_round_no`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险辨识审核记录';

-- changeset lanchun:ddl-16
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_assessment_record' ) AS table_exists;
CREATE TABLE `sm_risk_assessment_record` (
  `id` bigint(20) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `method_id` bigint(20) NOT NULL,
  `assessment_type` varchar(32) NOT NULL,
  `total_score` decimal(18,4) DEFAULT NULL,
  `risk_level` varchar(32) DEFAULT NULL,
  `method_snapshot_json` longtext DEFAULT NULL,
  `score_detail_json` longtext DEFAULT NULL,
  `submitted` tinyint(1) NOT NULL DEFAULT 1,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_record_object` (`object_id`, `assessment_type`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估记录';

-- changeset lanchun:ddl-17
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_change_log' ) AS table_exists;
CREATE TABLE `sm_risk_change_log` (
  `id` bigint(20) NOT NULL,
  `business_type` varchar(64) NOT NULL,
  `business_id` bigint(20) NOT NULL,
  `change_type` varchar(64) NOT NULL,
  `before_json` longtext DEFAULT NULL,
  `after_json` longtext DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_change_log_business` (`business_type`, `business_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险变更日志';

-- changeset lanchun:ddl-18
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_control_measure' ) AS table_exists;
CREATE TABLE `sm_risk_control_measure` (
  `id` bigint(20) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `control_type` varchar(64) DEFAULT NULL,
  `control_level` varchar(64) DEFAULT NULL,
  `executor_user_id` varchar(100) DEFAULT NULL,
  `content` varchar(1000) NOT NULL,
  `execution_status` varchar(32) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_control_measure_object` (`object_id`, `execution_status`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险管控措施';

-- changeset lanchun:ddl-19
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_measure_execution_record' ) AS table_exists;
CREATE TABLE `sm_risk_measure_execution_record` (
  `id` bigint(20) NOT NULL,
  `measure_id` bigint(20) NOT NULL,
  `execution_result` varchar(100) DEFAULT NULL,
  `execution_desc` varchar(1000) DEFAULT NULL,
  `executor_user_id` varchar(100) DEFAULT NULL,
  `execution_time` datetime NOT NULL,
  `gps_location` longtext DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_measure_execution_record_measure` (`measure_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险措施执行记录';

-- changeset lanchun:ddl-20
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_measure_attachment' ) AS table_exists;
CREATE TABLE `sm_risk_measure_attachment` (
  `id` bigint(20) NOT NULL,
  `execution_record_id` bigint(20) NOT NULL,
  `file_id` varchar(100) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_measure_attachment_record` (`execution_record_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险措施执行附件';

-- changeset lanchun:ddl-21
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_assessment_plan' ) AS table_exists;
CREATE TABLE `sm_risk_assessment_plan` (
  `id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `plan_name` varchar(100) NOT NULL,
  `cycle_type` varchar(32) DEFAULT NULL,
  `next_execute_time` datetime DEFAULT NULL,
  `last_execute_time` datetime DEFAULT NULL,
  `status` varchar(32) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_plan_unit_status` (`unit_id`, `status`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估计划';

-- changeset lanchun:ddl-22
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_plan_notify_user' ) AS table_exists;
CREATE TABLE `sm_risk_plan_notify_user` (
  `id` bigint(20) NOT NULL,
  `plan_id` bigint(20) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_plan_notify_user_plan` (`plan_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估计划通知人员';

-- changeset lanchun:ddl-23
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_warning_rule' ) AS table_exists;
CREATE TABLE `sm_risk_warning_rule` (
  `id` bigint(20) NOT NULL,
  `warning_type` varchar(64) NOT NULL,
  `condition_json` longtext DEFAULT NULL,
  `severity` varchar(32) DEFAULT NULL,
  `notify_channels` varchar(200) DEFAULT NULL,
  `dispose_deadline_days` int(11) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_warning_rule_enabled` (`enabled`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险预警规则';

-- changeset lanchun:ddl-24
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'sm_risk_warning_record' ) AS table_exists;
CREATE TABLE `sm_risk_warning_record` (
  `id` bigint(20) NOT NULL,
  `rule_id` bigint(20) DEFAULT NULL,
  `unit_id` bigint(20) DEFAULT NULL,
  `object_id` bigint(20) DEFAULT NULL,
  `severity` varchar(32) DEFAULT NULL,
  `warning_status` varchar(32) NOT NULL,
  `trigger_time` datetime DEFAULT NULL,
  `dispose_time` datetime DEFAULT NULL,
  `dispose_desc` varchar(500) DEFAULT NULL COMMENT '处置说明',
  `dispose_result` varchar(50) DEFAULT NULL COMMENT '处置结果',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_warning_record_status` (`warning_status`, `unit_id`, `object_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险预警记录';
