-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `gen_supplier` (
                                `id` bigint(20) unsigned NOT NULL COMMENT '主键',
                                `name` varchar(100) NOT NULL COMMENT '商家名称',
                                `code` varchar(100) NOT NULL COMMENT '商家编号',
                                `admin_user_id` bigint(20) DEFAULT NULL COMMENT '管理员用户主键',
                                `admin_user_name` varchar(100) DEFAULT NULL COMMENT '管理员用户名称',
                                `admin_phone` varchar(100) DEFAULT NULL COMMENT '管理员手机号',
                                `liaison` varchar(100) DEFAULT NULL COMMENT '联系人',
                                `phone` varchar(100) DEFAULT NULL COMMENT '联系人手机号',
                                `area` varchar(100) DEFAULT NULL COMMENT '所在地区',
                                `address` varchar(100) DEFAULT NULL COMMENT '详细地址',
                                `remark` varchar(200) DEFAULT NULL COMMENT '备注说明',
                                `category` varchar(1000) DEFAULT NULL COMMENT '主营品种',
                                `risk_level` varchar(100) DEFAULT NULL COMMENT '风险级别',
                                `risk_amount` int(11) DEFAULT '0' COMMENT '风险次数',
                                `supplier_tenant` int(11) DEFAULT NULL COMMENT '批发商租户编号',
                                `tenant` int(11) DEFAULT NULL COMMENT '租户编号',
                                `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '是否删除',
                                `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                `shift_master` varchar(50) DEFAULT NULL COMMENT '档主',
                                `shift_master_phone` varchar(50) DEFAULT NULL COMMENT '档主联系电话',
                                `shift_master_card` varchar(50) DEFAULT NULL COMMENT '档主身份证',
                                `other_contacts` varchar(50) DEFAULT NULL COMMENT '其他联系人',
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE KEY `idx_supplier_tenant_tenant_deleted` (`supplier_tenant`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='批发商表';

