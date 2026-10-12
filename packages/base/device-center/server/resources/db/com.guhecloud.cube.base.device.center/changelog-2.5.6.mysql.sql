-- changeset lhp:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_md_product' AND column_name='extend_config') AS column_exists;
ALTER TABLE `iot_md_product` MODIFY COLUMN `extend_config` LONGTEXT COMMENT '产品扩展配置（JSON字符串';