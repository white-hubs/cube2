-- liquibase formatted sql
-- changeset qqq:1686103358461-1

-- 字典服务sql
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (5, 'project-merchant-area', '上海二期迁移-区域类型', 0, '1', '2023-08-04 13:33:30', '1', '2023-08-04 13:33:30');
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (6, 'project-merchant-spaceAttr', '上海二期迁移-空间属性', 0, '1', '2023-08-04 13:45:14', '1', '2023-08-04 13:45:14');
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (7, 'project-merchant-spaceTradeModel', '上海二期迁移-经营类型', 0, '1', '2023-08-04 13:48:08', '1', '2023-08-04 13:48:08');
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (8, 'project-merchant-storeType', '上海二期迁移-档位类型', 0, '1', '2023-08-04 15:59:44', '1', '2023-08-04 15:59:44');
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (9, 'project-merchant-vehicleType', '上海二期迁移-车辆类型', 0, '1', '2023-08-07 15:11:04', '1', '2023-08-07 15:11:04');
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (10, 'project-merchant-evaluate_level', '上海二期迁移-评价分类', 0, '1', '2023-08-07 16:05:19', '1', '2023-08-07 16:05:19');
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701534728015151105, 'project-merchant-protocolType', '上海二期迁移-协议类型', 0, '17311369782', '2023-09-12 17:54:27', '17311369782', '2023-09-12 17:54:27');


INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687339349157707777, '档位', 'spaceAttr_booth', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-spaceAttr', 0, '17311369782', '2023-08-04 13:47:05', '17311369782', '2023-08-04 13:47:05', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687339416568561665, '车位', 'spaceAttr_parking', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-spaceAttr', 0, '17311369782', '2023-08-04 13:47:21', '17311369782', '2023-08-04 13:47:21', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687339857918394370, '运营', 'spaceTradeModel_operation', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-spaceTradeModel', 0, '17311369782', '2023-08-04 13:49:06', '17311369782', '2023-08-04 13:49:06', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687339898590560258, '自用', 'spaceTradeModel_personal', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-spaceTradeModel', 0, '17311369782', '2023-08-04 13:49:16', '17311369782', '2023-08-04 13:49:16', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687339956039942146, '公用', 'spaceTradeModel_public', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-spaceTradeModel', 0, '17311369782', '2023-08-04 13:49:29', '17311369782', '2023-08-04 13:49:29', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687373021084745729, '商户档位', 'storeType_fruits', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-storeType', 0, '17311369782', '2023-08-04 16:00:53', '17311369782', '2023-08-04 16:00:53', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688448099989946369, '三轮车', 'vehicleType_tricycle', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-vehicleType', 0, '17311369782', '2023-08-07 15:12:52', '17311369782', '2023-08-07 15:12:52', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688448198920994818, '摩托车', 'vehicleType_motorcycle', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-vehicleType', 0, '17311369782', '2023-08-07 15:13:15', '17311369782', '2023-08-07 15:13:15', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688463780585660417, '差评', 'evaluate_level_bad', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-evaluate_level', 0, '17311369782', '2023-08-07 16:15:10', '17311369782', '2023-08-07 16:15:10', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688463858602442754, '好评', 'evaluate_level_good', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-evaluate_level', 0, '17311369782', '2023-08-07 16:15:29', '17311369782', '2023-08-07 16:15:29', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688463921652686849, '中评', 'evaluate_level_middle', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-evaluate_level', 0, '17311369782', '2023-08-07 16:15:44', '17311369782', '2023-08-07 16:15:44', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688488148694953985, '食品安全', 'protocolType_foodSafety', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-protocolType', 0, '17311369782', '2023-08-07 17:52:00', '17311369782', '2023-08-07 17:52:00', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688488497908510722, '门前三包', 'protocolType_mqsb', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-protocolType', 0, '17311369782', '2023-08-07 17:53:23', '17311369782', '2023-08-07 17:53:23', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688488571908616193, '商户经营', 'questionnaireType_shjy', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-questionnaireType', 0, '17311369782', '2023-08-07 17:53:41', '17311369782', '2023-08-07 17:53:41', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1688488839396159490, '市场管理', 'questionnaireType_scgl', NULL, 58197260, 'v1.0.0', NULL, 'project-merchant-questionnaireType', 0, '17311369782', '2023-08-07 17:54:45', '17311369782', '2023-08-07 17:54:45', 'ENABLE');


INSERT INTO `cube-base-dictionary`.`cascade_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `level`, `superiors_id`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687336862770102273, '楼栋区域', 'areaTypeBuildings', '上海二期迁移-区域类型', 58197260, 'v1.0.0', NULL, 'project-merchant-area', 1, 0, 0, '17311369782', '2023-08-04 13:37:12', '17311369782', '2023-08-04 13:37:12', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`cascade_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `level`, `superiors_id`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687337936008609794, '楼栋', 'areaTypeBuildings_building', '上海二期迁移-区域类型', 58197260, 'v1.0.0', NULL, 'project-merchant-area', 2, 1687336862770102273, 0, '17311369782', '2023-08-04 13:41:28', '17311369782', '2023-08-04 13:41:28', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`cascade_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `level`, `superiors_id`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687338128669769729, '楼层', 'areaTypeBuildings_floor', '上海二期迁移-区域类型', 58197260, 'v1.0.0', NULL, 'project-merchant-area', 3, 1687337936008609794, 0, '17311369782', '2023-08-04 13:42:14', '17311369782', '2023-08-04 13:42:14', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`cascade_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `level`, `superiors_id`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1687338283338924033, '片区', 'areaTypeBuildings_slice', '上海二期迁移-区域类型', 58197260, 'v1.0.0', NULL, 'project-merchant-area', 4, 1687338128669769729, 0, '17311369782', '2023-08-04 13:42:51', '17311369782', '2023-08-04 13:42:51', 'ENABLE');


