-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- 创建历史轨迹表
CREATE TABLE `cube-biz-dispatch`.`dispatch_device_gps_history`  (
  `id` bigint(20) NOT NULL,
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称，可以是人员名称，也可以是设备名称',
  `device_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `upload_time` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `direction` int(10) NULL DEFAULT NULL COMMENT '方向',
  `lng` double NULL DEFAULT NULL COMMENT '经度',
  `lat` double NULL DEFAULT NULL COMMENT '纬度',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置信息',
  `height` double NULL DEFAULT NULL COMMENT '高度',
  `speed` double NULL DEFAULT NULL COMMENT '速度',
  `tenant` int(10) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_device_id_device_code_upload_time_tenant`(`device_id`, `device_code`, `upload_time`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备坐标信息历史记录' ROW_FORMAT = DYNAMIC;