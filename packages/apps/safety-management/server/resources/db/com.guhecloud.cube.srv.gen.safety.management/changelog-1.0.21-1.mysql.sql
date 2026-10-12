-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE IF NOT EXISTS `cube_srv_safety_management`.`market_monthly_wind_up`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `monthly_report_id` bigint(20) NULL DEFAULT NULL COMMENT '总部月报主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `monthly` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月报月份',
  `month_date` datetime NULL DEFAULT NULL COMMENT '月报时间',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '填报状态',
  `report_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '填报数据(前端使用)',
  `report_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上报人',
  `report_time` datetime NULL DEFAULT NULL COMMENT '上报时间',
  `report_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件地址',
  `report_id` bigint(20) NULL DEFAULT NULL COMMENT '文件id',
  `report_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `market_tenant` int(11) NULL DEFAULT NULL COMMENT '市场租户',
  `market_name` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市场名称',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_report_id_deleted_tenant_market_tenant`(`monthly_report_id`, `market_tenant`, `deleted`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '市场月报上报信息' ROW_FORMAT = Dynamic;


INSERT INTO `market_monthly_wind_up`(`id`,monthly_report_id,title,monthly,month_date,`status`,report_data,report_user_id,report_time,report_url,report_id,report_name,market_tenant,market_name,deleted,create_by,create_time,update_by,update_time,tenant)
SELECT
`id`,monthly_report_id,title,monthly,month_date,`status`,report_data,report_user_id,report_time,report_url,report_id,report_name,market_tenant,market_name,deleted,create_by,create_time,update_by,update_time,tenant
FROM
market_monthly_report
WHERE `status` = 'WIND_UP' AND deleted = 0