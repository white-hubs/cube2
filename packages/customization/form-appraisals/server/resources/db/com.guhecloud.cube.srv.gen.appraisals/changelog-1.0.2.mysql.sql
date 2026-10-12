-- liquibase formatted sql


-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_process') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `form_appraisal_process` (
  `id` bigint NOT NULL COMMENT '主键',
  `process_instance_id` varchar(100) NOT NULL COMMENT '流程实例ID',
  `user_id` bigint DEFAULT NULL COMMENT '发起人ID',
  `organization_id` bigint DEFAULT NULL COMMENT '部门ID',
  `post_ids` varchar(500) DEFAULT NULL COMMENT '岗位IDS',
  `role_ids` varchar(500) DEFAULT NULL COMMENT '角色IDS',
  `appointment_ids` varchar(500) DEFAULT NULL COMMENT '职位IDS',
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
  KEY `idx_fap_process_instance_id` (`process_instance_id`),
  KEY `idx_fap_user_id` (`user_id`),
  KEY `idx_fap_org_id` (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单考核流程记录表';
