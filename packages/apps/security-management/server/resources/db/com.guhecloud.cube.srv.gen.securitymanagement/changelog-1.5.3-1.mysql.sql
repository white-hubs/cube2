-- liquibase formatted sql

-- changeset chx:ddl-1
-- ----------------------------
-- Table structure for five_call_log
-- ----------------------------
CREATE table IF NOT EXISTS `five_call_log` (
    `id` bigint(20) NOT NULL,
    `caller` varchar(50) DEFAULT NULL COMMENT '来电人',
    `call_time` datetime DEFAULT NULL COMMENT '来电时间',
    `lift_number` varchar(50) DEFAULT NULL COMMENT '电梯编号',
    `area_id` varchar(50) DEFAULT NULL COMMENT '区域id',
    `area_name` varchar(100) DEFAULT NULL COMMENT '区域名称',
    `sequence` varchar(500) DEFAULT NULL COMMENT '区域序列号',
    `call_content` varchar(500) DEFAULT NULL COMMENT '通话内容',
    `handling_situation` varchar(300) DEFAULT NULL COMMENT '处理情况',
    `tenant` int(11) DEFAULT NULL,
    `deleted` bigint(20) NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `five_call_log_call_time_IDX` (`call_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC
