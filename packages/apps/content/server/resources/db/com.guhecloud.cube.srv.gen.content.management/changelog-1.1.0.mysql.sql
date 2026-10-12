-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'announcement' AND column_name='is_hide') AS column_exists;
ALTER TABLE `announcement` ADD COLUMN `status` varchar(20) DEFAULT NULL COMMENT '是否隐藏';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'announcement' AND column_name='is_topping') AS column_exists;
ALTER TABLE `announcement` ADD COLUMN `is_topping` bigint(20) DEFAULT NULL COMMENT '是否置顶';

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'announcement' AND column_name='publish_agency') AS column_exists;
ALTER TABLE `announcement` ADD COLUMN `publish_agency` bigint(20) DEFAULT NULL COMMENT '发布机构';

-- changeset zengyadong:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'announcement' AND column_name='tag_id') AS column_exists;
ALTER TABLE `announcement` ADD COLUMN `tag_id` bigint(20) DEFAULT NULL COMMENT '公告标签';


-- changeset zengyadong:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'model_type' AND column_name='one_level_type') AS column_exists;
ALTER TABLE `model_type` ADD COLUMN `one_level_type` varchar(50) DEFAULT NULL COMMENT '一级类型';

-- changeset zengyadong:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'notice_config' AND column_name='role') AS column_exists;
ALTER TABLE `notice_config` ADD COLUMN `role` varchar(200) DEFAULT NULL COMMENT '指定角色';