-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_ask_leave_from' AND column_name='post_id') AS column_exists;
ALTER TABLE att_ask_leave_from CHANGE post_id post_ids VARCHAR(500) NULL COMMENT '职位ids';
ALTER TABLE att_ask_leave_from MODIFY COLUMN post_ids VARCHAR(500) NULL COMMENT '职位ids';

-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_leave_post_from' AND column_name='post_id') AS column_exists;
ALTER TABLE att_leave_post_from CHANGE post_id post_ids VARCHAR(500) NULL COMMENT '职位ids';
ALTER TABLE att_leave_post_from MODIFY COLUMN post_ids VARCHAR(500) NULL COMMENT '职位ids';

-- changeset chx:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_away_business_from' AND column_name='post_id') AS column_exists;
ALTER TABLE att_away_business_from CHANGE post_id post_ids VARCHAR(500) NULL COMMENT '职位ids';
ALTER TABLE att_away_business_from MODIFY COLUMN post_ids VARCHAR(500) NULL COMMENT '职位ids';