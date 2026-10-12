-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-linkage`.`event` ADD INDEX `idx_rule_id_deleted_tenant`(`rule_id`, `tenant`, `deleted`) USING BTREE;

ALTER TABLE `cube-biz-linkage`.`event_action` ADD INDEX `idx_event_id_tenant_deleted`(`event_id`, `tenant`, `deleted`) USING BTREE;


ALTER TABLE `cube-biz-linkage`.`event_iot_device_pro` CHANGE `device_genre_id` `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号';

ALTER TABLE `cube-biz-linkage`.`event_iot_device_pro` CHANGE `device_genre_name` `device_prod_category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称';

ALTER TABLE `cube-biz-linkage`.`event_iot_device_pro` CHANGE `device_model_id` `device_prod_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备产品主键';

ALTER TABLE `cube-biz-linkage`.`event_iot_device_pro` CHANGE `device_model_name` `device_prod_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称';


ALTER TABLE `cube-biz-linkage`.`event_iot_device_pro_record` CHANGE `device_genre_id` `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号';

ALTER TABLE `cube-biz-linkage`.`event_iot_device_pro_record` CHANGE `device_genre_name` `device_prod_category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称';

ALTER TABLE `cube-biz-linkage`.`event_iot_device_pro_record` CHANGE `device_model_id` `device_prod_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备产品主键';

ALTER TABLE `cube-biz-linkage`.`event_iot_device_pro_record` CHANGE `device_model_name` `device_prod_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称';


ALTER TABLE `cube-biz-linkage`.`rule_device_alarm` CHANGE `device_genre_id` `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号';

ALTER TABLE `cube-biz-linkage`.`rule_device_alarm` CHANGE `device_genre_name` `device_prod_category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称';

ALTER TABLE `cube-biz-linkage`.`rule_device_alarm` CHANGE `device_model_id` `device_prod_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备产品主键';

ALTER TABLE `cube-biz-linkage`.`rule_device_alarm` CHANGE `device_model_name` `device_prod_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称';

ALTER TABLE `cube-biz-linkage`.`rule_device_alarm` ADD COLUMN `device_prod_category_code_concat` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号序列号' AFTER `device_prod_name`;


ALTER TABLE `cube-biz-linkage`.`rule_device_condition` CHANGE `device_genre_id` `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号';

ALTER TABLE `cube-biz-linkage`.`rule_device_condition` ADD COLUMN `device_prod_category_code_concat` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号序列号' AFTER `device_prod_category_code`;

ALTER TABLE `cube-biz-linkage`.`rule_device_condition` CHANGE `device_genre_name` `device_prod_category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称';

ALTER TABLE `cube-biz-linkage`.`rule_device_condition` CHANGE `device_model_id` `device_prod_id` bigint(20) NULL DEFAULT NULL COMMENT '设备型号主键';

ALTER TABLE `cube-biz-linkage`.`rule_device_condition` CHANGE `device_model_name` `device_prod_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备型号名称';