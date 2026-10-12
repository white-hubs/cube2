-- liquibase formatted sql

-- changeset chx:ddl_1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'dbc_goods_category') AS column_exists;
CREATE TABLE `dbc_goods_category` (
                                      `id` bigint(20) unsigned NOT NULL COMMENT '主键',
                                      `market_id` int(11) NOT NULL COMMENT '市场编号',
                                      `oid` varchar(100) NOT NULL COMMENT '品类序号',
                                      `code` varchar(100) NOT NULL COMMENT '品类编号',
                                      `name` varchar(100) NOT NULL COMMENT '品类名称',
                                      `parent` varchar(500) DEFAULT NULL COMMENT '上级序号',
                                      `lev` varchar(100) DEFAULT NULL COMMENT '等级',
                                      `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
                                      `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
                                      `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='大白菜商品品类表';
