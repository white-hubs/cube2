-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-mdata`.`stall`
    MODIFY COLUMN `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '档位类型' AFTER `name`,
    MODIFY COLUMN `area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '档位区域' AFTER `type`,
    MODIFY COLUMN `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '具体位置' AFTER `area`,
    MODIFY COLUMN `built_up_area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '建筑面积' AFTER `location`,
    MODIFY COLUMN `practical_area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '实用面积' AFTER `built_up_area`,
    MODIFY COLUMN `dept_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '部门主键' AFTER `practical_area`,
    MODIFY COLUMN `lease_type` tinyint(1) NULL DEFAULT 0 COMMENT '出租类型' AFTER `dept_name`,
    MODIFY COLUMN `enabled` tinyint(3) UNSIGNED NULL DEFAULT 0 COMMENT '是否启用' AFTER `remark`;

ALTER TABLE `cube-biz-mdata`.`supplier`
    MODIFY COLUMN `admin_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '管理员手机号' AFTER `admin_user_name`,
    MODIFY COLUMN `liaison` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '联系人' AFTER `admin_phone`,
    MODIFY COLUMN `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '联系人手机号' AFTER `liaison`,
    MODIFY COLUMN `area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所在地区' AFTER `phone`,
    ADD COLUMN `shift_master` varchar(50) NULL COMMENT '档主' AFTER `update_time`,
    ADD COLUMN `shift_master_phone` varchar(50) NULL COMMENT '档主联系电话' AFTER `shift_master`,
    ADD COLUMN `shift_master_card` varchar(50) NULL COMMENT '档主身份证' AFTER `shift_master_phone`,
    ADD COLUMN `other_contacts` varchar(50) NULL COMMENT '其他联系人' AFTER `shift_master_card`;

