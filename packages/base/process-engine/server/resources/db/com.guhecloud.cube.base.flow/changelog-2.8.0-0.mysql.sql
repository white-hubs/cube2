-- liquibase formatted sql

-- changeset codex:alter-form-flow-group-tree-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'form_flow_group' AND column_name = 'parent_id' ) AS column_exists;
ALTER TABLE `form_flow_group` ADD COLUMN `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级分组ID';

-- changeset codex:alter-form-flow-group-tree-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'form_flow_group' AND column_name = 'level' ) AS column_exists;
ALTER TABLE `form_flow_group` ADD COLUMN `level` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '分组层级';

-- changeset codex:alter-form-flow-group-tree-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'form_flow_group' AND column_name = 'sequence' ) AS column_exists;
ALTER TABLE `form_flow_group` ADD COLUMN `sequence` varchar(500) NULL DEFAULT NULL COMMENT '序列号';

-- changeset codex:data-form-flow-group-tree-1
CREATE TEMPORARY TABLE `tmp_form_flow_group_root` (
  `tenant` int(10) NOT NULL,
  `root_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tenant`)
);

INSERT INTO `tmp_form_flow_group_root` (`tenant`, `root_id`)
SELECT DISTINCT `tenant`, 9000000000000000000 + CAST(`tenant` AS SIGNED)
FROM `form_flow_group`
WHERE `tenant` IS NOT NULL;

INSERT INTO `form_flow_group` (
  `id`,
  `name`,
  `parent_id`,
  `level`,
  `sequence`,
  `tenant`,
  `deleted`,
  `create_by`,
  `creator_id`,
  `creator_type`,
  `create_time`,
  `update_by`,
  `updater_id`,
  `updater_type`,
  `update_time`
)
SELECT
  `root_id`,
  '全部',
  NULL,
  1,
  CONCAT(`root_id`, '-'),
  `tenant`,
  0,
  'SYSTEM',
  NULL,
  NULL,
  NOW(),
  'SYSTEM',
  NULL,
  NULL,
  NOW()
FROM `tmp_form_flow_group_root`;

UPDATE `form_flow_group` g
JOIN `tmp_form_flow_group_root` r ON g.`tenant` = r.`tenant`
SET g.`parent_id` = r.`root_id`,
    g.`level` = 2,
    g.`sequence` = CONCAT(r.`root_id`, '-', g.`id`, '-')
WHERE g.`id` <> r.`root_id`;

DROP TEMPORARY TABLE `tmp_form_flow_group_root`;

-- changeset codex:alter-form-flow-group-tree-4
ALTER TABLE `form_flow_group` MODIFY COLUMN `level` int(10) UNSIGNED NOT NULL COMMENT '分组层级';

-- changeset codex:alter-form-flow-group-tree-5
ALTER TABLE `form_flow_group` MODIFY COLUMN `sequence` varchar(500) NOT NULL COMMENT '序列号';

-- changeset codex:index-form-flow-group-tree-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'form_flow_group' AND index_name = 'idx_form_flow_group_tenant_parent_deleted' ) AS index_exists;
CREATE INDEX `idx_form_flow_group_tenant_parent_deleted` ON `form_flow_group` (`tenant`, `parent_id`, `deleted`);

-- changeset codex:index-form-flow-group-tree-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'form_flow_group' AND index_name = 'idx_form_flow_group_tenant_level_deleted' ) AS index_exists;
CREATE INDEX `idx_form_flow_group_tenant_level_deleted` ON `form_flow_group` (`tenant`, `level`, `deleted`);
