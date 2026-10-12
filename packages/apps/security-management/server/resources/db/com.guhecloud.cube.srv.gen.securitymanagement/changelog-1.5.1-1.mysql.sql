-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    MODIFY COLUMN `genre_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备品类' AFTER `device_id`,
    MODIFY COLUMN `genre` varchar(50) NULL DEFAULT NULL COMMENT '设备品类id' AFTER `genre_name`;

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    ADD COLUMN `lnt_and_lat` varchar(100) NULL COMMENT '经纬度' AFTER `brand_Name`;

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    CHANGE COLUMN `genre_name` `device_prod_category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备品类' AFTER `device_id`;

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    ADD COLUMN `device_prod_category_code_concat` varchar(100) NULL COMMENT '品类序列号' AFTER `lnt_and_lat`;

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    CHANGE COLUMN `genre` `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备品类code' AFTER `device_prod_category_name`;

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    CHANGE COLUMN `model_id` `device_prod_id` bigint(20) NULL DEFAULT NULL COMMENT '产品id' AFTER `area_id`,
    CHANGE COLUMN `model_name` `device_prod_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品' AFTER `device_prod_id`;

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    CHANGE COLUMN `brand_Name` `brand_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌' AFTER `brand_code`;

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    MODIFY COLUMN `communication_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '在线离线状态（0离线,1是在线）' AFTER `device_status`;

ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    MODIFY COLUMN `device_status` int(10) NULL DEFAULT NULL COMMENT '设备状态' AFTER `code`;

CREATE TABLE `water_map_type` (
                                  `id` bigint(20) NOT NULL,
                                  `map_code` varchar(255) DEFAULT NULL COMMENT '映射code',
                                  `device_prod_category_name` varchar(255) DEFAULT NULL COMMENT '品类名称',
                                  `device_prod_category_code` varchar(255) DEFAULT NULL COMMENT '品类code',
                                  `tenant` int(11) DEFAULT NULL,
                                  `deleted` bigint(20) DEFAULT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  `update_by` varchar(255) DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  `create_by` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;