-- 初始化表


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appeal_description
-- ----------------------------
CREATE TABLE `appeal_description`  (
                                       `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                       `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                                       `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述名称',
                                       `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '诉求类型',
                                       `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
                                       `update_time` datetime NOT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '诉求描述表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for area
-- ----------------------------
CREATE TABLE `area`  (
                         `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                         `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父级区域id',
                         `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目ID',
                         `bim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'BIM模型ID',
                         `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域名称',
                         `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域编码',
                         `acreage` double(10, 2) NULL DEFAULT NULL COMMENT '区域面积',
  `level` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域级别',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域类型（楼栋、室外）',
  `sort` int(4) NOT NULL COMMENT '排序',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `building_area` float(10, 0) NULL DEFAULT 0 COMMENT '建筑面积',
  `cover_area` float(10, 0) NULL DEFAULT 0 COMMENT '占地面积',
  `lease_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁面积',
  `lease_real_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁实际面积',
  `lease_public_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁公摊面积',
  `fire_area` float(10, 0) NULL DEFAULT 0 COMMENT '消防面积',
  `clean_area` float(10, 0) NULL DEFAULT 0 COMMENT '清洁面积',
  `parking_area` float(10, 0) NULL DEFAULT 0 COMMENT '车位面积',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
  `tenant` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '区域表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for assembly_notice
-- ----------------------------
CREATE TABLE `assembly_notice`  (
                                    `id` bigint(20) NOT NULL COMMENT '主键',
                                    `msg_id` bigint(20) NULL DEFAULT NULL COMMENT '消息id',
                                    `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息配置json',
                                    `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
                                    `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
                                    `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
                                    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `tenant` int(11) NULL DEFAULT NULL,
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for assembly_notice_condition
-- ----------------------------
CREATE TABLE `assembly_notice_condition`  (
                                              `id` bigint(20) NOT NULL,
                                              `notice_id` bigint(20) NULL DEFAULT NULL COMMENT '通知id(assembly_notice表id)',
                                              `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知条件类型',
                                              `value` int(11) NULL DEFAULT NULL COMMENT '通知时间',
                                              `msg_id` bigint(20) NULL DEFAULT NULL COMMENT '消息id',
                                              `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
                                              `module_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块类型',
                                              `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
                                              `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
                                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                              `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
                                              `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                              `tenant` int(11) NULL DEFAULT NULL,
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for assembly_notice_user
-- ----------------------------
CREATE TABLE `assembly_notice_user`  (
                                         `id` bigint(20) NOT NULL COMMENT '主键',
                                         `notice_id` bigint(20) NOT NULL COMMENT '消息配置id',
                                         `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
                                         `type_ids` bigint(20) NOT NULL COMMENT '类型id',
                                         `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知人员类型',
                                         `condition_id` bigint(20) NULL DEFAULT NULL COMMENT '条件id',
                                         `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
                                         `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
                                         `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         `tenant` int(11) NULL DEFAULT NULL,
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for company
-- ----------------------------
CREATE TABLE `company`  (
                            `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                            `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                            `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商铺名称',
                            `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商铺编号',
                            `purpose` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商铺用途',
                            `owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属人姓名',
                            `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属人电话',
                            `start_time` date NULL DEFAULT NULL COMMENT '开始时间',
                            `end_time` date NULL DEFAULT NULL COMMENT '结束时间',
                            `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                            `introduction` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户经营信息简介',
                            `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                            `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                            `deleted` int(1) NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
                            `security_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安全协议',
                            `training_photo_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安全培训照片',
                            `company_photo_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户灭火器/消防栓',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `pid`(`project_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商铺表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for company_user
-- ----------------------------
CREATE TABLE `company_user`  (
                                 `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主键',
                                 `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '项目ID',
                                 `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户ID',
                                 `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
                                 `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
                                 `owner` int(1) NOT NULL DEFAULT 0 COMMENT '是否档主',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商铺-用户关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device
-- ----------------------------
CREATE TABLE `device`  (
                           `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                           `area_id3` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '三级区域ID',
                           `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                           `area_id1` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '一级区域ID',
                           `area_id2` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '二级区域ID',
                           `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型ID',
                           `model` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备型号',
                           `iot_device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联网设备ID',
                           `iot_device_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联网设备类型',
                           `bim_model_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'BIM模型ID',
                           `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
                           `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称 ',
                           `pinyin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '首字母',
                           `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态（正常、故障、报废 ...）',
                           `online` int(1) NOT NULL DEFAULT 0 COMMENT '在离线状态',
                           `manual_maint` date NULL DEFAULT NULL COMMENT '手动维护数据日期',
                           `price` double(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `install_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装地址',
  `install_date` date NULL DEFAULT NULL COMMENT '安装时间',
  `start_date` date NULL DEFAULT NULL COMMENT '启用时间',
  `expire_date` date NULL DEFAULT NULL COMMENT '到期时间',
  `production_date` date NULL DEFAULT NULL COMMENT '生产时间',
  `time_limit` int(10) NULL DEFAULT NULL COMMENT '有效期',
  `maint_cycle` int(11) NULL DEFAULT 0 COMMENT '维保周期（天）',
  `maint_date` datetime NULL DEFAULT NULL COMMENT '上次维保时间',
  `manufacturer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产厂家',
  `specification` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `dept_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理部门ID',
  `contact_person` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人员',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `contact_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系地址',
  `qr_code` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码图',
  `gis_longitude` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gis经度',
  `gis_latitude` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gis纬度',
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备登录账号',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备登录密码',
  `ip_addr` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `port` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '端口',
  `videobox_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联视频分析盒子ID',
  `token` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'token信息',
  `finance_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财政编码',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `nearby_cams` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附近摄像头',
  `visibility` int(1) NULL DEFAULT 0 COMMENT '可见性（摄像头设备针对外部市场人员）',
  `expire_change` tinyint(1) NULL DEFAULT NULL COMMENT 'true设备到期更换过',
  `monitoring_object` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控对象',
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0代表存在，1代表已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for device_elevator_property
-- ----------------------------
CREATE TABLE `device_elevator_property`  (
                                             `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                             `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
                                             `floor_num` int(11) NOT NULL DEFAULT 0 COMMENT '层数',
                                             `station_num` int(11) NOT NULL DEFAULT 0 COMMENT '站数',
                                             `door_num` int(11) NOT NULL DEFAULT 0 COMMENT '门数',
                                             `load` int(11) NOT NULL DEFAULT 0 COMMENT '载重',
                                             `speed` float NOT NULL DEFAULT 0 COMMENT '速度',
                                             `year_inspect_date` datetime NULL DEFAULT NULL COMMENT '年检日期',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电梯设备特有属性' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_logs
-- ----------------------------
CREATE TABLE `device_logs`  (
                                `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
                                `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目id',
                                `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
                                `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
                                `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                                `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                                `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
                                `dept_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
                                `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名',
                                `time` datetime NOT NULL COMMENT '时间',
                                `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备注',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备修改日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_notice
-- ----------------------------
CREATE TABLE `device_notice`  (
                                  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知ID',
                                  `business_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型',
                                  `days` int(10) NOT NULL COMMENT '还有多少天到期通知，负数就表示过期多少天',
                                  `site` int(1) NOT NULL DEFAULT 0 COMMENT '站内信',
                                  `sms` int(1) NOT NULL DEFAULT 0 COMMENT '短信',
                                  `wx` int(1) NOT NULL DEFAULT 0 COMMENT '微信',
                                  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                  `create_time` datetime NULL DEFAULT NULL,
                                  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                  `update_time` datetime NULL DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备相关通知（目前只有设备到期）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_notice_config
-- ----------------------------
CREATE TABLE `device_notice_config`  (
                                         `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                         `notice_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知ID',
                                         `notice_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知对象类型（用户、部门、岗位、部门岗位）',
                                         `user_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的用户IDS',
                                         `dept_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的部门IDS',
                                         `post_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的岗位IDS',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备相关通知人员配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_online_record
-- ----------------------------
CREATE TABLE `device_online_record`  (
                                         `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                         `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                         `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
                                         `online` tinyint(1) NOT NULL COMMENT '上线/离线',
                                         `date` datetime NOT NULL COMMENT '时间',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备上下线记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_threshold_value
-- ----------------------------
CREATE TABLE `device_threshold_value`  (
                                           `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                           `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                           `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '阈值模板名称',
                                           `device_id_str` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '设备IDS',
                                           `dygxsz` int(10) NULL DEFAULT NULL COMMENT '电压高限',
                                           `dydxsz` int(10) NULL DEFAULT NULL COMMENT '电压低限',
                                           `dlgxsz` int(10) NULL DEFAULT NULL COMMENT '电流高限',
                                           `glgxsz` int(10) NULL DEFAULT NULL COMMENT '功率高限',
                                           `ldlgxsz` int(10) NULL DEFAULT NULL COMMENT '漏电流高限',
                                           `wdgxsz` int(10) NULL DEFAULT NULL COMMENT '温度高限',
                                           `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                           `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                           `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                           `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备阈值' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_type
-- ----------------------------
CREATE TABLE `device_type`  (
                                `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '父ID',
                                `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代码',
                                `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                `validity_months` int(10) NULL DEFAULT NULL COMMENT '有效期（月）',
                                `sort` int(4) NOT NULL COMMENT '排序',
                                `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备类型' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for device_videobox
-- ----------------------------
CREATE TABLE `device_videobox`  (
                                    `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                    `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                    `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型外键(数据字典)',
                                    `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
                                    `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称 ',
                                    `production_date` date NULL DEFAULT NULL COMMENT '生产时间',
                                    `manufacturer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产厂家',
                                    `specification` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格型号',
                                    `contact_person` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人员',
                                    `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
                                    `contact_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系地址',
                                    `live_action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实时场景',
                                    `purchase_contract` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购合同',
                                    `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                    `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0代表存在，1代表已删除)',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备（智能视频分析盒）' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for envmodel_config
-- ----------------------------
CREATE TABLE `envmodel_config`  (
                                    `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                    `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                    `x` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    `y` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    `z` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    `angle` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '旋转角度',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '环境模型配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for faq
-- ----------------------------
CREATE TABLE `faq`  (
                        `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                        `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                        `question` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '常见问题',
                        `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                        `answer` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '答案',
                        `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                        `create_day` date NULL DEFAULT NULL COMMENT '创建天',
                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                        `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                        `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                        `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '常见问题解答' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for faq_copy1
-- ----------------------------
CREATE TABLE `faq_copy1`  (
                              `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                              `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                              `question` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '常见问题',
                              `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                              `answer` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '答案',
                              `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                              `create_day` date NULL DEFAULT NULL COMMENT '创建天',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                              `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '常见问题解答' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for fireinsp_item_evaluation
-- ----------------------------
CREATE TABLE `fireinsp_item_evaluation`  (
                                             `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                             `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目ID',
                                             `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                                             `standard_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标准ID',
                                             `standard_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标准名',
                                             `item_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项ID',
                                             `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项名称',
                                             `item_result_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项结果编号',
                                             `item_result_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项结果名',
                                             `item_result_mark` int(3) NOT NULL DEFAULT 0 COMMENT '项结果扣分',
                                             `enabled` int(1) NOT NULL DEFAULT 0 COMMENT '启停状态',
                                             `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                             `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                             `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检项评价配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for firm_manage
-- ----------------------------
CREATE TABLE `firm_manage`  (
                                `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的项目ID',
                                `project_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的项目名',
                                `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名称',
                                `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '填报url地址',
                                `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                `create_time` datetime NOT NULL COMMENT '创建时间',
                                `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
                                `update_time` datetime NOT NULL COMMENT '更新时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公司管理-用于安全月报' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for history_area
-- ----------------------------
CREATE TABLE `history_area`  (
                                 `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                 `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                 `object_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对象类型(区域、空间)',
                                 `object_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对象ID',
                                 `building_area` float(10, 0) NULL DEFAULT 0 COMMENT '建筑面积',
  `cover_area` float(10, 0) NULL DEFAULT 0 COMMENT '占地面积',
  `lease_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁面积',
  `lease_real_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁实际面积',
  `lease_public_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁公摊面积',
  `fire_area` float(10, 0) NULL DEFAULT 0 COMMENT '消防面积',
  `clean_area` float(10, 0) NULL DEFAULT 0 COMMENT '清洁面积',
  `parking_area` float(10, 0) NULL DEFAULT 0 COMMENT '车位面积',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '空间、区域历史面积' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for holiday_config
-- ----------------------------
CREATE TABLE `holiday_config`  (
                                   `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                   `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节假日名称',
                                   `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目ID',
                                   `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节假日备注',
                                   `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                   `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                   `deleted` int(1) NOT NULL COMMENT '删除标志（0代表存在，1代表删除）',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节假日或非工作日配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for holiday_date_config
-- ----------------------------
CREATE TABLE `holiday_date_config`  (
                                        `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `holiday_config_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节假日配置id',
                                        `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目id',
                                        `holiday_time` date NULL DEFAULT NULL COMMENT '假日时间',
                                        `holiday_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假日类型,节假日或非工作日(数据字典)',
                                        `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                        `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                        `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                        `deleted` int(1) NOT NULL COMMENT '删除标志（0代表存在，1代表删除）',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节假日关联日期' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for imported_goods
-- ----------------------------
CREATE TABLE `imported_goods`  (
                                   `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID',
                                   `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目ID',
                                   `imported_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '进口ID',
                                   `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '档位ID',
                                   `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户ID',
                                   `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户名',
                                   `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
                                   `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品类型',
                                   `supplier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商',
                                   `quantity` int(11) NOT NULL DEFAULT 0 COMMENT '数量',
                                   `unit` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '单位',
                                   `producing_area` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产地',
                                   `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
                                   `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '检查状态',
                                   `result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结果',
                                   `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '进口商品' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for iot_config
-- ----------------------------
CREATE TABLE `iot_config`  (
                               `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                               `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                               `iot_project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'iot平台项目ID',
                               `platform_site` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '平台地址Api访问',
                               `platform_site_web` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '平台前端跳转地址',
                               `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
                               `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录密码',
                               `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物联网配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for knowledge
-- ----------------------------
CREATE TABLE `knowledge`  (
                              `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID',
                              `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目ID',
                              `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
                              `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '编号',
                              `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型',
                              `device_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备类型ID',
                              `device_ids` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备最多10个',
                              `description` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '描述',
                              `remark` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
                              `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
                              `create_time` datetime NOT NULL COMMENT '创建时间',
                              `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '更新人',
                              `update_time` datetime NOT NULL COMMENT '更新时间',
                              `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0代表存在，1代表已删除)',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识库表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for project
-- ----------------------------
CREATE TABLE `project`  (
                            `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                            `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
                            `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                            `abbr` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简称',
                            `pinyin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '首字母',
                            `longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '经度',
                            `latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '纬度',
                            `enabled` int(1) NOT NULL COMMENT '是否启用',
                            `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
                            `province_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '省级区域',
                            `city_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '市级区域',
                            `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '市场地址',
                            `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目URL地址',
                            `api` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目后端api地址',
                            `launch_date` date NULL DEFAULT NULL COMMENT '上线日期',
                            `launched` int(1) NOT NULL DEFAULT 0 COMMENT '是否已上线',
                            `introduction` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
                            `sort` int(4) NOT NULL DEFAULT 0 COMMENT '排序',
                            `figures` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目图片路径',
                            `acreage` int(10) NULL DEFAULT NULL COMMENT '面积',
                            `copyright` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版权信息',
                            `model` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模式（集贸、农批）',
                            `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
                            `create_time` datetime NOT NULL COMMENT '创建时间',
                            `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新人',
                            `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                            `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0存在，1已删除)',
                            `tenant` int(11) NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for project_user
-- ----------------------------
CREATE TABLE `project_user`  (
                                 `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                 `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                 `manager` int(1) NOT NULL DEFAULT 0,
                                 PRIMARY KEY (`project_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目和用户中间表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for purchaser
-- ----------------------------
CREATE TABLE `purchaser`  (
                              `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                              `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名称',
                              `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                              `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
                              `pinyin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '拼音首字母',
                              `gender` int(1) NOT NULL DEFAULT 1 COMMENT '性别（男：1，女：2，未知：9）',
                              `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
                              `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像路径',
                              `enabled` int(1) NOT NULL DEFAULT 1 COMMENT '状态（启用，停用）',
                              `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'userType_staff' COMMENT '用户类型',
                              `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                              `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                              `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在，1代表删除）',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户端-采购商' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for resouce
-- ----------------------------
CREATE TABLE `resouce`  (
                            `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                            `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父ID',
                            `group` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组',
                            `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源类型：MENU、FUNCTION等',
                            `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源代码',
                            `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
                            `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'MENU的图标',
                            `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'MENU的path',
                            `apis` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'MENU需要使用的api',
                            `sort` int(4) NOT NULL COMMENT '排序',
                            `enabled` int(1) NOT NULL COMMENT '是否启用',
                            `identity_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `pid`(`pid`) USING BTREE,
                            INDEX `sort`(`sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for sensitive_word
-- ----------------------------
CREATE TABLE `sensitive_word`  (
                                   `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                   `order` int(11) NULL DEFAULT 0 COMMENT '排序',
                                   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '敏感词库' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for space
-- ----------------------------
CREATE TABLE `space`  (
                          `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                          `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                          `area_id1` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '所属一级区域ID',
                          `area_id2` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '所属二级区域ID',
                          `area_id3` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '所属三级区域ID',
                          `bim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'BIM模型ID',
                          `dept_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理部门ID',
                          `dept_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理部门名称',
                          `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '商户ID',
                          `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户名称',
                          `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
                          `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                          `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位类型',
                          `attribute` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '属性',
                          `trade_model` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '经营类型',
                          `acreage` double(10, 2) NOT NULL DEFAULT 0.00 COMMENT '空间面积',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `protocol_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协议ID（档位需要签订的协议，2022/1/10王量）',
  `building_area` float(10, 0) NULL DEFAULT NULL COMMENT '建筑面积',
  `cover_area` float(10, 0) NULL DEFAULT NULL COMMENT '占地面积',
  `lease_area` float(10, 0) NULL DEFAULT NULL COMMENT '租赁面积',
  `lease_real_area` float(10, 0) NULL DEFAULT NULL COMMENT '租赁实际面积',
  `lease_public_area` float(10, 0) NULL DEFAULT NULL COMMENT '租赁公摊面积',
  `fire_area` float(10, 0) NULL DEFAULT NULL COMMENT '消防面积',
  `clean_area` float(10, 0) NULL DEFAULT NULL COMMENT '清洁面积',
  `parking_area` float(10, 0) NULL DEFAULT NULL COMMENT '车位面积',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
  `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `company`(`company_id`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '空间表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for spare
-- ----------------------------
CREATE TABLE `spare`  (
                          `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                          `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                          `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                          `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                          `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型ID',
                          `type_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型Code',
                          `factory` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '厂家',
                          `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '型号',
                          `num` int(11) NOT NULL DEFAULT 0 COMMENT '库存 数量',
                          `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计量单位',
                          `price` decimal(32, 2) NOT NULL DEFAULT 0.00 COMMENT '价格',
                          `estimate_low` int(11) NOT NULL DEFAULT 0 COMMENT '数量过低预警值 库存最小',
                          `estimate_high` int(11) NOT NULL DEFAULT 0 COMMENT '数量过高预警值 库存最大',
                          `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
                          `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '地址',
                          `time` datetime NULL DEFAULT NULL COMMENT '添加时间',
                          `has_details` int(1) NOT NULL DEFAULT 0 COMMENT '是否有详情',
                          `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态',
                          `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                          `create_time` datetime NOT NULL COMMENT '创建时间',
                          `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
                          `update_time` datetime NOT NULL COMMENT '更新时间',
                          `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备件信息' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for spare_device
-- ----------------------------
CREATE TABLE `spare_device`  (
                                 `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                                 `spare_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备件ID',
                                 `device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备ID',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备件关联设备表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for spare_type
-- ----------------------------
CREATE TABLE `spare_type`  (
                               `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                               `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '父ID',
                               `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                               `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
                               `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                               `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                               `sort` int(1) NOT NULL DEFAULT 0 COMMENT '排序',
                               `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                               `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                               `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                               `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备品备件类型' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for spare_use
-- ----------------------------
CREATE TABLE `spare_use`  (
                              `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                              `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                              `miant_firm` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '维保公司',
                              `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
                              `device_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
                              `device_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型ID',
                              `device_type_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型code',
                              `device_type_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型名称',
                              `area_id1` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '一级区域',
                              `area_id2` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '二级区域',
                              `area_id3` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '三级区域',
                              `install_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '安装位置',
                              `purpose` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用途',
                              `contact_person` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系人',
                              `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系电话',
                              `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务类型',
                              `business_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务ID',
                              `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
                              `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                              `create_time` datetime NOT NULL COMMENT '创建时间',
                              `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
                              `update_time` datetime NOT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备品备件使用记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for spare_use_spare
-- ----------------------------
CREATE TABLE `spare_use_spare`  (
                                    `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID ',
                                    `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                    `spare_use_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件使用记录ID',
                                    `spare_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件ID',
                                    `spare_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件名称',
                                    `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件类型ID',
                                    `type_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件类型Code',
                                    `type_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件类型名称',
                                    `factory` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备件厂家',
                                    `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备件型号',
                                    `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计量单位',
                                    `price` decimal(32, 2) NOT NULL DEFAULT 0.00 COMMENT '价格',
                                    `use_num` int(11) NOT NULL DEFAULT 0 COMMENT '使用数量',
                                    `time` datetime NULL DEFAULT NULL COMMENT '时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备品备件使用记录中使用的备件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stall_evaluation
-- ----------------------------
CREATE TABLE `stall_evaluation`  (
                                     `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                     `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                     `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
                                     `total` int(11) NOT NULL DEFAULT 0 COMMENT '评价次数',
                                     `z_score` int(11) NOT NULL DEFAULT 0 COMMENT '商品质量总评分',
                                     `z_score_avg` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '商品质量平均分',
  `s_score` int(11) NOT NULL DEFAULT 0 COMMENT '诚实守信总评分',
  `s_score_avg` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '诚实守信平均分',
  `t_score` int(11) NOT NULL DEFAULT 0 COMMENT '服务态度总评分',
  `t_score_avg` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '服务态度平均分',
  `w_score` int(11) NOT NULL DEFAULT 0 COMMENT '环境卫生总评分',
  `w_score_avg` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '环境卫生平均分',
  `total_score` int(11) NOT NULL COMMENT '总得分',
  `honesty_score` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '诚信经营值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位评价总表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stall_evaluation_ratio
-- ----------------------------
CREATE TABLE `stall_evaluation_ratio`  (
                                           `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                           `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                           `z_ratio` float(3, 2) NOT NULL DEFAULT 0.00 COMMENT '商品质量权重占比',
  `s_ratio` float(3, 2) NOT NULL DEFAULT 0.00 COMMENT '诚实守信权重占比',
  `t_ratio` float(3, 2) NOT NULL DEFAULT 0.00 COMMENT '服务态度权重占比',
  `w_ratio` float(3, 2) NOT NULL DEFAULT 0.00 COMMENT '环境卫生权重占比',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位评价维度权重占比' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stall_evaluation_record
-- ----------------------------
CREATE TABLE `stall_evaluation_record`  (
                                            `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                            `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录编号',
                                            `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                            `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
                                            `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价人ID',
                                            `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
                                            `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价人名',
                                            `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价人昵称',
                                            `z_score` int(1) NOT NULL DEFAULT 0 COMMENT '商品质量评分',
                                            `s_score` int(1) NOT NULL DEFAULT 0 COMMENT '诚实守信评分',
                                            `t_score` int(1) NOT NULL DEFAULT 0 COMMENT '服务态度评分',
                                            `w_score` int(1) NOT NULL DEFAULT 0 COMMENT '环境卫生评分',
                                            `score` float(2, 1) NOT NULL COMMENT '综合分',
  `honesty_score` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '诚信经营值',
  `descr` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `has_file` int(1) NULL DEFAULT 0 COMMENT '是否有图',
  `tags` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价标签',
  `examine_status` int(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0:未审核，1:已通过，2:未通过）',
  `examine_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `examine_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位评价记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stall_evaluation_record_tag
-- ----------------------------
CREATE TABLE `stall_evaluation_record_tag`  (
                                                `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                                `tag_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签ID',
                                                `record_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录ID',
                                                `tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
                                                `evaluation` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价维度',
                                                `time` datetime NULL DEFAULT NULL COMMENT '时间',
                                                `examine_status` int(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0:未审核，1:已通过，2:未通过）',
                                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评价记录关联标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stall_evaluation_tag
-- ----------------------------
CREATE TABLE `stall_evaluation_tag`  (
                                         `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                         `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                         `tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评价标签',
                                         `evaluation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评价维度',
                                         `category` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评价分类',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位评价标签' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stall_safety_insp
-- ----------------------------
CREATE TABLE `stall_safety_insp`  (
                                      `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                      `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
                                      `task_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名',
                                      `record_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录ID',
                                      `record_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录编号',
                                      `record_exist` int(1) NOT NULL DEFAULT 1 COMMENT '巡检记录是否存在',
                                      `standard_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准名',
                                      `item_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项ID',
                                      `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项名称',
                                      `item_result_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项结果名',
                                      `item_result_mark` int(3) NOT NULL DEFAULT 0 COMMENT '项结果扣分',
                                      `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                      `time` datetime NULL DEFAULT NULL COMMENT '巡检时间',
                                      `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检人ID',
                                      `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检人名',
                                      `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检人昵称',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位安全生产巡检扣分记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stall_safety_mark
-- ----------------------------
CREATE TABLE `stall_safety_mark`  (
                                      `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                      `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
                                      `year` int(11) NOT NULL DEFAULT 0 COMMENT '年份',
                                      `score` int(6) NOT NULL DEFAULT 0 COMMENT '分数',
                                      `insp_total` int(11) NOT NULL DEFAULT 0 COMMENT '扣分巡检项数',
                                      `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位安全生产值' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stall_safety_mark_logs
-- ----------------------------
CREATE TABLE `stall_safety_mark_logs`  (
                                           `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                           `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
                                           `point` int(11) NOT NULL DEFAULT 0 COMMENT '调整分数（加|减）',
                                           `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
                                           `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                           `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位安全生产分值修改记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stream_config
-- ----------------------------
CREATE TABLE `stream_config`  (
                                  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                                  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                                  `host` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机',
                                  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'IP',
                                  `port` int(5) NULL DEFAULT 0 COMMENT '端口',
                                  `rtmp_port` int(5) NULL DEFAULT 0 COMMENT '公网rtmp端口',
                                  `lan_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '局域网内网IP',
                                  `lan_port` int(5) NULL DEFAULT 0 COMMENT '局域网内网端口',
                                  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
                                  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流媒体服务配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE `user`  (
                         `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                         `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
                         `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                         `is_pwd_random` int(1) NOT NULL DEFAULT 0 COMMENT '是否是随机密码',
                         `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
                         `pinyin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '拼音首字母',
                         `gender` int(1) NOT NULL DEFAULT 1 COMMENT '性别（男：1，女：2，未知：9）',
                         `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号码',
                         `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                         `identity_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
                         `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像路径',
                         `enabled` int(1) NOT NULL DEFAULT 1 COMMENT '状态（启用，停用）',
                         `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'userType_staff' COMMENT '用户类型',
                         `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                         `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录IP',
                         `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
                         `theme` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题肤色',
                         `is_external` int(1) NULL DEFAULT 0 COMMENT '是否来自外部系统',
                         `source` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '来自哪里',
                         `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                         `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                         `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                         `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在，1代表删除）',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for user_device
-- ----------------------------
CREATE TABLE `user_device`  (
                                `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                                `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '项目ID',
                                `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务类型',
                                `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户ID',
                                `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '设备ID',
                                `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
                                `create_time` datetime NOT NULL COMMENT '创建时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户设备表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for user_link
-- ----------------------------
CREATE TABLE `user_link`  (
                              `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                              `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
                              `wxopenid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信openid',
                              `wxunionid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信unionid',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户绑定微信openid' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
CREATE TABLE `warehouse`  (
                              `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                              `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                              `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                              `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                              `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_auth
-- ----------------------------
CREATE TABLE `wholesaler_auth`  (
                                    `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
                                    `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
                                    `project_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
                                    `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                    `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商铺ID',
                                    `company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户名称',
                                    `enterprise_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业名称',
                                    `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
                                    `identity_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证',
                                    `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话',
                                    `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
                                    `post_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政',
                                    `registration_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册工商号',
                                    `status` int(1) NOT NULL DEFAULT 0 COMMENT '审核状态 0未审核，1审核通过，2驳回',
                                    `login_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号，user.username',
                                    `reviewer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
                                    `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
                                    `reject_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '驳回原因',
                                    `stall_ids` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联的档位IDS',
                                    `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请人',
                                    `create_time` datetime NOT NULL COMMENT '申请时间',
                                    `personnel_pic_id` bigint(20) NULL DEFAULT NULL COMMENT '身份证人像面照片ID',
                                    `id_card_photo_id` bigint(20) NULL DEFAULT NULL COMMENT '身份证人像面照片DTO',
                                    `id_card_nation_id` bigint(20) NULL DEFAULT NULL COMMENT '身份证国徽面照片ID',
                                    `license_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照ID',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE INDEX `userId`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '实名认证信息表，' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_contract
-- ----------------------------
CREATE TABLE `wholesaler_contract`  (
                                        `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
                                        `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                        `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同编号',
                                        `signing_way` int(1) NOT NULL COMMENT '签约方式（0新签约，1续签）',
                                        `contract_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同类型ID',
                                        `status` int(1) NOT NULL DEFAULT 0 COMMENT '合同状态（0草稿 1待签订 2 乙方签约 3签章（生效））',
                                        `begin_time` date NOT NULL COMMENT '租赁日期始',
                                        `end_time` date NOT NULL COMMENT '租赁日期终',
                                        `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
                                        `signatory_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签约人ID',
                                        `signatory_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '甲方签约人名字',
                                        `signatory_time` datetime NULL DEFAULT NULL COMMENT '甲方签约时间',
                                        `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                        `create_time` datetime NULL DEFAULT NULL,
                                        `second_part_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '乙方ID',
                                        `paper_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纸质合同编号',
                                        `old_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原合同ID',
                                        `old_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原合同编号',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户合同表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_contract_type
-- ----------------------------
CREATE TABLE `wholesaler_contract_type`  (
                                             `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
                                             `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                             `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
                                             `enabled` int(1) NOT NULL COMMENT '是否启用（1启用，0没有启用）',
                                             `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同类型名称',
                                             `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                             `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户合同类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_device
-- ----------------------------
CREATE TABLE `wholesaler_device`  (
                                      `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT 'id',
                                      `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                      `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型ID',
                                      `count` int(11) NOT NULL COMMENT '设备数量',
                                      `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所在档位ID',
                                      `stall_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位名称',
                                      `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属商户ID',
                                      `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户名称',
                                      `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备描述',
                                      `danger` int(1) NOT NULL COMMENT '是否危险物品',
                                      `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                      `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                      `deleted` int(1) NOT NULL COMMENT '删除标识',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户档案-所有设备（批发商的设备）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_device_type
-- ----------------------------
CREATE TABLE `wholesaler_device_type`  (
                                           `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                           `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父级ID',
                                           `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                                           `is_danger` int(1) NOT NULL DEFAULT 0 COMMENT '是否是危险源',
                                           `sort` int(2) NOT NULL DEFAULT 0 COMMENT '排序',
                                           `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
                                           `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                           `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                           `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                           `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0代表存在，1代表已删除)',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户设备类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_first_part
-- ----------------------------
CREATE TABLE `wholesaler_first_part`  (
                                          `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
                                          `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                          `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '甲方名称',
                                          `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
                                          `post_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
                                          `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
                                          `seal_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子章',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '甲方信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_notice
-- ----------------------------
CREATE TABLE `wholesaler_notice`  (
                                      `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
                                      `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                      `business_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型',
                                      `notice_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知对象类型（用户、部门、岗位、部门岗位）',
                                      `user_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的用户IDS',
                                      `dept_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的部门IDS',
                                      `post_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的岗位IDS',
                                      `site` int(1) NOT NULL DEFAULT 0 COMMENT '站内信',
                                      `sms` int(1) NOT NULL DEFAULT 0 COMMENT '短信',
                                      `wx` int(1) NOT NULL DEFAULT 0 COMMENT '微信',
                                      `pop` int(1) NOT NULL DEFAULT 0 COMMENT '弹窗',
                                      `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                      `create_time` datetime NULL DEFAULT NULL,
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户相关功能通知市场方（目前合同、认证）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_protocol
-- ----------------------------
CREATE TABLE `wholesaler_protocol`  (
                                        `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                        `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                                        `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
                                        `template_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `template_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `template_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                        `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                        `begin_time` datetime NOT NULL,
                                        `end_time` datetime NOT NULL,
                                        `processing_status` int(1) NOT NULL DEFAULT 0 COMMENT '处理是否完成',
                                        `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '有效状态：未生效、有效、失效',
                                        `xxl_task_ids` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'xxl的任务IDS',
                                        `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                        `create_time` datetime NULL DEFAULT NULL,
                                        `deleted` int(1) NOT NULL DEFAULT 0,
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户协议' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_protocol_template
-- ----------------------------
CREATE TABLE `wholesaler_protocol_template`  (
                                                 `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                                                 `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目id',
                                                 `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
                                                 `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
                                                 `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板类型',
                                                 `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                 `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                                 `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                 `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户协议模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_question
-- ----------------------------
CREATE TABLE `wholesaler_question`  (
                                        `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主键ID',
                                        `questionnaire_temp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷模板ID',
                                        `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题类型',
                                        `number` int(2) NOT NULL COMMENT '题号',
                                        `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题干',
                                        `options` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选项',
                                        `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户问卷-问卷上的题目' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_questionnaire
-- ----------------------------
CREATE TABLE `wholesaler_questionnaire`  (
                                             `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '主键ID',
                                             `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                             `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
                                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷名称',
                                             `temp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板id',
                                             `temp_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板类型',
                                             `temp_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板类型',
                                             `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态（是否有效）',
                                             `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '描述',
                                             `begin_time` datetime NOT NULL COMMENT '开始时间',
                                             `end_time` datetime NOT NULL COMMENT '结束时间',
                                             `xxl_task_ids` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'xxl的任务IDS',
                                             `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                             `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '问卷调查' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_questionnaire_temp
-- ----------------------------
CREATE TABLE `wholesaler_questionnaire_temp`  (
                                                  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
                                                  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                                  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                                                  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷模板名称',
                                                  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷模板类型',
                                                  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                                  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
                                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户问卷模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_relationship
-- ----------------------------
CREATE TABLE `wholesaler_relationship`  (
                                            `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                            `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型，可能是问卷类型，可能是协议类型',
                                            `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
                                            `business_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务ID，可能是问卷ID可能是协议ID',
                                            `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态（是否签约，做题）',
                                            `signed_who` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签约（做题）人',
                                            `sigend_time` datetime NULL DEFAULT NULL COMMENT '签约（做题）时间',
                                            `owner` int(1) NULL DEFAULT NULL COMMENT '1档主0非档主',
                                            `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                            `create_time` datetime NULL DEFAULT NULL,
                                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户中间关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_response
-- ----------------------------
CREATE TABLE `wholesaler_response`  (
                                        `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位id',
                                        `questionnaire_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷ID',
                                        `temp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板ID',
                                        `question_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题iD',
                                        `answer` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '回答',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '问卷调查结果' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_second_part
-- ----------------------------
CREATE TABLE `wholesaler_second_part`  (
                                           `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
                                           `contract_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对应的合同ID',
                                           `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '乙方名称',
                                           `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
                                           `post_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮编',
                                           `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话',
                                           `company_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业机构代码',
                                           `legal_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '法人',
                                           `identity_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '法人身份证',
                                           `signatory` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签约人',
                                           `signatory_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签约人号码',
                                           `status` int(1) NOT NULL DEFAULT 0 COMMENT '0是没有通过验证，1是通过了',
                                           `create_time` datetime NULL DEFAULT NULL,
                                           `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '乙方信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_signatory
-- ----------------------------
CREATE TABLE `wholesaler_signatory`  (
                                         `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
                                         `first_part_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '甲方ID',
                                         `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签约人ID',
                                         `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签约人姓名',
                                         `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                         `seal_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子章',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '甲方签约人信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wholesaler_vehicle
-- ----------------------------
CREATE TABLE `wholesaler_vehicle`  (
                                       `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
                                       `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
                                       `vehicle_plate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号（三轮车无车牌）',
                                       `vehicle_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                                       `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '颜色',
                                       `owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属人姓名',
                                       `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属人电话（巡逻的人要根据电话找人）',
                                       `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位',
                                       `stall_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位名称',
                                       `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户',
                                       `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户名称',
                                       `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                       `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户档案-所有车辆（批发商交通工具表）' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

-- 菜单
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'MerchantManagement', `name` = '商户档案', `icon` = '1685852414488707074', `path` = '/portal/wholesaler/merchant-archives', `sort` = 1, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685852423846199297-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 11:18:34', `update_by` = 'admin', `update_time` = '2023-09-11 15:35:41' WHERE `id` = 1685852423846199297;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'shba', `name` = '商户备案', `icon` = '1685921008023076865', `path` = '', `sort` = 2, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685921016126472193-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 15:51:07', `update_by` = 'admin', `update_time` = '2023-07-31 15:52:15' WHERE `id` = 1685921016126472193;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'ryba', `name` = '人员备案', `icon` = '1685921193054797826', `path` = '/portal/wholesaler/staff-record', `sort` = 1, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685921016126472193, `sequence` = '1685921016126472193-1685921198167654402-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 15:51:51', `update_by` = 'admin', `update_time` = '2023-09-11 15:35:51' WHERE `id` = 1685921198167654402;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'clba', `name` = '车辆备案', `icon` = '1685921640306016257', `path` = '/portal/wholesaler/vehicle-record', `sort` = 2, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685921016126472193, `sequence` = '1685921016126472193-1685921653597765633-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 15:53:40', `update_by` = 'admin', `update_time` = '2023-09-11 15:35:57' WHERE `id` = 1685921653597765633;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'sbba', `name` = '设备备案', `icon` = '1685921790407573505', `path` = '/portal/wholesaler/device-record', `sort` = 3, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685921016126472193, `sequence` = '1685921016126472193-1685921799517601793-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 15:54:15', `update_by` = 'admin', `update_time` = '2023-09-11 15:36:02' WHERE `id` = 1685921799517601793;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'shyx', `name` = '商户协议', `icon` = '1685921954954313729', `path` = '', `sort` = 3, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685921965628817409-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 15:54:54', `update_by` = 'admin', `update_time` = '2023-07-31 15:57:51' WHERE `id` = 1685921965628817409;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'xymb', `name` = '协议模板', `icon` = '1685923884308987905', `path` = '/portal/wholesaler/agreement-mould', `sort` = 1, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685921965628817409, `sequence` = '1685921965628817409-1685923906727542786-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:02:37', `update_by` = 'admin', `update_time` = '2023-09-11 15:36:10' WHERE `id` = 1685923906727542786;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'xyqd', `name` = '协议签订', `icon` = '1685924073153331201', `path` = '/portal/wholesaler/agreement-sign', `sort` = 2, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685921965628817409, `sequence` = '1685921965628817409-1685924080690495490-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:03:18', `update_by` = 'admin', `update_time` = '2023-09-11 15:36:15' WHERE `id` = 1685924080690495490;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'shwj', `name` = '商户问卷', `icon` = '1685924617032925186', `path` = NULL, `sort` = 4, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685924625752883201-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:05:28', `update_by` = 'admin', `update_time` = '2023-07-31 16:05:28' WHERE `id` = 1685924625752883201;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'wjmb', `name` = '问卷模板', `icon` = '1685925254344835073', `path` = '/portal/wholesaler/questionnaire-temp', `sort` = 1, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685924625752883201, `sequence` = '1685924625752883201-1685925261168967681-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:08:00', `update_by` = 'admin', `update_time` = '2023-09-11 15:36:27' WHERE `id` = 1685925261168967681;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'wjdc', `name` = '问卷调查', `icon` = '1685925351593967617', `path` = '/portal/wholesaler/questionnaire-examine', `sort` = 2, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685924625752883201, `sequence` = '1685924625752883201-1685925358313242625-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:08:23', `update_by` = 'admin', `update_time` = '2023-09-11 15:36:32' WHERE `id` = 1685925358313242625;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'htgl', `name` = '合同管理', `icon` = '1685925932404408321', `path` = NULL, `sort` = 5, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685925942017753089-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:10:42', `update_by` = 'admin', `update_time` = '2023-07-31 16:10:42' WHERE `id` = 1685925942017753089;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'htlb', `name` = '合同列表', `icon` = '1685926051459727361', `path` = '/portal/wholesaler/contract-list', `sort` = 1, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685925942017753089, `sequence` = '1685925942017753089-1685926059223384065-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:11:10', `update_by` = 'admin', `update_time` = '2023-09-11 15:36:45' WHERE `id` = 1685926059223384065;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'htlx', `name` = '合同类型', `icon` = '1685926163284066305', `path` = '/portal/wholesaler/contract-type', `sort` = 2, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685925942017753089, `sequence` = '1685925942017753089-1685926170141753345-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:11:37', `update_by` = 'admin', `update_time` = '2023-09-11 15:36:49' WHERE `id` = 1685926170141753345;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'jfxx', `name` = '甲方信息', `icon` = '1685926697529344001', `path` = '/portal/wholesaler/party-info', `sort` = 3, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685925942017753089, `sequence` = '1685925942017753089-1685926711500570626-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:13:46', `update_by` = 'admin', `update_time` = '2023-09-11 15:36:55' WHERE `id` = 1685926711500570626;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'smrz', `name` = '实名认证', `icon` = '1685926980728750082', `path` = '', `sort` = 6, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685926994095996929-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:14:53', `update_by` = 'admin', `update_time` = '2023-07-31 16:14:59' WHERE `id` = 1685926994095996929;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'smrz1', `name` = '认证列表', `icon` = '1685927119023341570', `path` = '/portal/wholesaler/real-name-auth', `sort` = 1, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1685926994095996929, `sequence` = '1685926994095996929-1685927199533006849-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:15:42', `update_by` = 'admin', `update_time` = '2023-09-11 15:37:07' WHERE `id` = 1685927199533006849;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843486182834178, `category_id` = NULL, `code` = 'qygl', `name` = '区域管理', `icon` = '1687075033061883905', `path` = '/portal/wholesaler/area-manage', `sort` = 7, `remark` = '', `enabled` = 1, `level` = 2, `parent_id` = 1687074853721833473, `sequence` = '1687074853721833473-1687075037470097409-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-08-03 20:16:48', `update_by` = 'admin', `update_time` = '2023-09-11 15:37:16' WHERE `id` = 1687075037470097409;

UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843906632450050, `category_id` = NULL, `code` = 'cgpjpz', `name` = '采购评价配置', `icon` = '1685907071261048834', `path` = '/portal/merchant-evaluation/purchase-evaluate-config', `sort` = 2, `remark` = '1', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685907083986567169-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 14:55:46', `update_by` = 'admin', `update_time` = '2023-09-11 10:11:35' WHERE `id` = 1685907083986567169;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843906632450050, `category_id` = NULL, `code` = 'mgcck', `name` = '敏感词词库', `icon` = '1685921971014303745', `path` = '/portal/merchant-evaluation/sensitive-words-thesaurus', `sort` = 3, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685921989473435650-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 15:55:00', `update_by` = 'admin', `update_time` = '2023-09-11 10:11:42' WHERE `id` = 1685921989473435650;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843906632450050, `category_id` = NULL, `code` = 'pjsh', `name` = '评价审核', `icon` = '1685927463614775298', `path` = '/portal/merchant-evaluation/evaluation-review', `sort` = 4, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685927472468951042-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:16:47', `update_by` = 'admin', `update_time` = '2023-09-11 10:11:48' WHERE `id` = 1685927472468951042;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843906632450050, `category_id` = NULL, `code` = 'dwfz', `name` = '档位分值', `icon` = '1685929458153127937', `path` = '/portal/merchant-evaluation/store-score', `sort` = 5, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685929470102700034-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:24:43', `update_by` = 'admin', `update_time` = '2023-09-11 10:11:53' WHERE `id` = 1685929470102700034;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843906632450050, `category_id` = NULL, `code` = 'pjpz', `name` = '评价配置', `icon` = '1685934794025041921', `path` = '/portal/merchant-evaluation/evaluate-config', `sort` = 6, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685934840816697346-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 16:46:04', `update_by` = 'admin', `update_time` = '2023-09-11 10:11:58' WHERE `id` = 1685934840816697346;
UPDATE `cube-biz-ouaa`.`menu` SET `terminal_id` = 1528568123400142850, `application_id` = 1685843906632450050, `category_id` = NULL, `code` = 'tjbb', `name` = '统计报表', `icon` = '1685941374678302721', `path` = '/portal/merchant-evaluation/statistical-report', `sort` = 9, `remark` = '', `enabled` = 1, `level` = 1, `parent_id` = NULL, `sequence` = '1685941406672453634-', `deleted` = 0, `create_by` = 'admin', `create_time` = '2023-07-31 17:12:05', `update_by` = 'admin', `update_time` = '2023-09-11 10:12:04' WHERE `id` = 1685941406672453634;
