-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `passenger_flow_record`  (
  `id` bigint(20) NOT NULL,
  `record_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录主键',
  `area_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `record_device` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录产生设备',
  `record_time` datetime(0) NULL DEFAULT NULL COMMENT '记录时间',
  `in_out` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '进出(1进，0出)',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `record_num` int(5) NULL DEFAULT 1 COMMENT '人数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `an_time_inout_index`(`area_name`, `record_time`, `in_out`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客流记录' ROW_FORMAT = Dynamic;
