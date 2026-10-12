-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'center_biz_device_expand' AND column_name='spatial_coordinates') AS column_exists;
ALTER TABLE `center_biz_device_expand` ADD COLUMN `spatial_coordinates` VARCHAR(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NUlL DEFAULT NULL  COMMENT '空间坐标' AFTER `install_date`;
