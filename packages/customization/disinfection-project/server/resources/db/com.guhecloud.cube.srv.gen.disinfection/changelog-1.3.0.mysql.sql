-- liquibase formatted sql


-- changeset ht:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_log') AS column_exists;
-- ----------------------------
-- cube_srv_gen_disinfection_project.member_log definition
CREATE TABLE `member_log` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `title` varchar(100) NOT NULL COMMENT '日志标题',
  `device_id` bigint(20) NOT NULL COMMENT '设备id',
  `detail` varchar(255) DEFAULT '0' COMMENT '日志描述',
  `type` varchar(20) NOT NULL DEFAULT '0' COMMENT '日志类型',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
  `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志记录表';




