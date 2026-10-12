-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'alarm_reason_config') AS column_exists;
CREATE TABLE `alarm_reason_config`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `initialized` tinyint(1) NULL DEFAULT NULL COMMENT '是否为初始化的',
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原因',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原因备注',
  `tenant` int(20) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警原因配置' ROW_FORMAT = Dynamic;

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'alarm_record_handler_reason') AS column_exists;
CREATE TABLE `alarm_record_handler_reason` (
  `id` bigint(20) unsigned NOT NULL,
  `alarm_record_id` bigint(20) unsigned DEFAULT NULL COMMENT '报警记录主键',
  `alarm_reason_id` bigint(20) unsigned DEFAULT NULL COMMENT '报警原因主键',
  `alarm_reason_code` varchar(100) DEFAULT NULL COMMENT '报警原因编号',
  `alarm_reason` varchar(100) DEFAULT NULL COMMENT '报警原因',
  `alarm_reason_remark` varchar(300) DEFAULT NULL COMMENT '报警原因描述',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警处理原因';