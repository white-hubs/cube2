-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_tasks' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `maintenance_tasks` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_tasks' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `maintenance_tasks` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_tasks' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `maintenance_tasks` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_tasks' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `maintenance_tasks` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_nfc_details' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_nfc_details` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_nfc_details' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_nfc_details` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_nfc_details' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_nfc_details` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_nfc_details' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_nfc_details` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_alarm' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_alarm` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_alarm' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_alarm` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_alarm' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_alarm` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_alarm' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_alarm` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_device' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_device` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-14
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_device' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_device` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-15
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_device' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_device` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-16
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_device' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_device` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-17
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_location' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_location` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-18
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_location' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_location` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-19
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_location' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_location` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-20
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_location' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_location` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-21
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_plan` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-22
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_plan` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-23
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_plan` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-24
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_plan` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-25
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan_location' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_plan_location` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-26
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan_location' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_plan_location` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-27
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan_location' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_plan_location` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-28
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan_location' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_plan_location` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-29
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan_task' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_plan_task` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-30
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan_task' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_plan_task` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-31
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan_task' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_plan_task` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-32
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_plan_task' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_plan_task` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-33
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_route' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_route` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-34
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_route' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_route` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-35
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_route' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_route` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-36
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_route' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_route` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-37
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_task' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_task` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-38
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_task' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_task` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-39
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_task' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_task` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-40
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_task' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_task` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-41
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_task_location' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `patrol_task_location` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-42
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_task_location' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `patrol_task_location` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-43
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_task_location' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `patrol_task_location` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-44
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'patrol_task_location' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `patrol_task_location` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-45
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_case' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `fault_case` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-46
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_case' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `fault_case` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-47
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_case' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `fault_case` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-48
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_case' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `fault_case` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-49
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_order' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `fault_order` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-50
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_order' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `fault_order` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-51
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_order' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `fault_order` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-52
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_order' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `fault_order` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-53
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_order_config' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `fault_order_config` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-54
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_order_config' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `fault_order_config` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-55
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_order_config' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `fault_order_config` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-56
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_order_config' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `fault_order_config` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-57
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_report' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `fault_report` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-58
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_report' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `fault_report` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-59
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_report' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `fault_report` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-60
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_report' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `fault_report` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-61
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_type' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `fault_type` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-62
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_type' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `fault_type` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-63
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_type' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `fault_type` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-64
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'fault_type' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `fault_type` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-65
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_category' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `hidden_danger_category` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-66
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_category' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `hidden_danger_category` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-67
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_category' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `hidden_danger_category` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-68
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_category' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `hidden_danger_category` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-69
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_level' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `hidden_danger_level` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-70
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_level' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `hidden_danger_level` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-71
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_level' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `hidden_danger_level` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-72
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_level' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `hidden_danger_level` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-73
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_new' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `hidden_danger_new` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-74
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_new' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `hidden_danger_new` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-75
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_new' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `hidden_danger_new` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-76
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_new' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `hidden_danger_new` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-77
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_type' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `hidden_danger_type` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-78
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_type' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `hidden_danger_type` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-79
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_type' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `hidden_danger_type` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-80
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'hidden_danger_type' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `hidden_danger_type` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-81
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_cron' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_cron` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-82
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_cron' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_cron` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-83
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_cron' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_cron` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-84
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_cron' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_cron` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-85
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_item_template' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_item_template` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-86
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_item_template' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_item_template` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-87
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_item_template' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_item_template` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-88
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_item_template' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_item_template` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-89
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_plan` MODIFY COLUMN creator_id VARCHAR(100) NULL COMMENT '创建用户id';

-- changeset lanchun:alter-90
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_plan` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-91
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_plan` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-92
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_plan` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-93
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record_item' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_record_item` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-94
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record_item' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_record_item` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-95
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record_item' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_record_item` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-96
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record_item' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_record_item` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-97
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-98
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-99
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-100
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-101
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_result' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_result` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-102
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_result' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_result` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-103
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_result' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_result` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-104
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_result' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_result` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-105
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_scene_point' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_scene_point` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-106
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_scene_point' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_scene_point` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-107
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_scene_point' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_scene_point` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-108
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_scene_point' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_scene_point` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-109
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_scene_type' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_scene_type` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-110
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_scene_type' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_scene_type` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-111
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_scene_type' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_scene_type` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-112
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_scene_type' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_scene_type` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-113
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_standard' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_standard` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-114
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_standard' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_standard` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-115
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_standard' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_standard` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-116
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_standard' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_standard` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-117
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_task` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-118
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_task` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-119
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_task` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-120
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_task` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-121
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task_target_item' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_task_target_item` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-122
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task_target_item' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_task_target_item` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-123
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task_target_item' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_task_target_item` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-124
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task_target_item' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_task_target_item` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-125
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_type' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_type` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-126
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_type' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_type` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-127
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_type' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_type` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-128
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_type' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_type` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-129
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_standard_item' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_standard_item` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-130
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_standard_item' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_standard_item` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-131
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_standard_item' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_standard_item` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-132
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_standard_item' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_standard_item` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-133
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_plan` MODIFY COLUMN creator_id VARCHAR(100) NULL COMMENT '创建用户id';

-- changeset lanchun:alter-134
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_plan` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-135
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_plan` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-136
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_plan` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-137
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan_relation' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_plan_relation` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-138
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan_relation' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_plan_relation` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-139
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan_relation' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_plan_relation` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-140
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_plan_relation' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_plan_relation` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-141
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task_target_relation' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_task_target_relation` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-142
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task_target_relation' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_task_target_relation` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-143
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task_target_relation' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_task_target_relation` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-144
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_task_target_relation' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_task_target_relation` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-145
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_company' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `outsource_company` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-146
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_company' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `outsource_company` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-147
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_company' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `outsource_company` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-148
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_company' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `outsource_company` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-149
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_device_instance' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `outsource_device_instance` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-150
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_device_instance' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `outsource_device_instance` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-151
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_device_instance' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `outsource_device_instance` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-152
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_device_instance' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `outsource_device_instance` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-153
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_model' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `outsource_model` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-154
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_model' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `outsource_model` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-155
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_model' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `outsource_model` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-156
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_model' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `outsource_model` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-157
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_work_order' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `outsource_work_order` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-158
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_work_order' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `outsource_work_order` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-159
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_work_order' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `outsource_work_order` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-160
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'outsource_work_order' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `outsource_work_order` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-161
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'retrofit_order' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `retrofit_order` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-162
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'retrofit_order' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `retrofit_order` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-163
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'retrofit_order' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `retrofit_order` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-164
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'retrofit_order' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `retrofit_order` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-165
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scada_resources' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `scada_resources` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-166
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scada_resources' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `scada_resources` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-167
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scada_resources' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `scada_resources` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-168
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scada_resources' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `scada_resources` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-169
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_object' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `maintenance_object` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-170
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_object' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `maintenance_object` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-171
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_object' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `maintenance_object` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-172
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_object' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `maintenance_object` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-173
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_object_projects' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `maintenance_object_projects` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-174
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_object_projects' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `maintenance_object_projects` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-175
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_object_projects' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `maintenance_object_projects` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-176
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_object_projects' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `maintenance_object_projects` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-177
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_plan' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `maintenance_plan` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-178
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_plan' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `maintenance_plan` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-179
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_plan' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `maintenance_plan` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-180
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_plan' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `maintenance_plan` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-181
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_proget_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `maintenance_proget_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-182
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_proget_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `maintenance_proget_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-183
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_proget_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `maintenance_proget_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-184
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_proget_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `maintenance_proget_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-185
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_project' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `maintenance_project` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-186
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_project' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `maintenance_project` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-187
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_project' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `maintenance_project` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-188
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_project' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `maintenance_project` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-189
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_rule_condition' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `maintenance_rule_condition` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-190
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_rule_condition' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `maintenance_rule_condition` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-191
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_rule_condition' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `maintenance_rule_condition` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-192
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_rule_condition' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `maintenance_rule_condition` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-193
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_rule_condition_exp' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `maintenance_rule_condition_exp` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-194
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_rule_condition_exp' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `maintenance_rule_condition_exp` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-195
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_rule_condition_exp' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `maintenance_rule_condition_exp` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-196
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'maintenance_rule_condition_exp' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `maintenance_rule_condition_exp` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-197
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record_repair' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `inspect_record_repair` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-198
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record_repair' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `inspect_record_repair` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-199
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record_repair' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `inspect_record_repair` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-200
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'inspect_record_repair' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `inspect_record_repair` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
