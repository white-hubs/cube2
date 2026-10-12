-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube_srv_safety_management`.`property_filing`  ADD COLUMN `num` int(11) DEFAULT 1 COMMENT '数量';
ALTER TABLE `cube_srv_safety_management`.`property_filing`  modify COLUMN `property_type` varchar(64) DEFAULT NULL COMMENT '父级资产类别Code';
ALTER TABLE `cube_srv_safety_management`.`property_filing`  ADD COLUMN `property_sub_type` varchar(64) DEFAULT NULL COMMENT '资产类别Code';