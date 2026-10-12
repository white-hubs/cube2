-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'broadcast_play_record' AND column_name = 'broadcast_play_content' ) AS column_exists;
ALTER TABLE `broadcast_play_record` MODIFY COLUMN broadcast_play_content varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '广播内容';

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'broadcast_play_record' AND column_name = 'broadcast_template_content' ) AS column_exists;
ALTER TABLE `broadcast_play_record` MODIFY COLUMN broadcast_template_content varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '广播模板原内容';

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'broadcast_play_rule_template' AND column_name = 'template_content' ) AS column_exists;
ALTER TABLE `broadcast_play_rule_template` MODIFY COLUMN template_content varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模板内容';

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'broadcast_template' AND column_name = 'content' ) AS column_exists;
ALTER TABLE `broadcast_template` MODIFY COLUMN content varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模板内容';
