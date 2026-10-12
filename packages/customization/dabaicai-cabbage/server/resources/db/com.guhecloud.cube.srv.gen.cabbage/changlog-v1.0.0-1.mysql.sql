-- liquibase formatted sql

-- changeset chx:ddl_1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'dbc_area') AS column_exists;
-- 大白菜表
CREATE TABLE `dbc_area` (
                            `id` bigint(20) NOT NULL,
                            `market_id` int(11) DEFAULT NULL COMMENT '市场id',
                            `area_code` varchar(100) NOT NULL COMMENT '所属区域序号',
                            `area_name` varchar(100) DEFAULT NULL COMMENT '所属区域名称',
                            `area_ids` varchar(200) DEFAULT NULL COMMENT '对应古河云区域Id(全路径区域 用逗号隔开)',
                            `tenant` int(11) DEFAULT NULL COMMENT '租户id',
                            `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜区域(映射)表';

-- changeset chx:ddl_2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'dbc_dept') AS column_exists;
CREATE TABLE `dbc_dept` (
                            `id` bigint(20) NOT NULL,
                            `market_id` int(11) DEFAULT NULL COMMENT '市场id',
                            `department_code` varchar(100) NOT NULL COMMENT '所属部门序号',
                            `department_name` varchar(100) DEFAULT NULL COMMENT '所属部门名称',
                            `organization_ids` varchar(200) DEFAULT NULL COMMENT '对应古河云部门Id(全路径部门 用逗号隔开)',
                            `tenant` int(11) DEFAULT NULL COMMENT '租户id',
                            `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜部门(映射)表';


-- changeset chx:ddl_3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'dbc_market') AS column_exists;
CREATE TABLE `dbc_market` (
                              `id` bigint(20) NOT NULL,
                              `market_id` int(11) NOT NULL COMMENT '市场编号',
                              `market_name` varchar(100) DEFAULT NULL COMMENT '市场名称',
                              `tenant_market_id` int(11) DEFAULT NULL COMMENT '市场id对应的租户id',
                              `tenant` int(11) DEFAULT '0' COMMENT '租户id',
                              `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
                              `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                              `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜市场表';


-- changeset chx:ddl_4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'dbc_store_type') AS column_exists;
CREATE TABLE `dbc_store_type` (
                                  `id` bigint(20) NOT NULL,
                                  `market_id` int(11) DEFAULT NULL COMMENT '市场id',
                                  `store_type_code` varchar(100) NOT NULL COMMENT '所属挡位类型序号',
                                  `store_type_name` varchar(100) DEFAULT NULL COMMENT '所属挡位类型名称',
                                  `stall_category_id` bigint(20) DEFAULT NULL COMMENT '对应在古河云新增的挡位类型Id',
                                  `tenant` int(11) DEFAULT NULL COMMENT '租户id',
                                  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
                                  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                  `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜部门(映射)表';

-- changeset chx:ddl_5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'dbc_store') AS column_exists;
CREATE TABLE `dbc_store` (
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
                             `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
                             `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                             `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳(0表示未删除)',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜挡位表';

-- changeset chx:ddl_6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'dbc_vehicle') AS column_exists;
CREATE TABLE `dbc_vehicle` (
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
                               `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜车辆表';



