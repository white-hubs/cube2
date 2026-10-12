-- liquibase formatted sql

-- changeset zengyadong:insert-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'appointment_record' AND column_name='cancel_reason' ) AS column_exists;
ALTER TABLE `cube-srv-gen-conferencemanage`.`appointment_record`
    ADD COLUMN `cancel_reason` VARCHAR ( 255 ) NULL COMMENT '取消原因';

-- changeset zengyadong:insert-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'book_room' AND column_name='cancel_reason' ) AS column_exists;
ALTER TABLE `cube-srv-gen-conferencemanage`.`book_room`
    ADD COLUMN `cancel_reason` VARCHAR ( 255 ) NULL COMMENT '取消原因';

-- changeset zengyadong:update-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'conference' AND column_name='administrators' ) AS column_exists;
ALTER TABLE `cube-srv-gen-conferencemanage`.`conference` MODIFY COLUMN `administrators` VARCHAR(255);