-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task_target_item') AS column_exists;
CREATE TABLE `inspect_task_target_item` (
                                            `id` bigint(20) unsigned NOT NULL,
                                            `task_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检任务ID',
                                            `target_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检对象ID',
                                            `standard_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检标准ID',
                                            `standard_name` varchar(100) DEFAULT NULL COMMENT '巡检对象名称',
                                            `standard_code` varchar(32) DEFAULT NULL COMMENT '标准编码',
                                            `item_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检项ID',
                                            `item_name` varchar(100) DEFAULT NULL COMMENT '巡检项名称',
                                            `item_alias` varchar(100) DEFAULT NULL COMMENT '巡检项别名',
                                            `result_item_value` varchar(255) DEFAULT NULL COMMENT '巡检结果默认值',
                                            `remark_result` varchar(200) DEFAULT NULL COMMENT '要填写描述的结果(多个用逗号隔开)',
                                            `image_result` varchar(200) DEFAULT NULL COMMENT '要拍照的结果(多个用逗号隔开)',
                                            `item_tag` varchar(100) DEFAULT NULL COMMENT '巡检项的tag',
                                            `result_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检结果Id',
                                            `result_text` text COMMENT '巡检结果',
                                            `tenant` int(11) NOT NULL COMMENT '租户ID',
                                            `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_by` varchar(20) DEFAULT NULL COMMENT '修改人',
                                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                            `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '删除标志',
                                            KEY `inspect_task_target_item` (`task_id`,`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务对象的巡检内容(标准,项,结果)';