-- liquibase formatted sql


-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_group_user' AND column_name='select_group') AS column_exists;
ALTER TABLE member_group_user ADD select_group BOOL NULL COMMENT '当前选择的分组';
ALTER TABLE member_group_user CHANGE select_group select_group BOOL NULL COMMENT '当前选择的分组' AFTER group_id;
