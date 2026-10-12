-- liquibase formatted sql

-- changeset chenhongxin:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'company' AND column_name='long_term') AS column_exists;
ALTER TABLE company ADD long_term TINYINT(1) DEFAULT 0 NULL COMMENT '营业期限是否长期(true长期';
ALTER TABLE company CHANGE long_term long_term TINYINT(1) DEFAULT 0 NULL COMMENT '营业期限是否长期(true长期';

-- changeset chenhongxin:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'company' AND column_name='business_license_file') AS column_exists;
ALTER TABLE company ADD business_license_file varchar(200) NULL COMMENT '营业执照图片';
ALTER TABLE company CHANGE business_license_file business_license_file varchar(200) NULL COMMENT '营业执照图片';

-- changeset chenhongxin:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'company' AND column_name='expiration_notice') AS column_exists;
ALTER TABLE `company` ADD COLUMN `expiration_notice` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '到期通知(true表示已经通知过';