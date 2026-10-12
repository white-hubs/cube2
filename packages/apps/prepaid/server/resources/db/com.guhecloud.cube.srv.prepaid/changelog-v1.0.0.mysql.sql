-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- 电表
CREATE TABLE `ammeter` (
                           `id` bigint(20) NOT NULL COMMENT 'id',
                           `meter_id` varchar(100) DEFAULT NULL COMMENT '电表编号',
                           `room_id` varchar(100) DEFAULT NULL COMMENT '商铺编号',
                           `power_remain` decimal(10,2) DEFAULT NULL COMMENT '剩余电费',
                           `model` varchar(100) DEFAULT NULL COMMENT '电表型号',
                           `money` decimal(10,2) DEFAULT NULL COMMENT '合并计量余额',
                           `owe_money` tinyint(1) DEFAULT NULL COMMENT '是否欠费（true表示欠费）',
                           `un_connect` tinyint(1) DEFAULT NULL COMMENT '是否失连（true为失连）',
                           `open_or_close` tinyint(1) DEFAULT NULL COMMENT '分合闸状态（true为拉闸，false为合闸）',
                           `power_total` varchar(32) DEFAULT NULL COMMENT '总用电量',
                           `deleted` bigint(20) DEFAULT '0' COMMENT 'deleted',
                           `create_by` varchar(128) DEFAULT NULL COMMENT 'create_by',
                           `create_time` datetime DEFAULT NULL COMMENT 'create_time',
                           `update_by` varchar(128) DEFAULT NULL COMMENT 'update_by',
                           `update_time` datetime DEFAULT NULL COMMENT 'update_time',
                           `tenant` int(10) DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE,
                           KEY `ammeter_meter_id_IDX` (`meter_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电表';

-- 电表抄表记录
CREATE TABLE `collect_records` (
                                   `id` bigint(20) NOT NULL COMMENT 'id',
                                   `meter_id` varchar(100) DEFAULT NULL COMMENT '电表编号',
                                   `collect_time` datetime DEFAULT NULL COMMENT '采集时间',
                                   `power_remain` decimal(10,2) DEFAULT NULL COMMENT '剩余金额(元)',
                                   `power_tatol` varchar(100) DEFAULT NULL COMMENT '总用电量',
                                   `power_higher` varchar(100) DEFAULT NULL COMMENT '尖电能',
                                   `power_High` varchar(100) DEFAULT NULL COMMENT '峰电能',
                                   `power_Flat` varchar(100) DEFAULT NULL COMMENT '平电能',
                                   `power_Low` varchar(100) DEFAULT NULL COMMENT '谷电能',
                                   `price_higher` decimal(10,2) DEFAULT NULL COMMENT '尖电价',
                                   `price_High` decimal(10,2) DEFAULT NULL COMMENT '峰电价',
                                   `price_Flat` decimal(10,2) DEFAULT NULL COMMENT '平电价',
                                   `price_Low` decimal(10,2) DEFAULT NULL COMMENT '谷电价',
                                   `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间(0未删除)',
                                   `create_by` varchar(128) DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_by` varchar(128) DEFAULT NULL COMMENT '修改人',
                                   `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                   `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   KEY `collect_records_meter_id_IDX` (`meter_id`) USING BTREE,
                                   KEY `collect_records_collect_time_IDX` (`collect_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抄表记录';

-- 电表充值记录
CREATE TABLE `power_recharge` (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `meter_id` varchar(100) DEFAULT NULL COMMENT '电表编号',
                                  `recharge_time` datetime DEFAULT NULL COMMENT '充值时间',
                                  `sale_money` decimal(10,2) DEFAULT NULL COMMENT '售电金额',
                                  `buy_type` int(2) DEFAULT NULL COMMENT '购电类型(现金缴费= 1/刷卡机缴费= 2/微信扫码支付= 3/  支付宝扫码支付= 4/公对公转账= 5/  冲正或退电= 6/微信支付= 7/支付宝支付= 8/ 容量费= 9/充值批量导入= 10/网银转账= 11/  公摊电费= 12/基础电费= 13/在线卡充值= 20/  电费退补= 14)',
                                  `sale_type` int(2) DEFAULT NULL COMMENT '售电类型(正常售电= 1/冲正= 2/退电= 3 / 初始化= 4/容量费= 5)',
                                  `last_money` decimal(10,2) DEFAULT NULL COMMENT '上次累计金额',
                                  `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间(0未删除)',
                                  `create_by` varchar(128) DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_by` varchar(128) DEFAULT NULL COMMENT '修改人',
                                  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                  `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  KEY `power_recharge_meter_id_IDX` (`meter_id`) USING BTREE,
                                  KEY `power_recharge_recharge_time_IDX` (`recharge_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电表充值记录';

-- 日用电
CREATE TABLE `power_use_day` (
                                 `id` bigint(20) NOT NULL COMMENT 'id',
                                 `meter_id` varchar(100) DEFAULT NULL COMMENT '电表编号',
                                 `use_month` datetime DEFAULT NULL COMMENT '用能月份',
                                 `use_date` datetime DEFAULT NULL COMMENT '采集时间',
                                 `power_remain` decimal(10,2) DEFAULT NULL COMMENT '剩余金额(元)',
                                 `pv` varchar(100) DEFAULT NULL COMMENT '本段时间内的用电量',
                                 `pvj` varchar(100) DEFAULT NULL COMMENT '本段时间内的尖电能用电量',
                                 `pvf` varchar(100) DEFAULT NULL COMMENT '本段时间内的峰电能用电量',
                                 `pvp` varchar(100) DEFAULT NULL COMMENT '本段时间内的平电能用电量',
                                 `pvg` varchar(100) DEFAULT NULL COMMENT '本段时间内的谷电能用电量',
                                 `current_Pv` varchar(100) DEFAULT NULL COMMENT '当前抄表用电量',
                                 `current_Pvj` varchar(100) DEFAULT NULL COMMENT '当前抄表尖电量',
                                 `current_Pvf` varchar(100) DEFAULT NULL COMMENT '当前抄表峰电量',
                                 `current_Pvp` varchar(100) DEFAULT NULL COMMENT '当前抄表平电量',
                                 `current_Pvg` varchar(100) DEFAULT NULL COMMENT '当前抄表谷电量',
                                 `last_Pv` varchar(100) DEFAULT NULL COMMENT '上次抄表用电量',
                                 `last_Pvj` varchar(100) DEFAULT NULL COMMENT '上次抄表尖电量',
                                 `last_Pvf` varchar(100) DEFAULT NULL COMMENT '上次抄表峰电量',
                                 `last_Pvp` varchar(100) DEFAULT NULL COMMENT '上次抄表平电量',
                                 `last_Pvg` varchar(100) DEFAULT NULL COMMENT '上次抄表谷电量',
                                 `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间(0未删除)',
                                 `create_by` varchar(128) DEFAULT NULL COMMENT '创建人',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_by` varchar(128) DEFAULT NULL COMMENT '修改人',
                                 `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                 `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `power_use_day_meter_id_IDX` (`meter_id`) USING BTREE,
                                 KEY `power_use_day_use_date_IDX` (`use_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日用电表';

-- 水表
CREATE TABLE `water_meter` (
                               `id` bigint(20) NOT NULL COMMENT 'id',
                               `meter_id` varchar(100) DEFAULT NULL COMMENT '电表编号',
                               `room_id` varchar(100) DEFAULT NULL COMMENT '商铺编号',
                               `account` varchar(100) DEFAULT NULL COMMENT '剩余水量',
                               `total_money` decimal(10,2) DEFAULT NULL COMMENT '总购水金额',
                               `total_power` varchar(100) DEFAULT NULL COMMENT '累计购水量',
                               `price` decimal(10,2) DEFAULT NULL COMMENT '水价',
                               `owe_status` tinyint(1) DEFAULT NULL COMMENT '是否欠费（true表示欠费）',
                               `un_connect` tinyint(1) DEFAULT NULL COMMENT '是否失连（true为失连）',
                               `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间戳,0未删除',
                               `create_by` varchar(128) DEFAULT NULL COMMENT '创建人',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_by` varchar(128) DEFAULT NULL COMMENT '修改人',
                               `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                               `tenant` int(10) DEFAULT NULL COMMENT '租户号',
                               PRIMARY KEY (`id`) USING BTREE,
                               KEY `water_meter_meter_id_IDX` (`meter_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水表';

-- 水表充值记录
-- `cube-srv-gen-prepaid`.water_recharge definition

CREATE TABLE `water_recharge` (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `meter_id` varchar(100) DEFAULT NULL COMMENT '水表编号',
                                  `recharge_time` datetime DEFAULT NULL COMMENT '充值时间',
                                  `sale_money` decimal(10,2) DEFAULT NULL COMMENT '售水金额',
                                  `buy_type` int(2) DEFAULT NULL COMMENT '购水类型(现金缴费= 1/刷卡机缴费= 2/微信扫码支付= 3/  支付宝扫码支付= 4/公对公转账= 5/  冲正或退水= 6/微信支付= 7/支付宝支付= 8/ 容量费= 9/充值批量导入= 10/网银转账= 11/  公摊水费= 12/基础水费= 13/在线卡充值= 20/  水费退补= 14)',
                                  `sale_type` int(2) DEFAULT NULL COMMENT '售水类型(正常售水= 1/冲正= 2/退水= 3 / 初始化= 4/容量费= 5)',
                                  `last_money` decimal(10,2) DEFAULT NULL COMMENT '上次累计金额',
                                  `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间(0未删除)',
                                  `create_by` varchar(128) DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_by` varchar(128) DEFAULT NULL COMMENT '修改人',
                                  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                  `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  KEY `water_recharge_meter_id_IDX` (`meter_id`) USING BTREE,
                                  KEY `water_recharge_recharge_time_IDX` (`recharge_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水表充值记录';

-- 日用水
CREATE TABLE `water_use_day` (
                                 `id` bigint(20) NOT NULL COMMENT 'id',
                                 `meter_id` varchar(100) DEFAULT NULL COMMENT '水表编号',
                                 `use_month` datetime DEFAULT NULL COMMENT '用能月份',
                                 `use_date` datetime DEFAULT NULL COMMENT '采集时间',
                                 `power_remain` varchar(100) DEFAULT NULL COMMENT '剩余水量',
                                 `pv` varchar(100) DEFAULT NULL COMMENT '本段时间内的用水量',
                                 `current_Pv` varchar(100) DEFAULT NULL COMMENT '当前抄表用水量',
                                 `last_Pv` varchar(100) DEFAULT NULL COMMENT '上次抄表用水量',
                                 `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间(0未删除)',
                                 `create_by` varchar(128) DEFAULT NULL COMMENT '创建人',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_by` varchar(128) DEFAULT NULL COMMENT '修改人',
                                 `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                 `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `water_use_day_meter_id_IDX` (`meter_id`) USING BTREE,
                                 KEY `water_use_day_use_date_IDX` (`use_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日用水表';

-- 同步记录表
CREATE TABLE `pull_data_record` (
                                    `id` bigint(20) NOT NULL COMMENT 'id',
                                    `pull_business` varchar(100) DEFAULT NULL COMMENT '拉取的业务',
                                    `pull_time` datetime DEFAULT NULL COMMENT '本次拉取时间',
                                    `pull_type` varchar(100) DEFAULT NULL COMMENT '拉取类型(全量,增量.目前只用到增量拉取)',
                                    `pull_total` bigint(20) DEFAULT NULL COMMENT '本次拉取数据总量',
                                    `pull_result` varchar(100) DEFAULT NULL COMMENT '本次拉取结果',
                                    `pull_data_id` text COMMENT '拉取数据的主键',
                                    `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                                    `deleted` bigint(20) DEFAULT '0' COMMENT '删除时间(0未删除)',
                                    `create_by` varchar(128) DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_by` varchar(128) DEFAULT NULL COMMENT '修改人',
                                    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                    `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    KEY `pull_data_record_pull_time_IDX` (`pull_time`) USING BTREE,
                                    KEY `pull_data_record_pull_business_IDX` (`pull_business`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拉取记录表';
