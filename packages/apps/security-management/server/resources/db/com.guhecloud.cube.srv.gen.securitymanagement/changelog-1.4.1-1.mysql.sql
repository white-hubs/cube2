-- liquibase formatted sql

-- changeset qqq:1686103358461-1
ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    ADD COLUMN `brand_code` varchar(50) NULL COMMENT '品牌code' AFTER `third_party_id`,
ADD COLUMN `brand_Name` varchar(50) NULL COMMENT '品牌' AFTER `brand_code`;