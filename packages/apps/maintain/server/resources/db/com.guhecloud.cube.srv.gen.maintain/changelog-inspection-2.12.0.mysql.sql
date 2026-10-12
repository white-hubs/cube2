-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_item_template' AND column_name='item_tag') AS column_exists;
ALTER TABLE `inspect_item_template` ADD `item_tag` varchar(200) NULL COMMENT '巡检项的tag';
ALTER TABLE `inspect_item_template` CHANGE `item_tag` `item_tag` varchar(200) NULL COMMENT '巡检项的tag' AFTER `name`;

-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_standard_item' AND column_name='item_tag') AS column_exists;
ALTER TABLE `inspect_standard_item` ADD `item_tag` varchar(200) NULL COMMENT '巡检项的tag';
ALTER TABLE `inspect_standard_item` CHANGE `item_tag` `item_tag` varchar(200) NULL COMMENT '巡检项的tag' AFTER `name`;

-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record_item' AND column_name='item_tag') AS column_exists;
ALTER TABLE `inspect_record_item` ADD `item_tag` varchar(200) NULL COMMENT '巡检项的tag';
ALTER TABLE `inspect_record_item` CHANGE `item_tag` `item_tag` varchar(200) NULL COMMENT '巡检项的tag' AFTER `item_name`;
