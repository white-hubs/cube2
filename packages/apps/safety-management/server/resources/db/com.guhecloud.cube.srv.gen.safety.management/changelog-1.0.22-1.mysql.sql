-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'property_filing' AND column_name='attribute') AS column_exists;
ALTER TABLE `property_filing` ADD COLUMN `attribute` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性';

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'property_filing' AND column_name='bim_code') AS column_exists;
ALTER TABLE `property_filing` ADD COLUMN `bim_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型编码';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'property_filing' AND column_name='site_point_name') AS column_exists;
ALTER TABLE `property_filing` ADD COLUMN `site_point_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场点名称';

-- changeset lijie:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'property_filing' AND column_name='stall_name') AS column_exists;
ALTER TABLE `property_filing` ADD COLUMN `stall_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位名称';
