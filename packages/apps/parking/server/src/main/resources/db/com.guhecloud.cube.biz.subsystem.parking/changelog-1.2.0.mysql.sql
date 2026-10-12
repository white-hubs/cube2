-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'parking_info' AND column_name='source') AS column_exists;
ALTER TABLE `parking_info` ADD COLUMN `source` VARCHAR(50) NULL DEFAULT NULL COMMENT '车场来源 THIRD: 三方，SELF_BUILT：自建' AFTER `parent_code`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'parking_info' AND column_name='area_id') AS column_exists;
ALTER TABLE `parking_info` ADD COLUMN `area_id` varchar(500) NULL DEFAULT NULL COMMENT '所在区域' AFTER `source`;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'parking_info' AND column_name='address') AS column_exists;
ALTER TABLE `parking_info` ADD COLUMN `address` varchar(300) NULL DEFAULT NULL COMMENT '车场地址' AFTER `area_id`;

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'parking_info' AND column_name='remarks') AS column_exists;
ALTER TABLE `parking_info` ADD COLUMN `remarks` varchar(500) NULL DEFAULT NULL COMMENT '车场备注' AFTER `address`;

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'parking_info_device') AS table_exists;
CREATE TABLE `parking_info_device` (
  `id` bigint NOT NULL,
  `parking_info_id` bigint NOT NULL COMMENT '车场id',
  `device_id` bigint NOT NULL COMMENT '设备id',
  `current_car_number` int DEFAULT '0' COMMENT '当前车数量',
  `tenant` int DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车场设备关联表';

-- changeset lanchun:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'parking_info_visual') AS table_exists;
CREATE TABLE `parking_info_visual` (
  `id` bigint NOT NULL,
  `center_address` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '中心位置',
  `visual_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '视角参数',
  `hidden_building` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '隐藏楼栋',
  `tenant` int DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车场视角信息';

-- changeset lanchun:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'parking_info_dock_config') AS table_exists;
CREATE TABLE `parking_info_dock_config` (
  `id` bigint NOT NULL,
  `provider_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '厂家编码',
  `config` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车场对接配置',
  `tenant` int DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车场对接配置';

-- changeset lanchun:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'parking_info' AND column_name='model_area') AS column_exists;
ALTER TABLE `parking_info` ADD COLUMN `model_area` varchar(100) NULL DEFAULT NULL COMMENT '模型区域' AFTER `address`;
