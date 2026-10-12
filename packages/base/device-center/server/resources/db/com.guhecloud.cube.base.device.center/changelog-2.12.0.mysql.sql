-- liquibase formatted sql

-- changeset fl:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_dev_data_share') AS column_exists;
CREATE TABLE `iot_dev_data_share` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `source_device_id` bigint NOT NULL COMMENT '源设备ID',
  `source_product_id` bigint NOT NULL COMMENT '源产品ID（冗余，便于查询显示）',
  `source_data_type` tinyint NOT NULL COMMENT '数据类型：1-属性 2-事件',
  `source_data_id` bigint NOT NULL COMMENT '源数据ID（属性ID或事件ID）',
  `source_data_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '源数据编码（冗余，便于查询显示）',
  `target_device_id` bigint NOT NULL COMMENT '目标设备ID',
  `target_product_id` bigint NOT NULL COMMENT '目标产品ID（冗余，便于查询显示）',
  `target_data_id` bigint NOT NULL COMMENT '目标数据ID（属性ID或事件ID）',
  `target_data_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '目标数据编码（冗余，便于查询显示）',
  `share_mode` tinyint NOT NULL DEFAULT '1' COMMENT '共享模式：1-共享（源和目标都接收） 2-转换（仅目标接收）',
  `extend_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '扩展配置（JSON格式，预留数据转换规则等）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `tenant` int unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除标记：0-未删除 1-已删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_source_device` (`source_device_id`) USING BTREE COMMENT '源设备索引',
  KEY `idx_target_device` (`target_device_id`) USING BTREE COMMENT '目标设备索引',
  KEY `idx_data_type` (`source_data_type`) USING BTREE COMMENT '数据类型索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='设备数据共享配置表';