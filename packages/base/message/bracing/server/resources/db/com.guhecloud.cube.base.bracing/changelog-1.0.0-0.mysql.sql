-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `cube_base_bracing`.`assembly_notice` (
   `id` bigint(20) NOT NULL COMMENT '主键',
   `business_type` varchar(255) DEFAULT NULL COMMENT '业务类型',
   `label` varchar(50) NOT NULL COMMENT '标签',
   `scene_code` varchar(50) DEFAULT NULL COMMENT '消息场景code',
   `value` int(11) DEFAULT NULL COMMENT '通知时间',
   `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
   `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
   `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   `tenant` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知消息配置';


CREATE TABLE `cube_base_bracing`.`assembly_notice_way` (
   `id` bigint(20) NOT NULL,
   `notice_id` bigint(20) DEFAULT NULL COMMENT '消息配置id',
   `notice_way` VARCHAR(20) DEFAULT NULL COMMENT '通知方式',
   `template_id` bigint(20) DEFAULT NULL COMMENT '模版id',
   `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
   `create_by` varchar(100) NOT NULL COMMENT '创建用户',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
   `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   `tenant` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='通知方式';

CREATE TABLE `cube_base_bracing`.`assembly_notice_obj` (
   `id` bigint(20) NOT NULL,
   `notice_id` bigint(20) DEFAULT NULL COMMENT '消息配置id',
   `obj_type` varchar(20) DEFAULT NULL COMMENT '对象类型',
   `obj_id` varchar(255) DEFAULT NULL COMMENT '对象id',
   `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
   `create_by` varchar(100) NOT NULL COMMENT '创建用户',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
   `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   `tenant` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知对象';

-- 字典服务
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1698522206462902273, 'message-notice-type', '消息通知方式', 0, '17311369782', '2023-09-04 10:23:46', '17311369782', '2023-09-04 10:23:46');
INSERT INTO `cube-base-dictionary`.`sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1698532558298775554, '站内通知', 'CHANNEL_SITE', '站内信消息', 'v1.0.0', NULL, 'message-notice-type', '17311369782', '2023-09-04 11:04:54', '17311369782', '2023-09-04 11:04:54', 'ENABLE', 0);
INSERT INTO `cube-base-dictionary`.`sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1698533983099650049, '短信通知', 'CHANNEL_SMS', '短信消息', 'v1.0.0', NULL, 'message-notice-type', '17311369782', '2023-09-04 11:10:33', '17311369782', '2023-09-04 11:10:33', 'ENABLE', 0);
INSERT INTO `cube-base-dictionary`.`sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1698534106709983234, 'app通知', 'CHANNEL_APP', 'app消息', 'v1.0.0', NULL, 'message-notice-type', '17311369782', '2023-09-04 11:11:03', '17311369782', '2023-09-04 11:11:03', 'ENABLE', 0);
INSERT INTO `cube-base-dictionary`.`sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1698534951103070209, '语音通知', 'CHANNEL_VOICE', '语音通知', 'v1.0.0', NULL, 'message-notice-type', '17311369782', '2023-09-04 11:14:24', '17311369782', '2023-09-04 11:14:24', 'ENABLE', 0);
