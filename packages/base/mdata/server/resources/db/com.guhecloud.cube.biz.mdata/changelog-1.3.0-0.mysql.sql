-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'stall_expand') AS column_exists;
CREATE TABLE `stall_expand` (
  `id` bigint(20) unsigned NOT NULL,
  `stall_id` bigint(20) unsigned DEFAULT NULL COMMENT '档位主键',
  `lnt_and_lat` varchar(150) DEFAULT NULL COMMENT '经纬度信息',
  `location_des` varchar(500) DEFAULT NULL COMMENT '位置描述信息',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_stall_id_tenant_deleted` (`stall_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='档位扩展信息';
