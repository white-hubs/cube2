-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'point_type') AS column_exists;
CREATE TABLE `point_type`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件地址',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点位分类' ROW_FORMAT = Dynamic;

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'point_manage' AND column_name = 'type_id' ) AS column_exists;
ALTER TABLE `point_manage` ADD COLUMN `type_id` bigint(20) NULL DEFAULT NULL COMMENT '类型主键' AFTER `name`;

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'point_manage' AND column_name = 'area_sequence' ) AS column_exists;
ALTER TABLE `point_manage` ADD COLUMN `area_sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL COMMENT '区域序列号' AFTER `area_id`;

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'guide_area_manage' AND column_name = 'level' ) AS column_exists;
ALTER TABLE `guide_area_manage` MODIFY COLUMN `level` int(10) NULL DEFAULT NULL COMMENT '层级' AFTER `zoom`;

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'guide_area_manage' AND column_name = 'parent_id' ) AS column_exists;
ALTER TABLE `guide_area_manage` ADD COLUMN `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父类主键' AFTER `level`;

-- changeset lanchun:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'guide_area_manage' AND column_name = 'sequence' ) AS column_exists;
ALTER TABLE `guide_area_manage` ADD COLUMN `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL COMMENT '序列号' AFTER `parent_id`;

-- changeset lanchun:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'guide_area_manage' AND column_name = 'sort' ) AS column_exists;
ALTER TABLE `guide_area_manage` ADD COLUMN `sort` int(10) NULL DEFAULT NULL COMMENT '序号' AFTER `sequence`;

-- changeset lanchun:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'layer_manage' AND column_name = 'area_id' ) AS column_exists;
ALTER TABLE `layer_manage` ADD COLUMN `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域主键' AFTER `state`;

-- changeset lanchun:ddl-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'layer_manage' AND column_name = 'area_sequence' ) AS column_exists;
ALTER TABLE `layer_manage` ADD COLUMN `area_sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL COMMENT '区域序列号' AFTER `area_id`;

-- changeset lanchun:ddl-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'point_manage' AND column_name = 'file_path' ) AS column_exists;
ALTER TABLE `point_manage` ADD COLUMN `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '文件地址' AFTER `state`;

-- changeset lanchun:ddl-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'guide_device_manage') AS column_exists;
CREATE TABLE `guide_device_manage` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `logo` varchar(500) DEFAULT NULL COMMENT '图标',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `coordinate` varchar(200) DEFAULT NULL COMMENT '点位坐标',
  `zoom` varchar(10) DEFAULT NULL COMMENT '层级',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导览设备配置';
