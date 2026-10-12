-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `qrcode_entity_style` (
   `id` bigint(20) NOT NULL,
   `entity_type` varchar(64) NOT NULL COMMENT '实体类型',
   `style_id` bigint(20) DEFAULT NULL COMMENT '样式id',
   `tenant` int(10) DEFAULT NULL COMMENT '租户编号',
   `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   `deleted` bigint(20) DEFAULT NULL COMMENT '是否删除',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实体关联二维码样式';

INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_type` (`id`, `type_name`, `entity_type`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752179570416852994, '风险源管理', 'RISK_SOURCE', 58197260, 'admin', '2024-01-30 11:58:58', 'admin', '2024-01-30 11:58:58', 0);

INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752207346958401539, 1752179570416852994, 1697138315709743105, 0, 58197260, 'admin', '2024-01-30 13:49:21', 'admin', '2024-01-30 13:49:21', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752207346958401540, 1752179570416852994, 1697138315743297538, 0, 58197260, 'admin', '2024-01-30 13:49:21', 'admin', '2024-01-30 13:49:21', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752207346958401541, 1752179570416852994, 1697138315760074754, 0, 58197260, 'admin', '2024-01-30 13:49:21', 'admin', '2024-01-30 13:49:21', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752207346958401542, 1752179570416852994, 1697138315688771585, 0, 58197260, 'admin', '2024-01-30 13:49:21', 'admin', '2024-01-30 13:49:21', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752207347012927490, 1752179570416852994, 1697138315776851970, 0, 58197260, 'admin', '2024-01-30 13:49:21', 'admin', '2024-01-30 13:49:21', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752207347012927491, 1752179570416852994, 1697138315806212097, 0, 58197260, 'admin', '2024-01-30 13:49:21', 'admin', '2024-01-30 13:49:21', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752207347012927492, 1752179570416852994, 1697138315822989313, 0, 58197260, 'admin', '2024-01-30 13:49:21', 'admin', '2024-01-30 13:49:21', 0);
INSERT INTO `cube-srv-gen-identity`.`qrcode_entity_application` (`id`, `entity_id`, `application_id`, `selected`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1752207347012927493, 1752179570416852994, 1716651713169014786, 0, 58197260, 'admin', '2024-01-30 13:49:21', 'admin', '2024-01-30 13:49:21', 0);
