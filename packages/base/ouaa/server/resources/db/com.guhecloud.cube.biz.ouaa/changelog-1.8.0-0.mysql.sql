-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'application_extend') AS column_exists;
CREATE TABLE `application_extend` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键',
  `description` text CHARACTER SET utf8mb4 COMMENT '应用介绍',
  `feature_desc` text CHARACTER SET utf8mb4 COMMENT '功能描述',
  `help_info` text CHARACTER SET utf8mb4 COMMENT '使用帮助',
  `purchase_info` text CHARACTER SET utf8mb4 COMMENT '购买说明',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `title` varchar(500) DEFAULT NULL COMMENT '标题',
  `keywords` varchar(500) DEFAULT NULL COMMENT '关键字',
  `app_desc` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='应用信息扩展表';


-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'application_iteration_content') AS column_exists;
CREATE TABLE `application_iteration_content` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `iteration_id` bigint(20) NOT NULL COMMENT '迭代记录id',
  `category` varchar(50) DEFAULT NULL COMMENT '迭代类别：0-修复,1-优化,2-新增,3-移除',
  `content` text COMMENT '迭代内容',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用迭代内容表';

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'application_iteration_record') AS column_exists;
CREATE TABLE `application_iteration_record` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `application_id` bigint(20) NOT NULL COMMENT '应用ID',
  `version` varchar(50) NOT NULL COMMENT '版本号',
  `last_version` varchar(50) DEFAULT NULL COMMENT '上次版本',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用迭代记录表';

-- changeset zengyadong:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'application' AND column_name = 'version' ) AS column_exists;
ALTER TABLE `application` ADD COLUMN `version` varchar(50) DEFAULT NULL COMMENT '版本';

-- changeset zengyadong:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'application' AND column_name = 'view_count' ) AS column_exists;
ALTER TABLE `application` ADD COLUMN `view_count` bigint(20) DEFAULT NULL COMMENT '浏览次数';

-- changeset zengyadong:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'application' AND column_name = 'depend_app_Ids' ) AS column_exists;
ALTER TABLE `application` ADD COLUMN `depend_app_ids` varchar(500) DEFAULT NULL COMMENT '依赖应用';

-- changeset zengyadong:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'application' AND column_name = 'public_access' ) AS column_exists;
ALTER TABLE `application` ADD COLUMN `public_access` varchar(20) DEFAULT NULL COMMENT '是否可公开访问';

-- changeset zengyadong:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'role' AND column_name = 'product_id' ) AS column_exists;
ALTER TABLE `role` MODIFY COLUMN `product_id` BIGINT (20) DEFAULT NULL;

-- changeset zengyadong:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'tenant_application' AND column_name = 'product_id' ) AS column_exists;
ALTER TABLE `tenant_application` MODIFY COLUMN `product_id` BIGINT (20) DEFAULT NULL;