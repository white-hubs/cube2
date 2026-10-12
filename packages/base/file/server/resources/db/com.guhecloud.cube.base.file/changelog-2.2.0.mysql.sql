-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_0' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_0` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_0' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_0` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_0' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_0` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_0' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_0` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_1' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_1` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_1' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_1` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_1' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_1` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_1' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_1` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_2' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_2` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-14
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_2' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_2` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-15
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_2' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_2` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-16
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_2' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_2` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-17
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_3' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_3` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-18
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_3' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_3` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-19
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_3' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_3` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-20
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_3' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_3` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-21
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_4' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_4` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-22
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_4' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_4` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-23
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_4' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_4` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-24
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_4' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_4` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-25
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_5' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_5` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-26
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_5' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_5` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-27
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_5' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_5` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-28
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_5' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_5` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-29
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_6' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_6` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-30
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_6' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_6` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-31
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_6' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_6` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-32
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_6' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_6` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-33
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_7' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_7` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-34
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_7' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_7` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-35
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_7' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_7` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-36
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_7' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_7` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-37
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_8' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_8` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-38
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_8' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_8` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-39
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_8' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_8` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-40
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_8' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_8` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-41
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_9' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_9` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-42
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_9' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_9` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-43
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_9' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_9` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-44
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_9' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_9` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-45
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_10' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_10` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-46
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_10' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_10` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-47
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_10' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_10` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-48
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_10' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_10` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-49
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_11' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_11` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-50
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_11' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_11` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-51
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_11' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_11` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-52
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_11' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_11` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-53
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_12' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_12` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-54
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_12' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_12` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-55
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_12' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_12` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-56
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_12' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_12` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-57
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_13' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_13` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-58
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_13' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_13` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-59
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_13' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_13` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-60
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_13' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_13` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-61
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_14' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_14` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-62
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_14' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_14` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-63
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_14' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_14` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-64
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_14' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_14` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-65
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_15' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_15` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-66
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_15' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_15` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-67
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_15' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_15` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-68
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_15' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_15` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-69
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_config' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_config` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-70
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_config' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_config` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-71
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_config' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_config` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-72
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_config' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_config` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-73
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_expiration_policy' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_expiration_policy` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-74
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_expiration_policy' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_expiration_policy` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-75
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_expiration_policy' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_expiration_policy` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-76
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_expiration_policy' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_expiration_policy` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-77
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-78
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-79
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-80
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-81
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_0' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_0` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-82
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_0' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_0` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-83
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_0' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_0` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-84
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_0' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_0` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-85
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_1' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_1` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-86
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_1' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_1` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-87
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_1' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_1` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-88
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_1' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_1` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-89
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_2' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_2` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-90
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_2' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_2` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-91
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_2' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_2` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-92
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_2' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_2` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-93
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_3' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_3` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-94
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_3' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_3` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-95
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_3' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_3` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-96
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_3' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_3` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-97
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_4' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_4` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-98
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_4' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_4` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-99
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_4' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_4` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-100
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_4' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_4` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-101
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_5' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_5` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-102
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_5' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_5` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-103
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_5' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_5` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-104
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_5' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_5` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-105
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_6' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_6` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-106
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_6' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_6` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-107
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_6' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_6` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-108
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_6' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_6` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-109
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_7' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_7` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-110
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_7' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_7` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-111
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_7' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_7` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-112
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_7' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_7` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-113
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_8' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_8` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-114
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_8' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_8` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-115
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_8' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_8` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-116
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_8' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_8` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-117
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_9' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_9` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-118
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_9' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_9` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-119
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_9' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_9` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-120
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_9' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_9` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-121
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_10' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_10` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-122
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_10' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_10` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-123
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_10' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_10` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-124
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_10' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_10` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-125
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_11' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_11` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-126
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_11' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_11` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-127
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_11' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_11` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-128
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_11' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_11` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-129
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_12' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_12` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-130
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_12' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_12` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-131
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_12' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_12` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-132
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_12' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_12` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-133
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_13' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_13` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-134
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_13' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_13` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-135
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_13' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_13` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-136
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_13' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_13` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-137
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_14' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_14` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-138
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_14' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_14` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-139
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_14' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_14` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-140
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_14' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_14` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-141
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_15' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_ref_15` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-142
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_15' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_ref_15` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-143
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_15' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_ref_15` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-144
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_ref_15' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_ref_15` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-145
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_store_sign_up' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `file_store_sign_up` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-146
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_store_sign_up' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `file_store_sign_up` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-147
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_store_sign_up' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `file_store_sign_up` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-148
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file_store_sign_up' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `file_store_sign_up` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-149
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scene_info' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `scene_info` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-150
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scene_info' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `scene_info` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-151
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scene_info' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `scene_info` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-152
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scene_info' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `scene_info` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';