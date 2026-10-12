-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `qrcode_entity_type` (
  `id` bigint(20) NOT NULL,
  `type_name` varchar(64) DEFAULT NULL COMMENT '类型名称',
  `entity_type` varchar(64) DEFAULT NULL COMMENT '实体类型',
  `tenant` int(10) DEFAULT NULL COMMENT '租户编号',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` bigint(20) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '实体类型';


CREATE TABLE `qrcode_entity_application` (
         `id` bigint(20) NOT NULL,
         `entity_id` bigint(20) DEFAULT NULL COMMENT '实体id',
         `application_id` bigint(20) DEFAULT NULL COMMENT '应用id',
         `selected` TINYINT(1) DEFAULT NULL COMMENT '是否选中',
         `tenant` int(10) DEFAULT NULL COMMENT '租户编号',
         `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
         `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
         `deleted` bigint(20) DEFAULT NULL COMMENT '是否删除',
         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT '实体绑定应用';


INSERT INTO `cube-srv-gen-identity`.`qrcode_application` (`id`, `application_code`, `application_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1697138315688771585, 'HIDDENDANGER', '隐患排查', 58197260, '17311369782', '2023-08-31 14:44:40', '17311369782', '2023-08-31 14:44:40', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_application` (`id`, `application_code`, `application_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1697138315709743105, 'FAULT', '故障报修', 58197260, '17311369782', '2023-08-31 14:44:40', '17311369782', '2023-08-31 14:44:40', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_application` (`id`, `application_code`, `application_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1697138315743297538, 'INSPECTION', '日常巡检', 58197260, '17311369782', '2023-08-31 14:44:40', '17311369782', '2023-08-31 14:44:40', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_application` (`id`, `application_code`, `application_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1697138315760074754, 'MAINTENACE', '维保作业', 58197260, '17311369782', '2023-08-31 14:44:40', '17311369782', '2023-08-31 14:44:40', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_application` (`id`, `application_code`, `application_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1697138315776851970, 'OUTSOURCE_REPAIR', '委外维修', 58197260, '17311369782', '2023-08-31 14:44:40', '17311369782', '2023-08-31 14:44:40', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_application` (`id`, `application_code`, `application_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1697138315806212097, 'RETROFIT', '更新改造', 58197260, '17311369782', '2023-08-31 14:44:40', '17311369782', '2023-08-31 14:44:40', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_application` (`id`, `application_code`, `application_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1697138315822989313, 'PROPERTY_MANAGEMENT', '资产管理', 58197260, '17311369782', '2023-08-31 14:44:40', '17311369782', '2023-08-31 14:44:40', 0);

INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_type` (`id`, `type_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `entity_type`) VALUES (1716712517378068482, '设备', 58197260, '17683181184', '2023-10-24 15:05:34', '17683181184', '2023-10-24 15:05:34', 0, 'DEVICE');
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_type` (`id`, `type_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `entity_type`) VALUES (1716712604103692289, '资产设备', 58197260, '17683181184', '2023-10-24 15:05:54', '17683181184', '2023-10-24 15:05:54', 0, 'PROPERTY');
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_type` (`id`, `type_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `entity_type`) VALUES (1716712636399833090, '档位', 58197260, '17683181184', '2023-10-24 15:06:02', '17683181184', '2023-10-24 15:06:02', 0, 'STALL');
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_type` (`id`, `type_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `entity_type`) VALUES (1716712662136082434, '现场点', 58197260, '17683181184', '2023-10-24 15:06:08', '17683181184', '2023-10-24 15:06:08', 0, 'SITE_POINT');


INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605756976267266, 1716712517378068482, 1697138315688771585, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605756976267267, 1716712517378068482, 1697138315709743105, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404609, 1716712517378068482, 1697138315743297538, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404610, 1716712517378068482, 1697138315760074754, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404611, 1716712517378068482, 1697138315776851970, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404612, 1716712517378068482, 1697138315806212097, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404613, 1716712517378068482, 1697138315822989313, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404616, 1716712604103692289, 1697138315688771585, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404617, 1716712604103692289, 1697138315709743105, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404618, 1716712604103692289, 1697138315743297538, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404619, 1716712604103692289, 1697138315760074754, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404620, 1716712604103692289, 1697138315776851970, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404621, 1716712604103692289, 1697138315806212097, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757022404622, 1716712604103692289, 1697138315822989313, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513474, 1716712636399833090, 1697138315688771585, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513475, 1716712636399833090, 1697138315709743105, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513476, 1716712636399833090, 1697138315743297538, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513477, 1716712636399833090, 1697138315760074754, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513478, 1716712636399833090, 1697138315776851970, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513479, 1716712636399833090, 1697138315806212097, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513480, 1716712636399833090, 1697138315822989313, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513483, 1716712662136082434, 1697138315688771585, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513484, 1716712662136082434, 1697138315709743105, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513485, 1716712662136082434, 1697138315743297538, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513486, 1716712662136082434, 1697138315760074754, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513487, 1716712662136082434, 1697138315776851970, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513488, 1716712662136082434, 1697138315806212097, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1719605757089513489, 1716712662136082434, 1697138315822989313, 0, 58197260, '17311369782', '2023-11-01 14:42:16', '17311369782', '2023-11-01 14:42:16', 0);
