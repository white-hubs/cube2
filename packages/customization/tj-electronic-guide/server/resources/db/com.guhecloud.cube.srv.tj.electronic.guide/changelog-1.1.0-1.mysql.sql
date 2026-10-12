-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `tj_guide_area_manage` ADD COLUMN `level` int(11) NULL DEFAULT NULL COMMENT '顺序' AFTER `zoom`;
