-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_report') AS column_exists;
CREATE TABLE `safety_report` (
                                 `id` bigint(20) NOT NULL,
                                 `name` varchar(255) NOT NULL COMMENT '标题',
                                 `file_id` bigint(20) DEFAULT NULL COMMENT '附件id',
                                 `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
                                 `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
                                 `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
                                 `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;