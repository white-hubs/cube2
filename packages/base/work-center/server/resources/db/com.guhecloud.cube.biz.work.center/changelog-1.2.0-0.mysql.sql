-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'handover_record_extend') AS column_exists;
CREATE TABLE `handover_record_extend` (
    `id`           BIGINT       NOT NULL COMMENT '主键ID',
    `handover_id`  BIGINT       NOT NULL COMMENT '交接记录ID',
    `att_info1`    VARCHAR(500) NULL COMMENT '附加信息1',
    `file1`        BIGINT       NULL COMMENT '交接扩展-业务文件key1',
    `att_info2`    VARCHAR(500) NULL COMMENT '附加信息2',
    `file2`        BIGINT       NULL COMMENT '交接扩展-业务文件key2',
    `att_info3`    VARCHAR(500) NULL COMMENT '附加信息3',
    `file3`        BIGINT       NULL COMMENT '交接扩展-业务文件key3',
    `att_info4`    VARCHAR(500) NULL COMMENT '附加信息4',
    `file4`        BIGINT       NULL COMMENT '交接扩展-业务文件key4',
  `tenant` int unsigned NOT NULL COMMENT '租户编号',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',

    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT = '交班记录扩展信息';