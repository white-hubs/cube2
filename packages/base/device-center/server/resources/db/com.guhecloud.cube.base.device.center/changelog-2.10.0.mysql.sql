-- liquibase formatted sql

-- changeset fl:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_dev_device_comm_info') AS column_exists;
CREATE TABLE `iot_dev_device_comm_info` (
    `device_id` bigint NOT NULL COMMENT '设备ID',
    `endpoint_id` varchar(255) DEFAULT NULL COMMENT '设备通信协议',
    `protocol_id` varchar(255) DEFAULT NULL COMMENT '设备通信协议ID',
    `comm_key` varchar(255) DEFAULT NULL COMMENT '设备通信key',
    `comm_extra` text COMMENT '设备通信扩展信息',
    `imei` varchar(255) DEFAULT NULL COMMENT 'IMEI',
    `imsi` varchar(255) DEFAULT NULL COMMENT 'IMSI',
    `iccid` varchar(255) DEFAULT NULL COMMENT 'ICCID',
    PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备通信信息表';

-- changeset fl:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_inner_subscription') AS column_exists;
CREATE TABLE `iot_inner_subscription` (
      `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订阅ID',
      `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订阅名称',
      `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '订阅描述',
      `subscriber_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订阅者ID',
      `tenant_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '租户ID列表，JSON字符串格式：["tenant1","tenant2"]',
      `device_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '设备ID列表，JSON字符串格式：["device1","device2"]',
      `device_types` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '设备类型列表，JSON字符串格式：["type1","type2"]',
      `product_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '产品ID列表，JSON字符串格式：["product1","product2"]',
      `event_types` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '事件类型列表，JSON字符串格式：["event1","event2"]',
      `push_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '推送配置，JSON字符串格式',
      `filter_expression` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '过滤表达式',
      `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '扩展属性，JSON字符串格式',
      `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
      `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`),
      KEY `idx_subscriber_id` (`subscriber_id`),
      KEY `idx_enabled` (`enabled`),
      KEY `idx_created_at` (`created_at`),
      KEY `idx_updated_at` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订阅配置表';