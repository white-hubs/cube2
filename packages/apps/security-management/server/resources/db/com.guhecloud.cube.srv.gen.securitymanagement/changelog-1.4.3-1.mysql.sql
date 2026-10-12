-- liquibase formatted sql

-- changeset chx:ddl-1
-- ----------------------------
-- Table structure for environment_hour_stat
-- ----------------------------
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'environment_hour_stat') AS column_exists;
CREATE TABLE `environment_hour_stat` (
                                         `id` bigint(20) NOT NULL,
                                         `stat_time` datetime DEFAULT NULL COMMENT '统计时间',
                                         `stat_day` varchar(12) DEFAULT NULL COMMENT '统计日期字符串',
                                         `pm_two_point_five` DECIMAL(10,2) default 0 COMMENT 'PM2.5',
                                         `deleted` bigint(20) DEFAULT NULL,
                                         `tenant` int(11) DEFAULT NULL,
                                         `create_by` varchar(255) DEFAULT NULL,
                                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                         `update_by` varchar(255) DEFAULT NULL,
                                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='环境检测小时表统计';
