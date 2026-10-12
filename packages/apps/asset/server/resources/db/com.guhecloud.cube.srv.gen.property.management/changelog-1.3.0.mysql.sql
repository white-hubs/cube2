-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `assembly_classify` ADD COLUMN `code` varchar(50) NULL DEFAULT NULL COMMENT '编码';
ALTER TABLE `assembly_classify` ADD COLUMN `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父级id';
ALTER TABLE `property_check_task` ADD COLUMN `abnormal_number` int(11) NULL DEFAULT 0 COMMENT '异常' AFTER `reality_end_time`;
ALTER TABLE `property_check_task_admin` ADD COLUMN `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明' AFTER `admin_id`;
ALTER TABLE `property_check_task_relation` ADD COLUMN `abnormal_type` bigint(50) NULL DEFAULT NULL COMMENT '异常类型' AFTER `tenant`;
ALTER TABLE `property_check_task_relation` ADD COLUMN `abnormal_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常备注' AFTER `abnormal_type`;
ALTER TABLE `property_check_task_relation` ADD COLUMN `abnormal_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常文件id' AFTER `abnormal_remarks`;
ALTER TABLE `property_detailed` MODIFY COLUMN `type_id` bigint(20) NULL DEFAULT NULL COMMENT '资产类别id' AFTER `name`;
ALTER TABLE `property_detailed` ADD COLUMN `receive_user_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人电话' AFTER `level`;

ALTER TABLE `cube-srv-gen-property-management`.`property_detailed` ADD COLUMN `finance_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码';
ALTER TABLE `cube-srv-gen-property-management`.`property_detailed` MODIFY COLUMN `serial_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资产编码' AFTER `id`;

CREATE TABLE `property_repair_report` (
                                          `id` bigint(20) NOT NULL,
                                          `process_instance_id` varchar(50) NOT NULL COMMENT '流程id（工作流）',
                                          `property_status` varchar(50) NOT NULL COMMENT '资产状态',
                                          `status` varchar(50) NOT NULL COMMENT '审核结果',
                                          `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
                                          `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
                                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
                                          `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                          `tenant` int(11) NOT NULL COMMENT '租户id',
                                          `property_id` bigint(20) NOT NULL COMMENT '资产id',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产报修记录表';

ALTER TABLE `cube-srv-gen-property-management`.`assembly_classify` ADD COLUMN `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态';
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1726913711757926401, 'property-abnormal-type', '资产异常类型', 0, '17311369782', '2023-11-21 18:41:28', '17311369782', '2023-11-21 18:41:28');
