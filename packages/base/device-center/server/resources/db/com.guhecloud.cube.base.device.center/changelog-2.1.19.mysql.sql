-- liquibase formatted sql
-- changeset wql:addTable

drop table if exists `center_biz_device_statistics`;
CREATE TABLE `center_biz_device_statistics` (
    `id` bigint(20) NOT NULL COMMENT '物联网设备ID',
    `device_state` int(11) DEFAULT NULL COMMENT '当前状态 0 离线 1在线',
    `report_time`datetime DEFAULT NULL COMMENT '状态更新时间',
    `online_time` BIGINT(20) DEFAULT NULL COMMENT '在线时长',
    `online_num` int(11) DEFAULT NULL COMMENT '在线次数',
    `offline_time` BIGINT(20) DEFAULT NULL COMMENT '离线时长',
    `offline_num` int(11) DEFAULT NULL COMMENT '离线次数',
    PRIMARY KEY `device_id` (`id`) USING BTREE COMMENT '设备id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='设备实例状态统计';