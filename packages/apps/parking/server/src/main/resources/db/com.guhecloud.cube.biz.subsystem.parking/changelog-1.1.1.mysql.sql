-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'vehicle_bottom_monitor_record') AS column_exists;
CREATE TABLE `vehicle_bottom_monitor_record` (
  `id` bigint(20) NOT NULL,
  `vehicle_no` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '车牌号',
  `reason` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来访事由名称',
  `reason_type` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来访事由编码',
  `record_code` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录编码',
  `access_type` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '出入标识',
  `access_time` datetime DEFAULT NULL COMMENT '出入时间',
  `under_body_photo` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '车底照片',
  `body_photo` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '车身抓拍照',
  `plate_photo` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '车牌抓拍照',
  `is_auto` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '是否自动识别',
  `org_code` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登记地点编码',
  `device_code` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '抓拍设备编码',
  `device_type` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '抓拍设备类型',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='车底监测记录';