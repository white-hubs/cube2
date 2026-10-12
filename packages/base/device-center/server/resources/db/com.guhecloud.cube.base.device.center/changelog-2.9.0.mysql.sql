-- liquibase formatted sql

-- changeset fl:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_dev_register_info') AS column_exists;
CREATE TABLE `iot_dev_register_info` (
    `id` bigint(20) NOT NULL,
    `comm_protocol_identifier` varchar(255) NOT NULL COMMENT '通信协议标识',
    `vendor` varchar(255) DEFAULT NULL COMMENT '厂商',
    `model` varchar(255) DEFAULT NULL COMMENT '型号',
    `sn` varchar(255) DEFAULT NULL COMMENT '序列号',
    `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户',
    `device_id` bigint(20) NOT NULL COMMENT '设备ID',
    PRIMARY KEY (`id`),
    KEY `dev_id` (`device_id`) USING BTREE,
    KEY `intrinsicIdentity` (`vendor`,`model`,`sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;