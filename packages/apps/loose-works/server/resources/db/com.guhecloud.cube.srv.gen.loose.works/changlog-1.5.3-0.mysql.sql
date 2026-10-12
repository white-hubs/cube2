-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_inspect_record') AS column_exists;
CREATE TABLE `project_inspect_record` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(100) DEFAULT NULL,
  `project_schedule_id` bigint(20) NOT NULL COMMENT '巡查工程',
  `result` varchar(32) DEFAULT NULL COMMENT '巡查结果',
  `user_id` bigint(20) DEFAULT NULL COMMENT '巡查人',
  `remarks` varchar(500) DEFAULT NULL COMMENT '描述',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工程临时巡查记录';