-- changeset fl:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_md_product_prop' AND column_name='default_value') AS column_exists;
ALTER TABLE `iot_md_product_prop`
    ADD COLUMN `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性默认值';

-- changeset fl:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_command_info') AS table_exists;
CREATE TABLE IF NOT EXISTS `iot_command_info` (
      `id` bigint(20) NOT NULL,
      `cmd_seq` varchar(255) DEFAULT NULL,
      `down_cmd` varchar(255) DEFAULT NULL,
      `translated_cmd_content` text,
      `cmd_flag` varchar(255) DEFAULT NULL,
      `start_time` datetime DEFAULT NULL,
      `end_time` datetime DEFAULT NULL,
      `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
      `deleted` bigint(20) NOT NULL COMMENT '是否删除',
      `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '更新用户',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`) USING BTREE,
      UNIQUE KEY `command_seq_index` (`cmd_seq`) USING BTREE COMMENT '命令id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下行命令表';

-- changeset fl:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_command_log') AS table_exists;
CREATE TABLE IF NOT EXISTS `iot_command_log` (
     `id` bigint(20) NOT NULL,
     `instance_id` varchar(255) DEFAULT NULL,
     `cmd_seq` varchar(255) DEFAULT NULL,
     `log_content` longtext,
     `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
     `deleted` bigint(20) NOT NULL COMMENT '是否删除',
     `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '更新用户',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE,
     KEY `command_seq_index` (`cmd_seq`) USING BTREE,
     KEY `instance_id` (`instance_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='命令日志表';

-- changeset fl:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_command_record') AS table_exists;
CREATE TABLE IF NOT EXISTS `iot_command_record` (
    `id` bigint(20) NOT NULL,
    `instance_id` varchar(255) DEFAULT NULL,
    `cmd_seq` varchar(255) DEFAULT NULL,
    `raw_cmd_content` mediumtext,
    `translated_cmd_content` text,
    `result_tips` varchar(255) DEFAULT NULL,
    `single_cmd_status` varchar(255) DEFAULT NULL,
    `start_time` datetime DEFAULT NULL,
    `end_time` datetime DEFAULT NULL,
    `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
    `deleted` bigint(20) NOT NULL COMMENT '是否删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '更新用户',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `instance_id` (`instance_id`) USING BTREE,
    KEY `command_seq_index` (`cmd_seq`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='命令记录详情表';