-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'conference' AND column_name='conference_room_type_id') AS column_exists;
ALTER TABLE `conference` ADD COLUMN `conference_room_type_id` bigint(20) NULL DEFAULT NULL COMMENT '会议室类型id';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'conference' AND column_name='book_person_type') AS column_exists;
ALTER TABLE `conference` ADD COLUMN `book_person_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用人员类型';

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'conference' AND column_name='book_person') AS column_exists;
ALTER TABLE `conference` ADD COLUMN `book_person` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用人员';

-- changeset zengyadong:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'book_room' AND column_name='process_ins_id') AS column_exists;
ALTER TABLE `book_room` ADD COLUMN `process_ins_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id';

-- changeset zengyadong:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'book_room' AND column_name='participant_type') AS column_exists;
ALTER TABLE `book_room` ADD COLUMN `participant_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会议参与人类型';

-- changeset zengyadong:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'book_room' AND column_name='participant_ids') AS column_exists;
ALTER TABLE `book_room` ADD COLUMN `participant_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会议参与人id';

-- changeset zengyadong:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'field_configuration' AND column_name='hidden') AS column_exists;
ALTER TABLE `field_configuration` ADD COLUMN `hidden` BIGINT ( 20 )  NULL DEFAULT NULL COMMENT '是否隐藏';

-- changeset zengyadong:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'book_interval_record' AND column_name='status') AS column_exists;
ALTER TABLE `book_interval_record` ADD COLUMN `status` BIGINT ( 20 )  NULL DEFAULT NULL COMMENT '是否隐藏';

-- changeset zengyadong:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'appointment_record' AND column_name='status') AS column_exists;
ALTER TABLE `appointment_record` ADD COLUMN `process_ins_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id';

-- changeset zengyadong:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'conference_room_type') AS column_exists;
CREATE TABLE `conference_room_type` (
                                        `id` bigint(20) NOT NULL,
                                        `name` varchar(255) DEFAULT NULL,
                                        `tenant` varchar(255) DEFAULT NULL,
                                        `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        `deleted` varchar(255) DEFAULT NULL,
                                        `status` varchar(10) DEFAULT NULL,
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;