-- liquibase formatted sql

-- changeset ghy:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'parking_hourly_vehicle_flow') AS table_exists;
CREATE TABLE `parking_hourly_vehicle_flow` (
  `id` bigint NOT NULL,
  `device_id` bigint NOT NULL COMMENT '设备id',
  `record_time` datetime NOT NULL COMMENT '记录时间，精确到小时',
  `vehicle_count` int NOT NULL DEFAULT 0 COMMENT '车辆数',
  `tenant` int DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_device_record_time_tenant_deleted` (`device_id`, `record_time`, `tenant`, `deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='停车场设备小时车流记录表';
