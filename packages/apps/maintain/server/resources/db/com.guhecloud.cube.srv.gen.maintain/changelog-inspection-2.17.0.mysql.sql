-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'inspect_scene_point_device') AS table_exists;
CREATE TABLE IF NOT EXISTS `inspect_scene_point_device` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键',
  `scene_point_id` bigint(20) unsigned NOT NULL COMMENT '现场点ID',
  `device_id` bigint(20) unsigned NOT NULL COMMENT '设备ID',
  `tenant` int(11) NOT NULL COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`),
  KEY `idx_scene_point_id` (`scene_point_id`),
  KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='现场点关联设备';
