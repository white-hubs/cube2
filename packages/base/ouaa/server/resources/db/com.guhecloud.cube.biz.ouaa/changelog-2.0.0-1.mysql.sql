-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'ouaa_card' AND column_name = 'default_card' ) AS column_exists;
ALTER TABLE ouaa_card ADD default_card tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否默认卡片' AFTER enabled;
