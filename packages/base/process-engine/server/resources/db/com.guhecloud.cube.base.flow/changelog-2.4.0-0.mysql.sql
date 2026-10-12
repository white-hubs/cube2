-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_flow' AND column_name='public_form') AS column_exists;
ALTER TABLE `form_flow` ADD COLUMN `public_form` tinyint(1) NULL DEFAULT NULL COMMENT '公开表单配置';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_flow' AND column_name='qr_image') AS column_exists;
ALTER TABLE `form_flow` ADD COLUMN `qr_image` bigint(20) NULL DEFAULT NULL COMMENT '二维码图片';

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_flow' AND column_name='qr_code_url') AS column_exists;
ALTER TABLE `form_flow` ADD COLUMN `qr_code_url` varchar(200) NULL DEFAULT NULL COMMENT '链接url';