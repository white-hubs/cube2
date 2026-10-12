-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_norm' AND column_name='sort') AS column_exists;
ALTER TABLE `rule_norm` ADD COLUMN `sort` int(10) NULL DEFAULT NULL COMMENT '排序';

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_norm' AND column_name='show_area') AS column_exists;
ALTER TABLE `rule_norm` ADD COLUMN `show_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule' AND column_name='product_id') AS column_exists;
ALTER TABLE `medical_rule` ADD COLUMN `product_id` bigint(20) NULL DEFAULT NULL COMMENT '产品id';

-- changeset lijie:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_norm' AND column_name='upper_limit') AS column_exists;
ALTER TABLE `rule_norm` MODIFY COLUMN `upper_limit` decimal(50, 10) NULL DEFAULT NULL COMMENT '上限';

-- changeset lijie:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_norm' AND column_name='lower_limit') AS column_exists;
ALTER TABLE `rule_norm` MODIFY COLUMN `lower_limit` decimal(50, 10) NULL DEFAULT NULL COMMENT '下限';

-- changeset lijie:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_norm' AND column_name='sort') AS column_exists;
ALTER TABLE `rule_norm` ADD COLUMN `sort` int(10) NULL DEFAULT NULL COMMENT '排序';

