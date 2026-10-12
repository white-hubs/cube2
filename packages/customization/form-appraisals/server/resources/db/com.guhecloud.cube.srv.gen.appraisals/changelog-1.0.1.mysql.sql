-- liquibase formatted sql


-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_rule') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `form_appraisal_rule` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(200) NOT NULL COMMENT '规则名称',
  `inspection_item` varchar(200) NOT NULL COMMENT '检查项目',
  `period_type` varchar(32) NOT NULL COMMENT '周期类型',
  `total_score` decimal(10,2) NOT NULL COMMENT '总分',
  `spot_check_score` decimal(10,2) NOT NULL COMMENT '抽查分',
  `inspection_score` decimal(10,2) NOT NULL COMMENT '检查分',
  `responsible_department_ids` varchar(500) NOT NULL COMMENT '责任部门ID列表，逗号分隔',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '启用状态',
  `rule_version` int NOT NULL DEFAULT 1 COMMENT '规则版本',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建人ID',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建人类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新人',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新人ID',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新人类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`),
  KEY `idx_far_period_type` (`period_type`),
  KEY `idx_far_enabled` (`enabled`),
  KEY `idx_far_department` (`responsible_department_ids`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单考核规则';

-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_rule_content') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `form_appraisal_rule_content` (
  `id` bigint NOT NULL COMMENT '主键',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `content_type` varchar(32) NOT NULL COMMENT '内容类型',
  `source_type` varchar(32) NOT NULL COMMENT '数据来源类型',
  `source_ref_id` varchar(64) NOT NULL COMMENT '底稿关联ID',
  `source_ref_code` varchar(64) NOT NULL COMMENT '底稿关联code',
  `source_ref_name` varchar(200) DEFAULT NULL COMMENT '底稿关联名称',
  `content_desc` varchar(500) NOT NULL COMMENT '内容描述',
  `standard_desc` varchar(1000) DEFAULT NULL COMMENT '标准描述',
  `sort` int DEFAULT 0 COMMENT '排序',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建人ID',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建人类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新人',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新人ID',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新人类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`),
  KEY `idx_farc_rule_id` (`rule_id`),
  KEY `idx_farc_rule_sort` (`rule_id`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单考核规则内容';

-- changeset chx:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_rule_item') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `form_appraisal_rule_item` (
  `id` bigint NOT NULL COMMENT '主键',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `content_id` bigint NOT NULL COMMENT '规则内容ID',
  `item_name` varchar(200) NOT NULL COMMENT '项名称',
  `field_key` varchar(128) DEFAULT NULL COMMENT '字段KEY',
  `calc_type` varchar(64) NOT NULL COMMENT '计算方式',
  `range_config_json` text NOT NULL COMMENT '扣分区间配置JSON',
  `sort` int DEFAULT 0 COMMENT '排序',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建人ID',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建人类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新人',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新人ID',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新人类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`),
  KEY `idx_fari_content_id` (`content_id`),
  KEY `idx_fari_content_sort` (`content_id`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单考核规则项';

-- changeset chx:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `form_appraisal_record` (
  `id` bigint NOT NULL COMMENT '主键',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `rule_version` int NOT NULL COMMENT '规则版本',
  `name` varchar(200) NOT NULL COMMENT '规则名称快照',
  `inspection_item` varchar(200) NOT NULL COMMENT '检查项目快照',
  `period_type` varchar(32) NOT NULL COMMENT '周期类型',
  `assessment_year` int NOT NULL COMMENT '考核年',
  `assessment_period` int NOT NULL COMMENT '考核周期值',
  `assessment_begin_time` datetime NOT NULL COMMENT '考核开始时间',
  `assessment_end_time` datetime NOT NULL COMMENT '考核结束时间',
  `responsible_department_ids` varchar(500) NOT NULL COMMENT '责任部门ID列表，逗号分隔',
  `responsible_department_names` varchar(500) DEFAULT NULL COMMENT '责任部门名称列表，逗号分隔',
  `origin_total_score` decimal(10,2) NOT NULL COMMENT '原始总分',
  `origin_spot_check_score` decimal(10,2) NOT NULL COMMENT '原始抽查分',
  `origin_inspection_score` decimal(10,2) NOT NULL COMMENT '原始检查分',
  `override_final_total_score` decimal(10,2) DEFAULT NULL COMMENT '覆盖总分',
  `override_final_spot_check_score` decimal(10,2) DEFAULT NULL COMMENT '覆盖抽查分',
  `override_final_inspection_score` decimal(10,2) DEFAULT NULL COMMENT '覆盖检查分',
  `final_total_score` decimal(10,2) NOT NULL COMMENT '最终总分',
  `final_spot_check_score` decimal(10,2) NOT NULL COMMENT '最终抽查分',
  `final_inspection_score` decimal(10,2) NOT NULL COMMENT '最终检查分',
  `status` varchar(32) NOT NULL COMMENT '记录状态',
  `generated_at` datetime NOT NULL COMMENT '生成时间',
  `adjust_remark` varchar(1000) DEFAULT NULL COMMENT '整单调整原因',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建人ID',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建人类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新人',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新人ID',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新人类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_far_record` (`tenant`, `rule_id`, `period_type`, `assessment_year`, `assessment_period`),
  KEY `idx_far_record_department` (`responsible_department_ids`),
  KEY `idx_far_record_generated` (`generated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单考核记录';

-- changeset chx:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record_detail') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `form_appraisal_record_detail` (
  `id` bigint NOT NULL COMMENT '主键',
  `record_id` bigint NOT NULL COMMENT '记录ID',
  `rule_content_id` bigint DEFAULT NULL COMMENT '规则内容ID',
  `rule_item_id` bigint DEFAULT NULL COMMENT '规则项ID',
  `content_type` varchar(32) NOT NULL COMMENT '内容类型',
  `source_type` varchar(32) NOT NULL COMMENT '来源类型',
  `source_ref_id` varchar(64) DEFAULT NULL COMMENT '底稿关联ID',
  `source_ref_name` varchar(200) DEFAULT NULL COMMENT '底稿关联名称',
  `content_desc` varchar(500) NOT NULL COMMENT '内容描述',
  `standard_desc` varchar(1000) DEFAULT NULL COMMENT '标准描述',
  `item_name` varchar(200) NOT NULL COMMENT '项名称',
  `field_key` varchar(128) DEFAULT NULL COMMENT '字段KEY',
  `calc_type` varchar(64) NOT NULL COMMENT '计算方式',
  `raw_value` varchar(200) DEFAULT NULL COMMENT '原始值',
  `matched_range_text` varchar(500) DEFAULT NULL COMMENT '命中区间文本',
  `origin_deduct_score` decimal(10,2) NOT NULL COMMENT '原始扣分',
  `manual_adjust_deduct_score` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '人工追加扣分',
  `final_deduct_score` decimal(10,2) NOT NULL COMMENT '最终扣分',
  `remark` varchar(1000) DEFAULT NULL COMMENT '单条明细备注',
  `sort` int DEFAULT 0 COMMENT '排序',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建人ID',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建人类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新人',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新人ID',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新人类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`),
  KEY `idx_fard_record_id` (`record_id`),
  KEY `idx_fard_record_sort` (`record_id`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单考核记录明细';
