-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'third_user_ref') AS column_exists;
CREATE TABLE `third_user_ref` (
  `id` bigint(20) unsigned NOT NULL,
  `type` varchar(50) DEFAULT NULL COMMENT '三方账号类型(微信等其它三方)',
  `config_id` bigint(20) unsigned DEFAULT NULL COMMENT '三方账号配置id',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '系统用户id',
  `ref_id` bigint(20) unsigned DEFAULT NULL COMMENT '关联的第三方账号id',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_type_config_id_user_id_deleted` (`type`,`config_id`,`user_id`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='三方账号关联信息';

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'user_wechat' AND INDEX_NAME = 'idx_user_wechat_user_id' ) AS column_exists;
ALTER TABLE `user_wechat` DROP INDEX `idx_user_wechat_user_id`;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'user_wechat' AND INDEX_NAME = 'idx_user_wechat_user_id_open_id' ) AS column_exists;
ALTER TABLE `user_wechat` ADD UNIQUE INDEX `idx_user_wechat_user_id_open_id`(`user_id`, `open_id`) USING BTREE;