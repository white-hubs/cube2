-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `work_overtime_rule_detail` DROP COLUMN `min_minute`;
ALTER TABLE `work_overtime_rule_detail` ADD COLUMN `work_condition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加班审批条件' AFTER `proportion`;
ALTER TABLE `work_overtime_rule_detail` ADD COLUMN `units` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算单位(半小时、小时、天)' AFTER `work_condition`;

CREATE TABLE `work_overtime_ledger`  (
  `id` bigint(20) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `org_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `work_date` date NOT NULL COMMENT '加班时间',
  `work_duration` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '工作日时长（时）',
  `work_pay_duration` int(11) NULL DEFAULT NULL COMMENT '工作时长记为加班费时长',
  `rest_duration` int(11) NULL DEFAULT NULL COMMENT '休息日加班时长',
  `rest_pat_duration` int(11) NULL DEFAULT NULL COMMENT '休息日加班记加班费时长',
  `fest_duration` int(11) NULL DEFAULT NULL COMMENT '节假日加班时长',
  `fest_oat_duration` int(11) NULL DEFAULT NULL COMMENT '节假日记加班费时长',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `work_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班类型',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `apply_done_time` datetime NULL DEFAULT NULL COMMENT '申请处理完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '加班台账' ROW_FORMAT = Dynamic;