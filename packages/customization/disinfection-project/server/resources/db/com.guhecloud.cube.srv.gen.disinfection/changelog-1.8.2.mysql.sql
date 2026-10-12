-- liquibase formatted sql

-- changeset disinfection:alter-scheduled-task-group-id
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task' AND column_name = 'group_id') AS column_exists;
ALTER TABLE `member_scheduled_task` ADD COLUMN `group_id` bigint(20) NOT NULL COMMENT '成员分组ID' AFTER `id`;

-- changeset disinfection:alter-scheduled-task-creator-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task' AND column_name = 'creator_id') AS column_exists;
ALTER TABLE `member_scheduled_task` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset disinfection:alter-scheduled-task-creator-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task' AND column_name = 'creator_type') AS column_exists;
ALTER TABLE `member_scheduled_task` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset disinfection:alter-scheduled-task-creator-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task' AND column_name = 'updater_id') AS column_exists;
ALTER TABLE `member_scheduled_task` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset disinfection:alter-scheduled-task-creator-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task' AND column_name = 'updater_type') AS column_exists;
ALTER TABLE `member_scheduled_task` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset disinfection:alter-scheduled-task-device-creator-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_device' AND column_name = 'creator_id') AS column_exists;
ALTER TABLE `member_scheduled_task_device` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset disinfection:alter-scheduled-task-device-creator-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_device' AND column_name = 'creator_type') AS column_exists;
ALTER TABLE `member_scheduled_task_device` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset disinfection:alter-scheduled-task-device-creator-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_device' AND column_name = 'updater_id') AS column_exists;
ALTER TABLE `member_scheduled_task_device` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset disinfection:alter-scheduled-task-device-creator-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_device' AND column_name = 'updater_type') AS column_exists;
ALTER TABLE `member_scheduled_task_device` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset disinfection:alter-scheduled-task-plan-creator-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_plan' AND column_name = 'creator_id') AS column_exists;
ALTER TABLE `member_scheduled_task_plan` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset disinfection:alter-scheduled-task-plan-creator-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_plan' AND column_name = 'creator_type') AS column_exists;
ALTER TABLE `member_scheduled_task_plan` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset disinfection:alter-scheduled-task-plan-creator-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_plan' AND column_name = 'updater_id') AS column_exists;
ALTER TABLE `member_scheduled_task_plan` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset disinfection:alter-scheduled-task-plan-creator-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_plan' AND column_name = 'updater_type') AS column_exists;
ALTER TABLE `member_scheduled_task_plan` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
