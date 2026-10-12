-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='supervise_user') AS column_exists;
ALTER TABLE `project_schedule` DROP COLUMN `supervise_user`;

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='supervise_users') AS column_exists;
ALTER TABLE `project_schedule` ADD COLUMN `supervise_users` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监管负责人';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='construction_site') AS column_exists;
ALTER TABLE `project_schedule` ADD COLUMN `construction_site` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '施工地点';

-- changeset lijie:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='remarks') AS column_exists;
ALTER TABLE `project_schedule` ADD COLUMN `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注';

-- changeset lijie:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='project_files') AS column_exists;
ALTER TABLE `project_schedule` ADD COLUMN `project_files` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工程详细资料';