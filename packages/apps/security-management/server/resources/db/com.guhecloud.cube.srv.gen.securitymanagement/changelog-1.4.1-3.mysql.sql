-- liquibase formatted sql

-- changeset qqq:1686103358461-1
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for security_panel_config
-- ----------------------------
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'passenger_flow_record') AS column_exists;
CREATE TABLE `passenger_flow_record` (
                                         `id` bigint(20) NOT NULL,
                                         `record_id` varchar(100) DEFAULT NULL COMMENT '记录主键',
                                         `area_name` varchar(100) DEFAULT NULL COMMENT '区域',
                                         `record_device` varchar(100) DEFAULT NULL COMMENT '记录产生设备',
                                         `record_time` datetime DEFAULT NULL COMMENT '记录时间',
                                         `in_out` varchar(50) DEFAULT NULL COMMENT '进出(1进，0出)',
                                         `deleted` bigint(20) DEFAULT NULL,
                                         `tenant` int(11) DEFAULT NULL,
                                         `create_by` varchar(255) DEFAULT NULL,
                                         `create_time` datetime DEFAULT NULL,
                                         `update_by` varchar(255) DEFAULT NULL,
                                         `update_time` datetime DEFAULT NULL,
                                         `record_num` int(5) DEFAULT '1' COMMENT '人数',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `an_time_inout_index` (`area_name`,`record_time`,`in_out`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客流记录';

SET FOREIGN_KEY_CHECKS = 1;