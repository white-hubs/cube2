-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule_issued_iot') AS column_exists;
CREATE TABLE `medical_rule_issued_iot` (
  `id` bigint(20) unsigned NOT NULL,
  `issued_status` varchar(50) DEFAULT NULL COMMENT '下发状态',
  `lastIssued_time` datetime DEFAULT NULL COMMENT '最后下发时间',
  `tenant` int(10) DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规则下发到iot记录';