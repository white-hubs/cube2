-- liquibase formatted sql


-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_group_device_binding_log') AS column_exists;
CREATE TABLE IF NOT EXISTS `member_group_device_binding_log` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `group_id` bigint NOT NULL COMMENT '分组ID（关联 member_group.id）',
  `device_id` bigint NOT NULL COMMENT '设备ID（关联设备表）',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
  `create_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户分组与设备关联表';