
-- liquibase formatted sql

-- changeset qqq:1686103358461-1
ALTER TABLE `cube-srv-gen-property-management`.`assembly_classify` MODIFY COLUMN `deleted` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '是否已删除' AFTER `remarks`;
ALTER TABLE `cube-srv-gen-property-management`.`assembly_classify` MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `create_by`;
ALTER TABLE `cube-srv-gen-property-management`.`assembly_classify` MODIFY COLUMN `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `update_by`;
ALTER TABLE `cube-srv-gen-property-management`.`assembly_classify` MODIFY COLUMN `tenant` int(11) NULL DEFAULT NULL AFTER `update_time`;

ALTER TABLE consumable MODIFY COLUMN `stock_max` int(11) NULL DEFAULT NULL COMMENT '库存上线' AFTER `quality_period`;
ALTER TABLE consumable MODIFY COLUMN `stock_min` int(11) NULL DEFAULT NULL COMMENT '库存下线' AFTER `stock_max`;
ALTER TABLE consumable MODIFY COLUMN `use_limit` int(11) NULL DEFAULT NULL COMMENT '限领数量（人/月）' AFTER `stock_min`;
ALTER TABLE consumable MODIFY COLUMN `stock` int(11) NULL DEFAULT NULL COMMENT '当前库存数量' AFTER `use_limit`;
ALTER TABLE consumable ADD COLUMN `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `deleted`;

ALTER TABLE consumable_check MODIFY COLUMN `start_time` date NULL DEFAULT NULL COMMENT '盘点开始时间' AFTER `store_id`;

ALTER TABLE `consumable_check_detail` MODIFY COLUMN `check_num` int(11) NULL DEFAULT NULL COMMENT '应盘数量' AFTER `consumable_id`;
ALTER TABLE `consumable_check_detail` MODIFY COLUMN `num` decimal(8, 2) NULL DEFAULT NULL COMMENT '实盘数量' AFTER `check_num`;

ALTER TABLE consumable_store_stock ADD COLUMN `avg_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '库存均价' AFTER `num`;


-- 消息通知
INSERT INTO `cube-base-message`.`sat_msg_define` (`id`, `application_id`, `application_code`, `application_name`, `business_type`, `msg_type`, `code`, `name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1746411346643657492, 1588343888124665859, 'PROPERTY_MANAGEMENT', '资产管理', 'PROPERTY_MAINTENANCE', 'INVENTORY_TASK', 'INVENTORY_TASK', '盘点任务', 0, '1', '2023-04-24 20:27:21', '1', '2023-04-24 20:27:21');
/*
* 代办标签
*/

INSERT INTO `cube-srv-gen-todo`.`todo_tag_refer_business_type` (`id`, `todo_tag_code`, `business_type`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (53, 'PROPERTY_MANAGEMENT', 'PROPERTY_TASK', 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `cube-srv-gen-todo`.`todo_tag_refer_business_type` (`id`, `todo_tag_code`, `business_type`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (54, 'PROPERTY_MANAGEMENT', 'CONSUMABLE_TASK', 1, 0, NULL, NULL, NULL, NULL);
