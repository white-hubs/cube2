-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `attributive_group` (
                                     `id` bigint(32) DEFAULT NULL,
                                     `tenant` varchar(32) DEFAULT NULL COMMENT '租户号',
                                     `deleted` varchar(32) DEFAULT NULL COMMENT '删除标识',
                                     `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                     `create_time` date DEFAULT NULL COMMENT '创建时间',
                                     `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                     `update_time` date DEFAULT NULL COMMENT '更新时间',
                                     `application_code` varchar(50) DEFAULT NULL COMMENT '应用',
                                     `name` varchar(50) DEFAULT NULL COMMENT '分组名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属性分组';

CREATE TABLE `group_attributive_details` (
                                             `id` bigint(20) NOT NULL,
                                             `tenant` int(32) DEFAULT NULL COMMENT '租户号',
                                             `deleted` bigint(20) DEFAULT NULL COMMENT '删除',
                                             `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                             `create_time` date DEFAULT NULL COMMENT '创建时间',
                                             `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                             `update_time` date DEFAULT NULL COMMENT '更新时间',
                                             `code` varchar(50) DEFAULT NULL COMMENT '属性code',
                                             `application_code` varchar(50) DEFAULT NULL COMMENT '应用',
                                             `group_id` bigint(20) DEFAULT NULL COMMENT '最大id',
                                             `attributive_name` varchar(50) DEFAULT NULL COMMENT '属性名称',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组属性详情';

INSERT INTO `attributive_group`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `application_code`, `name`) VALUES (123546549657567473, '58197260', '0', '9527', '2023-07-18', '9527', '2023-07-18', 'ELECTRICMANAGE', '电压');
INSERT INTO `attributive_group`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `application_code`, `name`) VALUES (145746978235563422, '58197260', '0', '9527', '2023-07-18', '9527', '2023-07-18', 'ELECTRICMANAGE', '电流');
INSERT INTO `attributive_group`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `application_code`, `name`) VALUES (178645283657421564, '58197260', '0', '9527', '2023-07-18', '9527', '2023-07-18', 'ELECTRICMANAGE', '温度');
INSERT INTO `attributive_group`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `application_code`, `name`) VALUES (178956363636364241, '58197260', '0', '9527', '2023-07-18', '9527', '2023-07-18', 'ELECTRICMANAGE', '总电能');


INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1235789963464581, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_electric_a', 'ELECTRICMANAGE', 145746978235563422, '配电箱电流a');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1365222289622788, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_temperature_b', 'ELECTRICMANAGE', 178645283657421564, '配电箱温度b');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1435986666248777, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_temperature_c', 'ELECTRICMANAGE', 178645283657421564, '配电箱温度c');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1623855659453202, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_temperature_a', 'ELECTRICMANAGE', 178645283657421564, '配电箱温度a');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1745899635824433, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_voltage_a', 'ELECTRICMANAGE', 123546549657567473, '配电箱电压a');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1764556647883324, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_voltage_c', 'ELECTRICMANAGE', 123546549657567473, '配电箱电压c');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1789435245512044, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_electric_b', 'ELECTRICMANAGE', 145746978235563422, '配电箱电流b');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1789654542356463, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_electric_total', 'ELECTRICMANAGE', 178956363636364241, '当前总电能');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1952487963665874, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_voltage_b', 'ELECTRICMANAGE', 123546549657567473, '配电箱电压b');
INSERT INTO `group_attributive_details`(`id`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `code`, `application_code`, `group_id`, `attributive_name`) VALUES (1956478265361488, 58197260, 0, '9527', '2023-07-18', '9527', '2023-07-18', 'box_electric_c', 'ELECTRICMANAGE', 145746978235563422, '配电箱电流c');


