-- liquibase formatted sql

-- changeset qqq:1686103358461-1


-- 人员时序主表(状态表)
CREATE TABLE `att_staff_sequential` (
        `id` bigint(20) NOT NULL,
        `sequential_month` varchar(8) NOT NULL COMMENT '月份',
        `sequential_date` date DEFAULT NULL COMMENT '日期',
        `staff_id` bigint(20) DEFAULT NULL COMMENT '人员id',
        `staff_name` varchar(32) DEFAULT NULL COMMENT '人员名称',
        `day_of_month` int(11) DEFAULT NULL COMMENT '当月的第几天',
        `week` varchar(20) DEFAULT NULL COMMENT '星期',
        `status_list` text DEFAULT NULL COMMENT '状态集合json',
        `tenant` int(11) DEFAULT NULL,
        `create_by` varchar(32) DEFAULT NULL,
        `create_time` datetime DEFAULT NULL,
        `update_by` varchar(32) DEFAULT NULL,
        `update_time` datetime DEFAULT NULL,
        `deleted` bigint(20) DEFAULT '0',
        PRIMARY KEY (`id`) USING BTREE,
        KEY `att_staff_sequential_staff_id_IDX` (`staff_id`) USING BTREE,
        KEY `att_staff_sequential_scheduling_date_IDX` (`sequential_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='人员时序主表(状态表)';

-- 人员时序详情表
CREATE TABLE `att_staff_sequential_detail` (
   `id` bigint(20) NOT NULL,
   `sequential_month` varchar(8) NOT NULL COMMENT '月份',
   `staff_id` bigint(20) DEFAULT NULL COMMENT '人员id',
   `staff_name` varchar(32) DEFAULT NULL COMMENT '人员名称',
   `start_time` datetime DEFAULT NULL COMMENT '开始时间',
   `end_time` datetime DEFAULT NULL COMMENT '结束时间',
   `status` varchar(50) DEFAULT NULL COMMENT '状态',
   `business_key` bigint(20) DEFAULT NULL COMMENT '人员id',
   `tenant` int(11) DEFAULT NULL,
   `create_by` varchar(32) DEFAULT NULL,
   `create_time` datetime DEFAULT NULL,
   `update_by` varchar(32) DEFAULT NULL,
   `update_time` datetime DEFAULT NULL,
   `deleted` bigint(20) DEFAULT '0',
   PRIMARY KEY (`id`) USING BTREE,
   KEY `att_staff_sequential_detail_staff_id_IDX` (`staff_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='人员时序主详情表';

