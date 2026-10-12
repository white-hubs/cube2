-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-supplier`.`gen_supplier`
    ADD COLUMN `disable` bigint(20) NULL DEFAULT NULL COMMENT '是否启用' AFTER `other_contacts`;

