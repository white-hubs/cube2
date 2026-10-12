-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'safety_inspect_record') AS column_exists;
CREATE TABLE IF NOT EXISTS `safety_inspect_record` (
    `id` bigint(20) unsigned NOT NULL,
    `tenant` int(11) DEFAULT NULL COMMENT '租户ID',
    `task_id` bigint(20) unsigned DEFAULT NULL COMMENT '任务ID（临时巡检没有任务ID）',
    `task_name` varchar(100) DEFAULT NULL COMMENT '任务名称',
    `plan_id` bigint(20) unsigned DEFAULT NULL COMMENT '计划ID',
    `plan_name` varchar(100) DEFAULT NULL COMMENT '计划名',
    `target_category` varchar(32) DEFAULT NULL COMMENT '目标对象类型',
    `target_id` bigint(20) unsigned DEFAULT NULL COMMENT '目标对象ID',
    `target_name` varchar(255) DEFAULT NULL COMMENT '目标对象名',
    `status` varchar(32) DEFAULT NULL COMMENT '状态',
    `time` datetime DEFAULT NULL COMMENT '巡检时间',
    `organization_id` bigint(20) unsigned DEFAULT NULL COMMENT '组织机构ID',
    `organization_name` varchar(100) DEFAULT NULL COMMENT '组织机构名',
    `position_id` bigint(20) unsigned DEFAULT NULL COMMENT '岗位ID',
    `position_name` varchar(100) DEFAULT NULL COMMENT '岗位名',
    `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
    `username` varchar(100) DEFAULT NULL COMMENT '用户名',
    `nickname` varchar(100) DEFAULT NULL COMMENT '用户姓名',
    `result` varchar(255) DEFAULT NULL COMMENT '巡检结果',
    `remark` varchar(128) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(20) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '删除标志',
    `area_id` varchar(256) DEFAULT NULL COMMENT '设备区域id',
    `area_name` varchar(255) DEFAULT NULL COMMENT '设备区域名称',
    `market_tenant` int(11) DEFAULT NULL COMMENT '市场租户id',
    `market_name` varchar(125) DEFAULT NULL COMMENT '市场租户名称',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `taskTargetObj` (`task_id`,`target_category`,`target_id`,`tenant`) USING BTREE,
    KEY `task_id` (`task_id`) USING BTREE,
    KEY `target_id` (`target_id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='巡检记录';


-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'safety_inspect_record_item') AS column_exists;
CREATE TABLE IF NOT EXISTS `safety_inspect_record_item` (
    `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
    `tenant` int(11) DEFAULT NULL COMMENT '租户ID',
    `record_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检记录ID',
    `temp_add_flag` bit(1) DEFAULT NULL COMMENT '是否临时添加标准',
    `standard_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检标准ID',
    `standard_name` varchar(256) DEFAULT NULL COMMENT '巡检标准名称',
    `item_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检项ID',
    `item_name` varchar(256) DEFAULT NULL COMMENT '巡检项名称',
    `item_tag` varchar(200) DEFAULT NULL COMMENT '巡检项的tag',
    `item_image_result` varchar(200) DEFAULT NULL COMMENT '要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)',
    `item_result_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检结果Id',
    `item_result_value` varchar(16) DEFAULT NULL COMMENT '巡检项结果值',
    `item_result_name` varchar(16) DEFAULT NULL COMMENT '巡检项结果名称',
    `error_value_flag` bit(1) DEFAULT NULL COMMENT '0 不是  1 是',
    `remark_result` varchar(200) DEFAULT NULL COMMENT '要填写描述的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)',
    `error_desc` varchar(128) DEFAULT NULL COMMENT '异常说明',
    `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(20) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '删除标志',
    `market_tenant` int(11) DEFAULT NULL COMMENT '市场租户id',
    `market_name` varchar(125) DEFAULT NULL COMMENT '市场租户名称',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `record_id` (`record_id`) USING BTREE,
    KEY `item_id` (`item_id`) USING BTREE,
    KEY `item_result_id` (`item_result_id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='巡检记录-结果项';


-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'property_filing' AND column_name = 'address') AS column_exists;
ALTER TABLE `property_filing` ADD `address` varchar(255) NULL COMMENT '详细地址';
