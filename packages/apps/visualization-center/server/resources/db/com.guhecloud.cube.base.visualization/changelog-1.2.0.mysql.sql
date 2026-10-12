-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'project_manage' AND column_name='title') AS column_exists;
ALTER TABLE `project_manage` ADD COLUMN `title`  varchar(255) DEFAULT NULL COMMENT '平台标题';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'project_manage' AND column_name='entry') AS column_exists;
ALTER TABLE `project_manage` ADD COLUMN `entry`  varchar(100) DEFAULT NULL COMMENT '渲染模型';

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'project_manage' AND column_name='icon_scale') AS column_exists;
ALTER TABLE `project_manage` ADD COLUMN `icon_scale`  decimal(10,2) DEFAULT NULL COMMENT '图片尺寸';

-- changeset zengyadong:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'project_manage' AND column_name='is_filter_area') AS column_exists;
ALTER TABLE `project_manage` ADD COLUMN `is_filter_area`  varchar(100) DEFAULT NULL COMMENT '区域设备过滤';

-- changeset zengyadong:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'project_manage' AND column_name='device_style') AS column_exists;
ALTER TABLE `project_manage` ADD COLUMN `device_style`  varchar(100) DEFAULT NULL COMMENT '设备详情风格';

-- changeset zengyadong:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'project_manage' AND column_name='nav_type') AS column_exists;
ALTER TABLE `project_manage` ADD COLUMN `nav_type`  varchar(100) DEFAULT NULL COMMENT '菜单风格';

-- changeset zengyadong:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'project_manage' AND column_name='card_type') AS column_exists;
ALTER TABLE `project_manage` ADD COLUMN `card_type`  varchar(100) DEFAULT NULL COMMENT '卡片风格';

-- changeset zengyadong:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'model_scene') AS table_exists;
CREATE TABLE `model_scene` (
  `id` bigint(20) NOT NULL,
  `project_id` bigint(20) DEFAULT NULL COMMENT '孪生项目id',
  `name` varchar(100) DEFAULT NULL COMMENT '模型名称',
  `model_type` varchar(100) DEFAULT NULL COMMENT '模型类型',
  `model_url` varchar(255) DEFAULT NULL COMMENT '模型地址',
  `area_id` varchar(255) DEFAULT NULL COMMENT '模型区域',
  `scene_type_id` bigint(20) DEFAULT NULL COMMENT '业务菜单',
  `model_file` bigint(20) DEFAULT NULL COMMENT '模型文件',
  `model_structure` varchar(255) DEFAULT NULL COMMENT '模型结构',
  `scene_url` varchar(100) DEFAULT NULL COMMENT '组态链接',
  `scene_img` bigint(20) DEFAULT NULL COMMENT '组态图片',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='模型场景配置';

-- changeset zengyadong:ddl-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'model_view_config') AS table_exists;
CREATE TABLE `model_view_config` (
  `id` bigint(20) NOT NULL,
  `label` varchar(255) DEFAULT NULL COMMENT '视角名称',
  `view_value` mediumtext COMMENT '视角参数',
  `area_id` bigint(20) DEFAULT NULL COMMENT '区域',
  `area_name` varchar(500) DEFAULT NULL COMMENT '区域名称',
  `build_tag_location` varchar(500) DEFAULT NULL COMMENT '中心点位置',
  `is_default` varchar(255) DEFAULT NULL COMMENT '是否默认',
  `hide_actors` varchar(500) DEFAULT NULL COMMENT '隐藏区域',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `project_id` bigint(20) DEFAULT NULL COMMENT '孪生项目id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='视角配置';

-- changeset zengyadong:ddl-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'roaming_route') AS table_exists;
CREATE TABLE `roaming_route` (
  `id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL COMMENT 'project_id',
  `name` varchar(255) DEFAULT NULL COMMENT '路线名称',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`,`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='漫游路线';

-- changeset zengyadong:ddl-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'roaming_route_point') AS table_exists;
CREATE TABLE `roaming_route_point` (
  `id` bigint(20) NOT NULL,
  `point_name` varchar(255) DEFAULT NULL COMMENT '点位名称',
  `speed` decimal(50,0) DEFAULT NULL,
  `perspective` mediumtext COMMENT '视角参数',
  `sort` bigint(20) DEFAULT NULL COMMENT '序号',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `route_id` bigint(20) DEFAULT NULL COMMENT '漫游线路id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='漫游点位';