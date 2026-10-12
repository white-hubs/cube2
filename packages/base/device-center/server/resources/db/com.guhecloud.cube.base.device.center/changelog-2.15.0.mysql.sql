-- changeset antigravity:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_record' AND column_name = 'target_identifier' ) AS column_exists;
ALTER TABLE `iot_command_record` ADD COLUMN `target_identifier` varchar(255) DEFAULT NULL COMMENT '目标标识(属性Code/方法Code/事件Code)';