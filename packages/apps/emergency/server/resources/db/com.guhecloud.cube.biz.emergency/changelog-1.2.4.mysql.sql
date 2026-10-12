-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-emergency`.`emergency_rule` ADD COLUMN `appoint_device` tinyint(1) NULL DEFAULT NULL COMMENT '是否指定设备' AFTER `rule_period_timed`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition` CHANGE  `device_genre_id` `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号';

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition` ADD COLUMN `device_prod_category_code_concat` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号序列号' AFTER `device_prod_category_code`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition` CHANGE `device_genre_name` device_prod_category_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称';

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition` CHANGE `device_model_id` `device_prod_id` bigint(20) NULL DEFAULT NULL COMMENT '设备产品主键';

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition` CHANGE `device_model_name` `device_prod_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称';

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition` ADD COLUMN `group_id` bigint(20) NULL DEFAULT NULL COMMENT '条件组主键' AFTER `logical_op`;


ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition_device` CHANGE `device_genre_id` `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号';

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition_device` ADD COLUMN `device_prod_category_code_concat` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号序列号' AFTER `device_prod_category_code`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition_device` CHANGE `device_genre_name` `device_prod_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称' AFTER `device_prod_category_code_concat`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition_device` CHANGE `device_model_id` `device_prod_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备产品主键';

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition_device` CHANGE `device_model_name` `device_prod_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称';

ALTER TABLE `cube-biz-emergency`.`emergency_rule_condition_exp` ADD COLUMN `group_id` bigint(20) NULL DEFAULT NULL COMMENT '条件组主键' AFTER `end_delay_time`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_time` ADD COLUMN `group_id` bigint(20) NULL DEFAULT NULL COMMENT '条件组主键' AFTER `emergency_rule_id`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_time` ADD COLUMN `time_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时段类型' AFTER `group_id`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_time` ADD COLUMN `time_conditions` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间条件' AFTER `time_type`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_time` MODIFY COLUMN `start_time` datetime NULL DEFAULT NULL AFTER `time_conditions`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule_time` MODIFY COLUMN `end_time` datetime NULL DEFAULT NULL AFTER `start_time`;



CREATE TABLE `cube-biz-emergency`.`emergency_rule_condition_group`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则条件组' ROW_FORMAT = DYNAMIC;


CREATE TABLE `cube-biz-emergency`.`emergency_rule_time_job`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则id',
  `emergency_rule_condition_group_id` bigint(20) NULL DEFAULT NULL COMMENT '条件组id',
  `emergency_rule_time_id` bigint(20) NULL DEFAULT NULL COMMENT '时间配置主键',
  `time_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间周期类型',
  `time_conditions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '单次时间条件',
  `rule_time_conditions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '时间条件',
  `operate_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `trigger_last_time` bigint(13) NULL DEFAULT NULL COMMENT '上次执行时间',
  `trigger_next_time` bigint(13) NULL DEFAULT NULL COMMENT '下次执行时间',
  `tenant` int(10) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE `cube-biz-emergency`.`emergency_rule_time_job_log`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `emergency_rule_time_job_id` bigint(20) NULL DEFAULT NULL,
  `open_or_close` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开启规则or关闭规则',
  `execute_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行状态',
  `trigger_time` datetime NULL DEFAULT NULL COMMENT '触发时间',
  `handler_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `tenant` int(10) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案时间任务执行记录' ROW_FORMAT = DYNAMIC;