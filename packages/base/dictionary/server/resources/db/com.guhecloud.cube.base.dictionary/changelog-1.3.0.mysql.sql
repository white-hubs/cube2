-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'data_dictionary' AND column_name='variable') AS column_exists;
ALTER TABLE `data_dictionary` MODIFY COLUMN `variable` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '变量';

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'cascade_dictionary' AND column_name='variable') AS column_exists;
ALTER TABLE `cascade_dictionary` MODIFY COLUMN `variable` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '变量';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'sys_dictionary' AND column_name='variable') AS column_exists;
ALTER TABLE `sys_dictionary` MODIFY COLUMN `variable` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '变量';

-- changeset lijie:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'dictionary_item' AND column_name='change_notice') AS column_exists;
ALTER TABLE `dictionary_item` ADD COLUMN `change_notice` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更通知,默认不通知';