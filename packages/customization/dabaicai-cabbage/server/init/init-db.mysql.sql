CREATE DATABASE IF NOT EXISTS `cube_srv_gen_cabbage` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube_srv_gen_cabbage`;

-- cube_srv_gen_cabbage.databasechangelog definition

CREATE TABLE IF NOT EXISTS `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT COUNT(1) IndexIsThere
FROM INFORMATION_SCHEMA.STATISTICS
WHERE table_schema=DATABASE()
AND   table_name='databasechangelog'
AND   index_name='unique_id_file_name';

SET @IndexIsThere =
(SELECT COUNT(1) IndexIsThere
 FROM INFORMATION_SCHEMA.STATISTICS
 WHERE table_schema=DATABASE()
 AND   table_name='databasechangelog'
 AND   index_name='unique_id_file_name');

SET @sqlstmt =
IF (@IndexIsThere = 0,
    'ALTER TABLE `cube_srv_gen_cabbage`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- cube_srv_gen_cabbage.databasechangeloglock definition

CREATE TABLE IF NOT EXISTS `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT IGNORE INTO cube_srv_gen_cabbage.databasechangeloglock (ID, LOCKED, LOCKGRANTED, LOCKEDBY) VALUES(1, 0, NULL, NULL);

-- cube_srv_gen_cabbage.dbc_area definition

CREATE TABLE IF NOT EXISTS `dbc_area` (
  `id` bigint(20) NOT NULL,
  `market_id` int(11) DEFAULT NULL COMMENT '市场id',
  `area_code` varchar(100) NOT NULL COMMENT '所属区域序号',
  `area_name` varchar(100) DEFAULT NULL COMMENT '所属区域名称',
  `area_ids` varchar(200) DEFAULT NULL COMMENT '对应古河云区域Id(全路径区域 用逗号隔开)',
  `tenant` int(11) DEFAULT NULL COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜区域(映射)表';


-- cube_srv_gen_cabbage.dbc_dept definition

CREATE TABLE IF NOT EXISTS `dbc_dept` (
  `id` bigint(20) NOT NULL,
  `market_id` int(11) DEFAULT NULL COMMENT '市场id',
  `department_code` varchar(100) NOT NULL COMMENT '所属部门序号',
  `department_name` varchar(100) DEFAULT NULL COMMENT '所属部门名称',
  `organization_ids` varchar(200) DEFAULT NULL COMMENT '对应古河云部门Id(全路径部门 用逗号隔开)',
  `tenant` int(11) DEFAULT NULL COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜部门(映射)表';


-- cube_srv_gen_cabbage.dbc_market definition

CREATE TABLE IF NOT EXISTS `dbc_market` (
  `id` bigint(20) NOT NULL,
  `market_id` int(11) NOT NULL COMMENT '市场编号',
  `market_name` varchar(100) DEFAULT NULL COMMENT '市场名称',
  `tenant_market_id` int(11) DEFAULT NULL COMMENT '市场id对应的租户id',
  `tenant` int(11) DEFAULT '0' COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜市场表';


-- cube_srv_gen_cabbage.dbc_store definition

CREATE TABLE IF NOT EXISTS `dbc_store` (
  `id` bigint(20) NOT NULL,
  `market_id` int(11) NOT NULL COMMENT '市场编号',
  `market_name` varchar(100) DEFAULT NULL COMMENT '市场名称',
  `storefront_code` varchar(100) DEFAULT NULL COMMENT '档位编号',
  `storefront_name` varchar(100) DEFAULT NULL COMMENT '档位名称',
  `alias` varchar(100) DEFAULT NULL COMMENT '档位别名',
  `area_code` varchar(100) DEFAULT NULL COMMENT '所属区域序号',
  `area_name` varchar(100) DEFAULT NULL COMMENT '所属区域名称',
  `storefront_type_code` varchar(100) DEFAULT NULL COMMENT '档位类型序号',
  `storefront_type_name` varchar(100) DEFAULT NULL COMMENT '档位类型名称',
  `department_code` varchar(100) DEFAULT NULL COMMENT '所属部门序号',
  `department_name` varchar(100) DEFAULT NULL COMMENT '所属部门名称',
  `customer_name` varchar(100) DEFAULT NULL COMMENT '档主名称',
  `customer_mobile` varchar(100) DEFAULT NULL COMMENT '档主联系手机号',
  `organization_ids` varchar(100) DEFAULT NULL COMMENT '对应古河云部门Id(全路径部门 用逗号隔开)',
  `area_ids` varchar(100) DEFAULT NULL COMMENT '对应古河云区域Id(全路径区域 用逗号隔开)',
  `stall_category_id` bigint(20) DEFAULT NULL COMMENT '对应古河云挡位类型id',
  `tenant` int(11) DEFAULT NULL COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜挡位表';


-- cube_srv_gen_cabbage.dbc_store_type definition

CREATE TABLE IF NOT EXISTS `dbc_store_type` (
  `id` bigint(20) NOT NULL,
  `market_id` int(11) DEFAULT NULL COMMENT '市场id',
  `store_type_code` varchar(100) NOT NULL COMMENT '所属挡位类型序号',
  `store_type_name` varchar(100) DEFAULT NULL COMMENT '所属挡位类型名称',
  `stall_category_id` bigint(20) DEFAULT NULL COMMENT '对应在古河云新增的挡位类型Id',
  `tenant` int(11) DEFAULT NULL COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜部门(映射)表';


-- cube_srv_gen_cabbage.dbc_vehicle definition

CREATE TABLE IF NOT EXISTS `dbc_vehicle` (
  `id` bigint(20) NOT NULL,
  `market_id` int(11) DEFAULT NULL COMMENT '市场id',
  `vehicle_number` varchar(50) DEFAULT NULL COMMENT '车牌号',
  `vehicle_type_code` varchar(100) DEFAULT NULL COMMENT '车辆类型编号',
  `vehicle_type_name` varchar(50) DEFAULT NULL COMMENT '车辆类型名称',
  `customer_name` varchar(100) DEFAULT NULL COMMENT '车主姓名',
  `customer_mobile` varchar(20) DEFAULT NULL COMMENT '车主电话',
  `market_name` varchar(100) DEFAULT NULL COMMENT '市场名称',
  `vehicle_Id` bigint(20) DEFAULT NULL COMMENT '古河云的车辆id',
  `tenant` int(11) DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜车辆表';
