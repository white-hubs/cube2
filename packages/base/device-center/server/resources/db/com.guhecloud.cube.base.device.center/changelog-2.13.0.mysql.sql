-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_info' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_command_info` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_info' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_command_info` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_info' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_command_info` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_info' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_command_info` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_log' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_command_log` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_log' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_command_log` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_log' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_command_log` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_log' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_command_log` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_command_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_command_record` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_command_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_command_record` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_unit' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_sys_unit` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-14
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_unit' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_sys_unit` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-15
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_unit' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_sys_unit` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-16
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_unit' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_sys_unit` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-17
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_sys_dict` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-18
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_sys_dict` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-19
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_sys_dict` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-20
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_sys_dict` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-21
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict_item' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_sys_dict_item` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-22
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict_item' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_sys_dict_item` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-23
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict_item' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_sys_dict_item` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-24
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict_item' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_sys_dict_item` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-25
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict_item_ref' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_sys_dict_item_ref` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-26
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict_item_ref' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_sys_dict_item_ref` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-27
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict_item_ref' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_sys_dict_item_ref` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-28
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_sys_dict_item_ref' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_sys_dict_item_ref` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-29
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_dev_device' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_dev_device` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-30
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_dev_device' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_dev_device` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-31
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_dev_device' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_dev_device` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-32
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_dev_device' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_dev_device` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-33
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_condition_meta' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_link_condition_meta` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-34
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_condition_meta' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_link_condition_meta` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-35
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_condition_meta' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_link_condition_meta` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-36
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_condition_meta' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_link_condition_meta` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-37
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_config' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_link_config` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-38
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_config' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_link_config` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-39
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_config' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_link_config` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-40
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_config' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_link_config` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-41
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_trigger_meta' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_link_trigger_meta` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-42
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_trigger_meta' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_link_trigger_meta` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-43
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_trigger_meta' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_link_trigger_meta` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-44
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_link_trigger_meta' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_link_trigger_meta` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-45
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_md_product` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-46
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_md_product` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-47
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_md_product` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-48
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_md_product` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-49
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_event' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_md_product_event` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-50
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_event' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_md_product_event` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-51
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_event' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_md_product_event` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-52
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_event' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_md_product_event` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-53
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_prop' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_md_product_prop` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-54
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_prop' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_md_product_prop` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-55
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_prop' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_md_product_prop` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-56
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_prop' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_md_product_prop` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-57
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_event_meta' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_md_product_event_meta` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-58
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_event_meta' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_md_product_event_meta` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-59
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_event_meta' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_md_product_event_meta` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-60
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_event_meta' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_md_product_event_meta` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-61
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_device` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-62
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_device` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-63
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_device` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-64
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_device` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-65
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device_expand' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_device_expand` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-66
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device_expand' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_device_expand` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-67
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device_expand' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_device_expand` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-68
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device_expand' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_device_expand` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-69
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device_ref' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_device_ref` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-70
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device_ref' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_device_ref` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-71
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device_ref' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_device_ref` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-72
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_device_ref' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_device_ref` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-73
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_group' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_group` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-74
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_group' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_group` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-75
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_group' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_group` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-76
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_group' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_group` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-77
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_group_ref' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_group_ref` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-78
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_group_ref' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_group_ref` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-79
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_group_ref' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_group_ref` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-80
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_group_ref' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_group_ref` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-81
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_tag` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-82
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_tag` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-83
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_tag` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-84
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_tag` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-85
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_ref' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_tag_ref` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-86
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_ref' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_tag_ref` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-87
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_ref' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_tag_ref` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-88
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_ref' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_tag_ref` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-89
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_rule' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_tag_rule` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-90
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_rule' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_tag_rule` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-91
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_rule' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_tag_rule` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-92
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_rule' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_tag_rule` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-93
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_type' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_biz_tag_type` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-94
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_type' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_biz_tag_type` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-95
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_type' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_biz_tag_type` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-96
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_biz_tag_type' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_biz_tag_type` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-97
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_attr_cmd' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_ext_device_attr_cmd` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-98
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_attr_cmd' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_ext_device_attr_cmd` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-99
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_attr_cmd' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_ext_device_attr_cmd` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-100
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_attr_cmd' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_ext_device_attr_cmd` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-101
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_attr_cmd_step' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_ext_device_attr_cmd_step` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-102
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_attr_cmd_step' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_ext_device_attr_cmd_step` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-103
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_attr_cmd_step' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_ext_device_attr_cmd_step` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-104
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_attr_cmd_step' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_ext_device_attr_cmd_step` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-105
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_cmd' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_ext_device_cmd` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-106
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_cmd' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_ext_device_cmd` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-107
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_cmd' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_ext_device_cmd` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-108
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_ext_device_cmd' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_ext_device_cmd` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-109
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_device_scene_config' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `center_device_scene_config` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-110
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_device_scene_config' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `center_device_scene_config` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-111
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_device_scene_config' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `center_device_scene_config` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-112
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'center_device_scene_config' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `center_device_scene_config` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-113
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_method' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_md_product_method` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-114
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_method' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_md_product_method` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-115
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_method' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_md_product_method` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-116
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_md_product_method' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_md_product_method` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-117
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_dev_data_share' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `iot_dev_data_share` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-118
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_dev_data_share' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `iot_dev_data_share` ADD COLUMN `creator_type` varchar(100) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-119
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_dev_data_share' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `iot_dev_data_share` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-120
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_dev_data_share' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `iot_dev_data_share` ADD COLUMN `updater_type` varchar(100) DEFAULT NULL COMMENT '更新用户类型';
