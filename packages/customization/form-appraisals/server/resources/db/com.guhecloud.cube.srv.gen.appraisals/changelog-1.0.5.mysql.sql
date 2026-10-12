-- liquibase formatted sql


-- changeset ghy:ddl-preview-record-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_preview_record';
CREATE TABLE IF NOT EXISTS `form_appraisal_preview_record` (
  `id` bigint NOT NULL COMMENT '主键',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `rule_version` int NOT NULL COMMENT '规则版本',
  `name` varchar(200) NOT NULL COMMENT '规则名称快照',
  `inspection_item` varchar(200) NOT NULL COMMENT '检查项目快照',
  `period_type` varchar(32) NOT NULL COMMENT '周期类型',
  `assessment_year` int NOT NULL COMMENT '考核年',
  `assessment_period` int NOT NULL COMMENT '考核周期值',
  `assessment_begin_time` datetime NOT NULL COMMENT '考核开始时间',
  `assessment_end_time` datetime NOT NULL COMMENT '本次预览统计截止时间',
  `responsible_department_ids` varchar(500) NOT NULL COMMENT '责任部门ID列表，逗号分隔',
  `responsible_department_names` varchar(500) DEFAULT NULL COMMENT '责任部门名称列表，逗号分隔',
  `first_level_form_group_id` bigint DEFAULT NULL COMMENT '一级流程表单分组ID快照',
  `first_level_form_group_name` varchar(200) DEFAULT NULL COMMENT '一级流程表单分组名称快照',
  `second_level_form_group_id` bigint DEFAULT NULL COMMENT '二级流程表单分组ID快照',
  `second_level_form_group_name` varchar(200) DEFAULT NULL COMMENT '二级流程表单分组名称快照',
  `origin_total_score` decimal(10,2) NOT NULL COMMENT '原始总分',
  `origin_spot_check_score` decimal(10,2) NOT NULL COMMENT '原始抽查分',
  `origin_inspection_score` decimal(10,2) NOT NULL COMMENT '原始检查分',
  `final_total_score` decimal(10,2) NOT NULL COMMENT '预览最终总分',
  `final_spot_check_score` decimal(10,2) NOT NULL COMMENT '预览最终抽查分',
  `final_inspection_score` decimal(10,2) NOT NULL COMMENT '预览最终检查分',
  `preview_as_of_time` datetime NOT NULL COMMENT '预览统计截止时间',
  `generated_at` datetime NOT NULL COMMENT '预览生成时间',
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
  UNIQUE KEY `uk_fapr_record` (`tenant`, `rule_id`, `period_type`, `assessment_year`, `assessment_period`),
  KEY `idx_fapr_period` (`tenant`, `period_type`, `assessment_year`, `assessment_period`),
  KEY `idx_fapr_preview_time` (`preview_as_of_time`),
  KEY `idx_fapr_first_group` (`first_level_form_group_id`),
  KEY `idx_fapr_second_group` (`second_level_form_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单考核预览记录';

-- changeset ghy:ddl-preview-record-detail-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_preview_record_detail';
CREATE TABLE IF NOT EXISTS `form_appraisal_preview_record_detail` (
  `id` bigint NOT NULL COMMENT '主键',
  `record_id` bigint NOT NULL COMMENT '预览记录ID',
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
  `deduction_reason_json` text COMMENT '系统生成扣分原因JSON快照',
  `origin_deduct_score` decimal(10,2) NOT NULL COMMENT '原始扣分',
  `manual_adjust_deduct_score` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '人工追加扣分',
  `final_deduct_score` decimal(10,2) NOT NULL COMMENT '最终扣分',
  `remark` varchar(1000) DEFAULT NULL COMMENT '明细备注',
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
  KEY `idx_faprd_record_id` (`record_id`),
  KEY `idx_faprd_record_sort` (`record_id`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单考核预览记录明细';


-- changeset ghy:ddl-item-field-name-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_rule_item' AND column_name = 'field_name';
ALTER TABLE form_appraisal_rule_item ADD field_name varchar(100) NULL COMMENT '关联字段的名称';
ALTER TABLE form_appraisal_rule_item CHANGE field_name field_name varchar(100) NULL COMMENT '关联字段的名称' AFTER field_key;

-- changeset ghy:ddl-detail-field-name-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_preview_record_detail' AND column_name = 'field_name';
ALTER TABLE form_appraisal_preview_record_detail ADD field_name varchar(100) NULL COMMENT '关联字段名称';
ALTER TABLE form_appraisal_preview_record_detail CHANGE field_name field_name varchar(100) NULL COMMENT '关联字段名称' AFTER field_key;

-- changeset ghy:ddl-detail-field-name-2
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record_detail' AND column_name = 'field_name';
ALTER TABLE form_appraisal_record_detail ADD field_name varchar(100) NULL COMMENT '关联字段名称';
ALTER TABLE form_appraisal_record_detail CHANGE field_name field_name varchar(100) NULL COMMENT '关联字段名称' AFTER field_key;
