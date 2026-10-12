-- liquibase formatted sql

-- changeset xiaofeng:ddl-1
-- ----------------------------
-- Table structure for command_dispatch_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `command_dispatch_record` (
    application_code VARCHAR(100) DEFAULT NULL COMMENT '模块应用',
    task_type VARCHAR(100) DEFAULT NULL COMMENT '任务类型',
    id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    task_name VARCHAR(100) DEFAULT NULL COMMENT '任务名称',
    send_details VARCHAR(3000) DEFAULT NULL COMMENT '发送明细JSON',
    terminal_ids VARCHAR(1000) DEFAULT NULL COMMENT '发送终端id集合，逗号分隔',
    send_status int DEFAULT NULL COMMENT '发送状态 0:成功，1：失败',
    send_result VARCHAR(3000) DEFAULT NULL COMMENT '发送结果',
    third_record_id VARCHAR(100) DEFAULT NULL COMMENT '第三方记录id',
    `deleted` bigint(20) DEFAULT NULL,
    `tenant` int(11) DEFAULT NULL,
    `create_by` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(255) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    CONSTRAINT idx_client_tenant UNIQUE (id, tenant),
    CONSTRAINT third_record_id_client UNIQUE (`third_record_id`,`application_code`,`task_type`,`tenant`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用模块子系统指令下发记录表';

