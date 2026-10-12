-- liquibase formatted sql


-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'terminal_id' ) AS column_exists;
ALTER TABLE `permission` DROP COLUMN `terminal_id`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'application_id' ) AS column_exists;
ALTER TABLE `permission` DROP COLUMN `application_id`;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'resource_type' ) AS column_exists;
ALTER TABLE `permission` DROP COLUMN `resource_type`;

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'resource_id' ) AS column_exists;
ALTER TABLE `permission` DROP COLUMN `resource_id`;

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'code' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限编号' AFTER `id`;

-- changeset lanchun:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'name' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称' AFTER `code`;

-- changeset lanchun:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'type' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限类型' AFTER `name`;

-- changeset lanchun:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'description' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限描述' AFTER `type`;

-- changeset lanchun:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'deleted' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除' AFTER `description`;

-- changeset lanchun:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'create_by' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `deleted`;

-- changeset lanchun:alter-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'create_time' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间' AFTER `create_by`;

-- changeset lanchun:alter-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'update_by' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;

-- changeset lanchun:alter-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'permission' AND column_name = 'update_time' ) AS column_exists;
ALTER TABLE `permission` ADD COLUMN `update_time` datetime NULL DEFAULT NULL COMMENT '更新人' AFTER `update_by`;

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'permission_interface') AS column_exists;
CREATE TABLE `permission_interface`  (
  `id` bigint(20) NOT NULL,
  `permission_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限编号',
  `interface_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口权限编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限关联接口' ROW_FORMAT = Dynamic;

-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'subject_permission') AS column_exists;
CREATE TABLE `subject_permission`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `subject_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主体主键',
  `subject_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主体类型',
  `permission_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限编号',
  `permission_extent_config` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限配置扩充信息',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '认证主体权限配置' ROW_FORMAT = DYNAMIC;

-- changeset zengyadong:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'old_permission_mapping') AS column_exists;
CREATE TABLE `old_permission_mapping`  (
  `old_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '旧权限编号',
  `new_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新权限编号',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '旧权限映射' ROW_FORMAT = Dynamic;



-- 功能权限迁移sql
REPLACE INTO subject_permission(
`id`,
`subject_id`,
`subject_type`,
`permission_code`,
`permission_extent_config`,
`tenant`,
`deleted`,
`create_by`,
`create_time`,
`update_by`,
`update_time`
)SELECT
t2.id,
t1.id as subject_id,
'ROLE',
t4.new_code,
null,
t1.tenant,
0,
t1.create_by,
t1.create_time,
t1.update_by,
t1.update_time
FROM
role t1
INNER JOIN ouaa_role_menu_function t2 on t1.id = t2.role_id
INNER JOIN ouaa_function t3 on t2.function_id = t3.id
INNER JOIN old_permission_mapping t4 on t3.`code` = t4.old_code
WHERE t4.type = 'funcation'

-- changeset lanchun:alter-14
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'menu' AND INDEX_NAME = 'idx_menu_terminal_id_application_id_deleted_name' ) AS column_exists;
ALTER TABLE `cube-biz-ouaa`.`menu` DROP INDEX `idx_menu_terminal_id_application_id_deleted_name`;

-- changeset lanchun:alter-15
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'menu' AND INDEX_NAME = 'idx_menu_terminal_id_application_id_deleted_code' ) AS column_exists;
ALTER TABLE `cube-biz-ouaa`.`menu` DROP INDEX `idx_menu_terminal_id_application_id_deleted_code`;

-- changeset lanchun:alter-16
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'menu' AND INDEX_NAME = 'idx_menu_terminal_id_application_id_parent_id_deleted_name' ) AS column_exists;
ALTER TABLE `cube-biz-ouaa`.`menu` ADD UNIQUE INDEX `idx_menu_terminal_id_application_id_parent_id_deleted_name`(`terminal_id`, `application_id`, `name`, `parent_id`, `deleted`) USING BTREE;

-- changeset lanchun:alter-17
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'menu' AND INDEX_NAME = 'idx_menu_terminal_id_application_id_parent_id_deleted_code' ) AS column_exists;
ALTER TABLE `cube-biz-ouaa`.`menu` ADD UNIQUE INDEX `idx_menu_terminal_id_application_id_parent_id_deleted_code`(`terminal_id`, `application_id`, `code`, `parent_id`, `deleted`) USING BTREE;