-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `gen_stall` (
                             `id` bigint(20) unsigned NOT NULL COMMENT '主键',
                             `code` varchar(100) DEFAULT NULL COMMENT '档位编号',
                             `name` varchar(100) DEFAULT NULL COMMENT '档位名称',
                             `type` varchar(20) DEFAULT NULL COMMENT '档位类型',
                             `area` varchar(100) DEFAULT NULL COMMENT '档位区域',
                             `location` varchar(100) DEFAULT NULL COMMENT '具体位置',
                             `built_up_area` varchar(20) DEFAULT NULL COMMENT '建筑面积',
                             `practical_area` varchar(20) DEFAULT NULL COMMENT '实用面积',
                             `dept_id` varchar(100) DEFAULT NULL COMMENT '部门主键',
                             `dept_name` varchar(20) DEFAULT NULL COMMENT '部门名称',
                             `lease_type` tinyint(1) DEFAULT '0' COMMENT '出租类型',
                             `leased` tinyint(1) DEFAULT NULL COMMENT '是否出租',
                             `bim_bind` tinyint(1) DEFAULT NULL COMMENT '模型绑定',
                             `remark` varchar(200) DEFAULT NULL COMMENT '备注说明',
                             `enabled` tinyint(3) unsigned DEFAULT '0' COMMENT '是否启用',
                             `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
                             `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否已删除',
                             `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `idx_stall_tenant_deleted_code` (`tenant`,`deleted`,`code`) USING BTREE,
                             UNIQUE KEY `idx_stall_tenant_deleted_name` (`tenant`,`deleted`,`name`) USING BTREE,
                             KEY `booth_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='档位信息表';

