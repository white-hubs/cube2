-- liquibase formatted sql
--
-- changeset chx:alter-1
CREATE TABLE IF NOT EXISTS `food_safe_day_statistics` (
    `id` bigint(20) NOT NULL,
    `time_day` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间天',
    `time_day_format` varchar(20) DEFAULT NULL COMMENT '时间天字符串',
    `test_count` int(11) DEFAULT '0' COMMENT '检测次数',
    `pass_count` int(11) DEFAULT '0' COMMENT '合格数量',
    `fail_count` int(11) DEFAULT '0' COMMENT '不合格数量',
    `pass_percent` varchar(10) DEFAULT '0.00' COMMENT '合格率',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `food_safe_ranking_statistics` (
    `id` bigint(20) NOT NULL,
    `time_day` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间天',
    `time_day_format` varchar(20) DEFAULT NULL COMMENT '时间天字符串',
    `sample_name` varchar(20) DEFAULT '0' COMMENT '日期',
    `fail_count` int(11) DEFAULT '0' COMMENT '检测次数',
    `time_period` varchar(50) DEFAULT '0' COMMENT '日期',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `food_safe_week_statistics` (
    `id` bigint(20) NOT NULL,
    `time_day` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间天',
    `time_day_format` varchar(20) DEFAULT NULL COMMENT '时间天字符串',
    `testcount` int(11) DEFAULT '0' COMMENT '检测次数',
    `testeddate` varchar(20) DEFAULT NULL COMMENT '日期',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `merchant_information` (
    `id` bigint(20) NOT NULL,
    `name` varchar(500) NOT NULL COMMENT '名称',
    `sex` varchar(20) DEFAULT NULL COMMENT '性别',
    `age` int(11) DEFAULT NULL COMMENT '年龄',
    `merchant_Type` varchar(100) DEFAULT NULL COMMENT '商户类型',
    `area` varchar(300) DEFAULT NULL COMMENT '区域',
    `business_scope` varchar(100) DEFAULT NULL COMMENT '经营类型',
    `vehicle_type` varchar(100) DEFAULT NULL COMMENT '采购车类型',
    `native_place_province_id` bigint(20) DEFAULT NULL COMMENT '籍贯省id',
    `native_place_city_id` bigint(20) DEFAULT NULL COMMENT '籍贯市id',
    `native_place_county_id` bigint(20) DEFAULT NULL COMMENT '籍贯县/区id',
    `native_place` varchar(500) DEFAULT NULL COMMENT '籍贯(短横线隔开)',
    `upstream_source_province_id` bigint(20) DEFAULT NULL COMMENT '上游来源省id',
    `upstream_source_city_id` bigint(20) DEFAULT NULL COMMENT '上游来源市id',
    `upstream_source_county_id` bigint(20) DEFAULT NULL COMMENT '上游来源县/区id',
    `upstream_source` varchar(500) DEFAULT NULL COMMENT '上游来源(短横线隔开)',
    `business_scale_number` int(11) DEFAULT '0' COMMENT '经营规模(吨)',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `park_settle_category_day` (
    `id` bigint(20) NOT NULL,
    `goods_count` varchar(255) NOT NULL COMMENT '货物总重量',
    `day_to_day_flag` varchar(255) DEFAULT NULL COMMENT '标识（-1: 下降\r\n1:上升）',
    `percent` varchar(255) DEFAULT NULL COMMENT '百分比',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    `delivery_date` date DEFAULT NULL COMMENT '来货时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='昨日来货统计';

CREATE TABLE IF NOT EXISTS `park_settle_category_day_details` (
    `id` bigint(20) NOT NULL,
    `category_id` bigint(20) NOT NULL,
    `main_cate_gory` varchar(255) DEFAULT NULL,
    `main_cate_gory_name` varchar(255) DEFAULT NULL,
    `net_weight` varchar(255) DEFAULT NULL,
    `percent` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `park_settle_category_week` (
    `id` bigint(20) NOT NULL,
    `main_cate_gory` varchar(255) DEFAULT NULL,
    `main_cate_gory_name` varchar(255) DEFAULT NULL,
    `net_weight` varchar(255) DEFAULT NULL,
    `days` date DEFAULT NULL,
    `percent` varchar(255) DEFAULT NULL,
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='近一周来货趋势图';

CREATE TABLE IF NOT EXISTS `scene_schedule_job` (
    `id` bigint(20) NOT NULL,
    `code` varchar(100) DEFAULT NULL,
    `name` varchar(100) DEFAULT NULL,
    `cron` varchar(300) DEFAULT NULL,
    `timeout` int(2) unsigned DEFAULT NULL,
    `enable` tinyint(1) DEFAULT NULL,
    `last_trigger_time` bigint(20) DEFAULT NULL COMMENT '上次执行时间',
    `next_trigger_time` bigint(20) DEFAULT NULL COMMENT '下次执行时间',
    `tenant` int(10) unsigned DEFAULT NULL,
    `deleted` bigint(20) unsigned DEFAULT NULL,
    `create_by` varchar(20) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(20) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_code_tenant_deleted` (`code`,`tenant`,`deleted`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景采集调度配置';

CREATE TABLE IF NOT EXISTS `transport_day` (
    `id` bigint(20) NOT NULL,
    `transport_count` int(10) DEFAULT NULL,
    `piece_count` int(10) DEFAULT NULL,
    `car_count` int(10) DEFAULT NULL,
    `transport_date` date DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='昨日转运统计';

CREATE TABLE IF NOT EXISTS `transport_week` (
    `id` bigint(20) NOT NULL,
    `enter_date` date DEFAULT NULL,
    `quick_piece_count` int(10) DEFAULT NULL,
    `normal_piece_count` int(10) DEFAULT NULL,
    `normal_car_sum` int(10) DEFAULT NULL COMMENT '普采车辆总数',
    `quick_car_sum` int(10) DEFAULT NULL COMMENT '快驴车辆总数',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `vehicle_day_statistics` (
    `id` bigint(20) NOT NULL,
    `time_hour` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间小时',
    `time_hour_format` varchar(20) DEFAULT NULL COMMENT '时间小时字符串',
    `time_day` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间天',
    `time_day_format` varchar(20) DEFAULT NULL COMMENT '时间天字符串',
    `in_coming_type` varchar(2) DEFAULT NULL COMMENT '来车类型',
    `vehicle_nature` varchar(20) DEFAULT NULL COMMENT '车辆性质枚举',
    `buss_type` varchar(2) DEFAULT NULL COMMENT '业务类型',
    `date_type` varchar(100) DEFAULT NULL COMMENT '日期类型(today ：查询日期的12小时nextDay ：查询日期下一日期的12小时)',
    `date_hour` varchar(20) DEFAULT NULL COMMENT '小时点',
    `car_count` int(11) DEFAULT '0' COMMENT '车次数',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `vehicle_load_week_statistics` (
    `id` bigint(20) NOT NULL,
    `time_day` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间天',
    `time_day_format` varchar(20) DEFAULT NULL COMMENT '时间天字符串',
    `days` varchar(20) DEFAULT NULL COMMENT '天',
    `car_count` int(11) DEFAULT '0' COMMENT '车次数',
    `weight_count` decimal(15,4) DEFAULT '0.0000' COMMENT '重量千克',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `vehicle_week_statistics` (
    `id` bigint(20) NOT NULL,
    `time_day` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间天',
    `time_day_format` varchar(20) DEFAULT NULL COMMENT '时间天字符串',
    `in_coming_car_type` varchar(2) DEFAULT NULL COMMENT '来车类型',
    `vehicle_nature` varchar(20) DEFAULT NULL COMMENT '车辆性质枚举',
    `days` varchar(20) DEFAULT NULL COMMENT '日期类型(today ：查询日期的12小时nextDay ：查询日期下一日期的12小时)',
    `car_sum` int(11) DEFAULT '0' COMMENT '车次数',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `country_address` (
    `id` bigint(20) NOT NULL,
    `pid` bigint(20) NOT NULL COMMENT '父级id',
    `deep` int NOT NULL COMMENT '级别',
    `name` varchar(200) NOT NULL COMMENT '名称',
    `pinyin_prefix` varchar(2) DEFAULT NULL COMMENT '首字母',
    `pinyin` varchar(100) DEFAULT NULL COMMENT '拼音',
    `ext_id` bigint(20) DEFAULT NULL COMMENT '行政区规划代码',
    `ext_name` varchar(200) DEFAULT NULL COMMENT '行政区规划名称',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT 0 COMMENT '租户主键',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- changeset chx:alter-2
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(11, 0, 0, '北京', 'b', 'bei jing', 110000000000, '北京市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(12, 0, 0, '天津', 't', 'tian jin', 120000000000, '天津市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(13, 0, 0, '河北', 'h', 'he bei', 130000000000, '河北省', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(14, 0, 0, '山西', 's', 'shan xi', 140000000000, '山西省', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(15, 0, 0, '内蒙古', 'n', 'nei meng gu', 150000000000, '内蒙古自治区', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(21, 0, 0, '辽宁', 'l', 'liao ning', 210000000000, '辽宁省', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(22, 0, 0, '吉林', 'j', 'ji lin', 220000000000, '吉林省', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(23, 0, 0, '黑龙江', 'h', 'hei long jiang', 230000000000, '黑龙江省', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(31, 0, 0, '上海', 's', 'shang hai', 310000000000, '上海市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(32, 0, 0, '江苏', 'j', 'jiang su', 320000000000, '江苏省', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(33, 0, 0, '浙江', 'z', 'zhe jiang', 330000000000, '浙江省', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(34, 0, 0, '安徽', 'a', 'an hui', 340000000000, '安徽省', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(35, 0, 0, '福建', 'f', 'fu jian', 350000000000, '福建省', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(36, 0, 0, '江西', 'j', 'jiang xi', 360000000000, '江西省', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(37, 0, 0, '山东', 's', 'shan dong', 370000000000, '山东省', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(41, 0, 0, '河南', 'h', 'he nan', 410000000000, '河南省', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(42, 0, 0, '湖北', 'h', 'hu bei', 420000000000, '湖北省', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(43, 0, 0, '湖南', 'h', 'hu nan', 430000000000, '湖南省', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(44, 0, 0, '广东', 'g', 'guang dong', 440000000000, '广东省', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(45, 0, 0, '广西', 'g', 'guang xi', 450000000000, '广西壮族自治区', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(46, 0, 0, '海南', 'h', 'hai nan', 460000000000, '海南省', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(50, 0, 0, '重庆', 'c', 'chong qing', 500000000000, '重庆市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(51, 0, 0, '四川', 's', 'si chuan', 510000000000, '四川省', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(52, 0, 0, '贵州', 'g', 'gui zhou', 520000000000, '贵州省', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(53, 0, 0, '云南', 'y', 'yun nan', 530000000000, '云南省', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(54, 0, 0, '西藏', 'x', 'xi zang', 540000000000, '西藏自治区', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(61, 0, 0, '陕西', 's', 'shan xi', 610000000000, '陕西省', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(62, 0, 0, '甘肃', 'g', 'gan su', 620000000000, '甘肃省', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(63, 0, 0, '青海', 'q', 'qing hai', 630000000000, '青海省', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(64, 0, 0, '宁夏', 'n', 'ning xia', 640000000000, '宁夏回族自治区', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(65, 0, 0, '新疆', 'x', 'xin jiang', 650000000000, '新疆维吾尔自治区', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(71, 0, 0, '台湾', '~3', 'tai wan', 710000000000, '台湾省', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(81, 0, 0, '香港', '~1', 'xiang gang', 810000000000, '香港特别行政区', 0, 'system', '2024-12-10 10:35:20', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(82, 0, 0, '澳门', '~2', 'ao men', 820000000000, '澳门特别行政区', 0, 'system', '2024-12-10 10:35:20', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1101, 11, 1, '北京', 'b', 'bei jing', 110100000000, '北京市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1201, 12, 1, '天津', 't', 'tian jin', 120100000000, '天津市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1301, 13, 1, '石家庄', 's', 'shi jia zhuang', 130100000000, '石家庄市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1302, 13, 1, '唐山', 't', 'tang shan', 130200000000, '唐山市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1303, 13, 1, '秦皇岛', 'q', 'qin huang dao', 130300000000, '秦皇岛市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1304, 13, 1, '邯郸', 'h', 'han dan', 130400000000, '邯郸市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1305, 13, 1, '邢台', 'x', 'xing tai', 130500000000, '邢台市', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1306, 13, 1, '保定', 'b', 'bao ding', 130600000000, '保定市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1307, 13, 1, '张家口', 'z', 'zhang jia kou', 130700000000, '张家口市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1308, 13, 1, '承德', 'c', 'cheng de', 130800000000, '承德市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1309, 13, 1, '沧州', 'c', 'cang zhou', 130900000000, '沧州市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1310, 13, 1, '廊坊', 'l', 'lang fang', 131000000000, '廊坊市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1311, 13, 1, '衡水', 'h', 'heng shui', 131100000000, '衡水市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1401, 14, 1, '太原', 't', 'tai yuan', 140100000000, '太原市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1402, 14, 1, '大同', 'd', 'da tong', 140200000000, '大同市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1403, 14, 1, '阳泉', 'y', 'yang quan', 140300000000, '阳泉市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1404, 14, 1, '长治', 'c', 'chang zhi', 140400000000, '长治市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1405, 14, 1, '晋城', 'j', 'jin cheng', 140500000000, '晋城市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1406, 14, 1, '朔州', 's', 'shuo zhou', 140600000000, '朔州市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1407, 14, 1, '晋中', 'j', 'jin zhong', 140700000000, '晋中市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1408, 14, 1, '运城', 'y', 'yun cheng', 140800000000, '运城市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1409, 14, 1, '忻州', 'x', 'xin zhou', 140900000000, '忻州市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1410, 14, 1, '临汾', 'l', 'lin fen', 141000000000, '临汾市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:53', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1411, 14, 1, '吕梁', 'l', 'lv liang', 141100000000, '吕梁市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1501, 15, 1, '呼和浩特', 'h', 'hu he hao te', 150100000000, '呼和浩特市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1502, 15, 1, '包头', 'b', 'bao tou', 150200000000, '包头市', 0, 'system', '2024-12-10 10:35:06', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1503, 15, 1, '乌海', 'w', 'wu hai', 150300000000, '乌海市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1504, 15, 1, '赤峰', 'c', 'chi feng', 150400000000, '赤峰市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1505, 15, 1, '通辽', 't', 'tong liao', 150500000000, '通辽市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1506, 15, 1, '鄂尔多斯', 'e', 'e er duo si', 150600000000, '鄂尔多斯市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1507, 15, 1, '呼伦贝尔', 'h', 'hu lun bei er', 150700000000, '呼伦贝尔市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1508, 15, 1, '巴彦淖尔', 'b', 'ba yan nao er', 150800000000, '巴彦淖尔市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1509, 15, 1, '乌兰察布', 'w', 'wu lan cha bu', 150900000000, '乌兰察布市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1522, 15, 1, '兴安', 'x', 'xing an', 152200000000, '兴安盟', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1525, 15, 1, '锡林郭勒', 'x', 'xi lin guo le', 152500000000, '锡林郭勒盟', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(1529, 15, 1, '阿拉善', 'a', 'a la shan', 152900000000, '阿拉善盟', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2101, 21, 1, '沈阳', 's', 'shen yang', 210100000000, '沈阳市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2102, 21, 1, '大连', 'd', 'da lian', 210200000000, '大连市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2103, 21, 1, '鞍山', 'a', 'an shan', 210300000000, '鞍山市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2104, 21, 1, '抚顺', 'f', 'fu shun', 210400000000, '抚顺市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2105, 21, 1, '本溪', 'b', 'ben xi', 210500000000, '本溪市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2106, 21, 1, '丹东', 'd', 'dan dong', 210600000000, '丹东市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2107, 21, 1, '锦州', 'j', 'jin zhou', 210700000000, '锦州市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2108, 21, 1, '营口', 'y', 'ying kou', 210800000000, '营口市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:54', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2109, 21, 1, '阜新', 'f', 'fu xin', 210900000000, '阜新市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2110, 21, 1, '辽阳', 'l', 'liao yang', 211000000000, '辽阳市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2111, 21, 1, '盘锦', 'p', 'pan jin', 211100000000, '盘锦市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2112, 21, 1, '铁岭', 't', 'tie ling', 211200000000, '铁岭市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2113, 21, 1, '朝阳', 'c', 'chao yang', 211300000000, '朝阳市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2114, 21, 1, '葫芦岛', 'h', 'hu lu dao', 211400000000, '葫芦岛市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2201, 22, 1, '长春', 'c', 'chang chun', 220100000000, '长春市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2202, 22, 1, '吉林市', 'j', 'ji lin shi', 220200000000, '吉林市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2203, 22, 1, '四平', 's', 'si ping', 220300000000, '四平市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2204, 22, 1, '辽源', 'l', 'liao yuan', 220400000000, '辽源市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2205, 22, 1, '通化', 't', 'tong hua', 220500000000, '通化市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2206, 22, 1, '白山', 'b', 'bai shan', 220600000000, '白山市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2207, 22, 1, '松原', 's', 'song yuan', 220700000000, '松原市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2208, 22, 1, '白城', 'b', 'bai cheng', 220800000000, '白城市', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2224, 22, 1, '延边', 'y', 'yan bian', 222400000000, '延边朝鲜族自治州', 0, 'system', '2024-12-10 10:35:07', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2301, 23, 1, '哈尔滨', 'h', 'ha er bin', 230100000000, '哈尔滨市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2302, 23, 1, '齐齐哈尔', 'q', 'qi qi ha er', 230200000000, '齐齐哈尔市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2303, 23, 1, '鸡西', 'j', 'ji xi', 230300000000, '鸡西市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2304, 23, 1, '鹤岗', 'h', 'he gang', 230400000000, '鹤岗市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2305, 23, 1, '双鸭山', 's', 'shuang ya shan', 230500000000, '双鸭山市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2306, 23, 1, '大庆', 'd', 'da qing', 230600000000, '大庆市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2307, 23, 1, '伊春', 'y', 'yi chun', 230700000000, '伊春市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:55', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2308, 23, 1, '佳木斯', 'j', 'jia mu si', 230800000000, '佳木斯市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2309, 23, 1, '七台河', 'q', 'qi tai he', 230900000000, '七台河市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2310, 23, 1, '牡丹江', 'm', 'mu dan jiang', 231000000000, '牡丹江市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2311, 23, 1, '黑河', 'h', 'hei he', 231100000000, '黑河市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2312, 23, 1, '绥化', 's', 'sui hua', 231200000000, '绥化市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(2327, 23, 1, '大兴安岭', 'd', 'da xing an ling', 232700000000, '大兴安岭地区', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3101, 31, 1, '上海', 's', 'shang hai', 310100000000, '上海市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3201, 32, 1, '南京', 'n', 'nan jing', 320100000000, '南京市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3202, 32, 1, '无锡', 'w', 'wu xi', 320200000000, '无锡市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3203, 32, 1, '徐州', 'x', 'xu zhou', 320300000000, '徐州市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3204, 32, 1, '常州', 'c', 'chang zhou', 320400000000, '常州市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3205, 32, 1, '苏州', 's', 'su zhou', 320500000000, '苏州市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3206, 32, 1, '南通', 'n', 'nan tong', 320600000000, '南通市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3207, 32, 1, '连云港', 'l', 'lian yun gang', 320700000000, '连云港市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3208, 32, 1, '淮安', 'h', 'huai an', 320800000000, '淮安市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3209, 32, 1, '盐城', 'y', 'yan cheng', 320900000000, '盐城市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3210, 32, 1, '扬州', 'y', 'yang zhou', 321000000000, '扬州市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3211, 32, 1, '镇江', 'z', 'zhen jiang', 321100000000, '镇江市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3212, 32, 1, '泰州', 't', 'tai zhou', 321200000000, '泰州市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3213, 32, 1, '宿迁', 's', 'su qian', 321300000000, '宿迁市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3301, 33, 1, '杭州', 'h', 'hang zhou', 330100000000, '杭州市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3302, 33, 1, '宁波', 'n', 'ning bo', 330200000000, '宁波市', 0, 'system', '2024-12-10 10:35:08', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3303, 33, 1, '温州', 'w', 'wen zhou', 330300000000, '温州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3304, 33, 1, '嘉兴', 'j', 'jia xing', 330400000000, '嘉兴市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3305, 33, 1, '湖州', 'h', 'hu zhou', 330500000000, '湖州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:56', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3306, 33, 1, '绍兴', 's', 'shao xing', 330600000000, '绍兴市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3307, 33, 1, '金华', 'j', 'jin hua', 330700000000, '金华市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3308, 33, 1, '衢州', 'q', 'qu zhou', 330800000000, '衢州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3309, 33, 1, '舟山', 'z', 'zhou shan', 330900000000, '舟山市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3310, 33, 1, '台州', 't', 'tai zhou', 331000000000, '台州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3311, 33, 1, '丽水', 'l', 'li shui', 331100000000, '丽水市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3401, 34, 1, '合肥', 'h', 'he fei', 340100000000, '合肥市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3402, 34, 1, '芜湖', 'w', 'wu hu', 340200000000, '芜湖市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3403, 34, 1, '蚌埠', 'b', 'beng bu', 340300000000, '蚌埠市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3404, 34, 1, '淮南', 'h', 'huai nan', 340400000000, '淮南市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3405, 34, 1, '马鞍山', 'm', 'ma an shan', 340500000000, '马鞍山市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3406, 34, 1, '淮北', 'h', 'huai bei', 340600000000, '淮北市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3407, 34, 1, '铜陵', 't', 'tong ling', 340700000000, '铜陵市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3408, 34, 1, '安庆', 'a', 'an qing', 340800000000, '安庆市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3410, 34, 1, '黄山', 'h', 'huang shan', 341000000000, '黄山市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3411, 34, 1, '滁州', 'c', 'chu zhou', 341100000000, '滁州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3412, 34, 1, '阜阳', 'f', 'fu yang', 341200000000, '阜阳市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3413, 34, 1, '宿州', 's', 'su zhou', 341300000000, '宿州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3415, 34, 1, '六安', 'l', 'lu an', 341500000000, '六安市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3416, 34, 1, '亳州', 'b', 'bo zhou', 341600000000, '亳州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3417, 34, 1, '池州', 'c', 'chi zhou', 341700000000, '池州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3418, 34, 1, '宣城', 'x', 'xuan cheng', 341800000000, '宣城市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3501, 35, 1, '福州', 'f', 'fu zhou', 350100000000, '福州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3502, 35, 1, '厦门', 'x', 'xia men', 350200000000, '厦门市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3503, 35, 1, '莆田', 'p', 'pu tian', 350300000000, '莆田市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3504, 35, 1, '三明', 's', 'san ming', 350400000000, '三明市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3505, 35, 1, '泉州', 'q', 'quan zhou', 350500000000, '泉州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3506, 35, 1, '漳州', 'z', 'zhang zhou', 350600000000, '漳州市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:57', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3507, 35, 1, '南平', 'n', 'nan ping', 350700000000, '南平市', 0, 'system', '2024-12-10 10:35:09', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3508, 35, 1, '龙岩', 'l', 'long yan', 350800000000, '龙岩市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3509, 35, 1, '宁德', 'n', 'ning de', 350900000000, '宁德市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3601, 36, 1, '南昌', 'n', 'nan chang', 360100000000, '南昌市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3602, 36, 1, '景德镇', 'j', 'jing de zhen', 360200000000, '景德镇市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3603, 36, 1, '萍乡', 'p', 'ping xiang', 360300000000, '萍乡市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3604, 36, 1, '九江', 'j', 'jiu jiang', 360400000000, '九江市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3605, 36, 1, '新余', 'x', 'xin yu', 360500000000, '新余市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3606, 36, 1, '鹰潭', 'y', 'ying tan', 360600000000, '鹰潭市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3607, 36, 1, '赣州', 'g', 'gan zhou', 360700000000, '赣州市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3608, 36, 1, '吉安', 'j', 'ji an', 360800000000, '吉安市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3609, 36, 1, '宜春', 'y', 'yi chun', 360900000000, '宜春市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3610, 36, 1, '抚州', 'f', 'fu zhou', 361000000000, '抚州市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3611, 36, 1, '上饶', 's', 'shang rao', 361100000000, '上饶市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3701, 37, 1, '济南', 'j', 'ji nan', 370100000000, '济南市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3702, 37, 1, '青岛', 'q', 'qing dao', 370200000000, '青岛市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3703, 37, 1, '淄博', 'z', 'zi bo', 370300000000, '淄博市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3704, 37, 1, '枣庄', 'z', 'zao zhuang', 370400000000, '枣庄市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3705, 37, 1, '东营', 'd', 'dong ying', 370500000000, '东营市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3706, 37, 1, '烟台', 'y', 'yan tai', 370600000000, '烟台市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3707, 37, 1, '潍坊', 'w', 'wei fang', 370700000000, '潍坊市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3708, 37, 1, '济宁', 'j', 'ji ning', 370800000000, '济宁市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:58', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3709, 37, 1, '泰安', 't', 'tai an', 370900000000, '泰安市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3710, 37, 1, '威海', 'w', 'wei hai', 371000000000, '威海市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3711, 37, 1, '日照', 'r', 'ri zhao', 371100000000, '日照市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3713, 37, 1, '临沂', 'l', 'lin yi', 371300000000, '临沂市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3714, 37, 1, '德州', 'd', 'de zhou', 371400000000, '德州市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3715, 37, 1, '聊城', 'l', 'liao cheng', 371500000000, '聊城市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3716, 37, 1, '滨州', 'b', 'bin zhou', 371600000000, '滨州市', 0, 'system', '2024-12-10 10:35:10', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(3717, 37, 1, '菏泽', 'h', 'he ze', 371700000000, '菏泽市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4101, 41, 1, '郑州', 'z', 'zheng zhou', 410100000000, '郑州市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4102, 41, 1, '开封', 'k', 'kai feng', 410200000000, '开封市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4103, 41, 1, '洛阳', 'l', 'luo yang', 410300000000, '洛阳市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4104, 41, 1, '平顶山', 'p', 'ping ding shan', 410400000000, '平顶山市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4105, 41, 1, '安阳', 'a', 'an yang', 410500000000, '安阳市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4106, 41, 1, '鹤壁', 'h', 'he bi', 410600000000, '鹤壁市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4107, 41, 1, '新乡', 'x', 'xin xiang', 410700000000, '新乡市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4108, 41, 1, '焦作', 'j', 'jiao zuo', 410800000000, '焦作市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4109, 41, 1, '濮阳', 'p', 'pu yang', 410900000000, '濮阳市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4110, 41, 1, '许昌', 'x', 'xu chang', 411000000000, '许昌市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4111, 41, 1, '漯河', 'l', 'luo he', 411100000000, '漯河市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4112, 41, 1, '三门峡', 's', 'san men xia', 411200000000, '三门峡市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4113, 41, 1, '南阳', 'n', 'nan yang', 411300000000, '南阳市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4114, 41, 1, '商丘', 's', 'shang qiu', 411400000000, '商丘市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4115, 41, 1, '信阳', 'x', 'xin yang', 411500000000, '信阳市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4116, 41, 1, '周口', 'z', 'zhou kou', 411600000000, '周口市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:35:59', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4117, 41, 1, '驻马店', 'z', 'zhu ma dian', 411700000000, '驻马店市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4201, 42, 1, '武汉', 'w', 'wu han', 420100000000, '武汉市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4202, 42, 1, '黄石', 'h', 'huang shi', 420200000000, '黄石市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4203, 42, 1, '十堰', 's', 'shi yan', 420300000000, '十堰市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4205, 42, 1, '宜昌', 'y', 'yi chang', 420500000000, '宜昌市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4206, 42, 1, '襄阳', 'x', 'xiang yang', 420600000000, '襄阳市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4207, 42, 1, '鄂州', 'e', 'e zhou', 420700000000, '鄂州市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4208, 42, 1, '荆门', 'j', 'jing men', 420800000000, '荆门市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4209, 42, 1, '孝感', 'x', 'xiao gan', 420900000000, '孝感市', 0, 'system', '2024-12-10 10:35:11', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4210, 42, 1, '荆州', 'j', 'jing zhou', 421000000000, '荆州市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4211, 42, 1, '黄冈', 'h', 'huang gang', 421100000000, '黄冈市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4212, 42, 1, '咸宁', 'x', 'xian ning', 421200000000, '咸宁市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4213, 42, 1, '随州', 's', 'sui zhou', 421300000000, '随州市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4228, 42, 1, '恩施', 'e', 'en shi', 422800000000, '恩施土家族苗族自治州', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4301, 43, 1, '长沙', 'c', 'chang sha', 430100000000, '长沙市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4302, 43, 1, '株洲', 'z', 'zhu zhou', 430200000000, '株洲市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4303, 43, 1, '湘潭', 'x', 'xiang tan', 430300000000, '湘潭市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4304, 43, 1, '衡阳', 'h', 'heng yang', 430400000000, '衡阳市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4305, 43, 1, '邵阳', 's', 'shao yang', 430500000000, '邵阳市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4306, 43, 1, '岳阳', 'y', 'yue yang', 430600000000, '岳阳市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4307, 43, 1, '常德', 'c', 'chang de', 430700000000, '常德市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4308, 43, 1, '张家界', 'z', 'zhang jia jie', 430800000000, '张家界市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4309, 43, 1, '益阳', 'y', 'yi yang', 430900000000, '益阳市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4310, 43, 1, '郴州', 'c', 'chen zhou', 431000000000, '郴州市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4311, 43, 1, '永州', 'y', 'yong zhou', 431100000000, '永州市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4312, 43, 1, '怀化', 'h', 'huai hua', 431200000000, '怀化市', 0, 'system', '2024-12-10 10:35:12', 'system', '2024-12-10 10:36:00', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4313, 43, 1, '娄底', 'l', 'lou di', 431300000000, '娄底市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4331, 43, 1, '湘西', 'x', 'xiang xi', 433100000000, '湘西土家族苗族自治州', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4401, 44, 1, '广州', 'g', 'guang zhou', 440100000000, '广州市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4402, 44, 1, '韶关', 's', 'shao guan', 440200000000, '韶关市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4403, 44, 1, '深圳', 's', 'shen zhen', 440300000000, '深圳市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4404, 44, 1, '珠海', 'z', 'zhu hai', 440400000000, '珠海市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4405, 44, 1, '汕头', 's', 'shan tou', 440500000000, '汕头市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4406, 44, 1, '佛山', 'f', 'fo shan', 440600000000, '佛山市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4407, 44, 1, '江门', 'j', 'jiang men', 440700000000, '江门市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4408, 44, 1, '湛江', 'z', 'zhan jiang', 440800000000, '湛江市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4409, 44, 1, '茂名', 'm', 'mao ming', 440900000000, '茂名市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4412, 44, 1, '肇庆', 'z', 'zhao qing', 441200000000, '肇庆市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4413, 44, 1, '惠州', 'h', 'hui zhou', 441300000000, '惠州市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4414, 44, 1, '梅州', 'm', 'mei zhou', 441400000000, '梅州市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4415, 44, 1, '汕尾', 's', 'shan wei', 441500000000, '汕尾市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4416, 44, 1, '河源', 'h', 'he yuan', 441600000000, '河源市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4417, 44, 1, '阳江', 'y', 'yang jiang', 441700000000, '阳江市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4418, 44, 1, '清远', 'q', 'qing yuan', 441800000000, '清远市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4419, 44, 1, '东莞', 'd', 'dong guan', 441900000000, '东莞市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4420, 44, 1, '中山', 'z', 'zhong shan', 442000000000, '中山市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4451, 44, 1, '潮州', 'c', 'chao zhou', 445100000000, '潮州市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4452, 44, 1, '揭阳', 'j', 'jie yang', 445200000000, '揭阳市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4453, 44, 1, '云浮', 'y', 'yun fu', 445300000000, '云浮市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4501, 45, 1, '南宁', 'n', 'nan ning', 450100000000, '南宁市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4502, 45, 1, '柳州', 'l', 'liu zhou', 450200000000, '柳州市', 0, 'system', '2024-12-10 10:35:13', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4503, 45, 1, '桂林', 'g', 'gui lin', 450300000000, '桂林市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4504, 45, 1, '梧州', 'w', 'wu zhou', 450400000000, '梧州市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4505, 45, 1, '北海', 'b', 'bei hai', 450500000000, '北海市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4506, 45, 1, '防城港', 'f', 'fang cheng gang', 450600000000, '防城港市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4507, 45, 1, '钦州', 'q', 'qin zhou', 450700000000, '钦州市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4508, 45, 1, '贵港', 'g', 'gui gang', 450800000000, '贵港市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4509, 45, 1, '玉林', 'y', 'yu lin', 450900000000, '玉林市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4510, 45, 1, '百色', 'b', 'bai se', 451000000000, '百色市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4511, 45, 1, '贺州', 'h', 'he zhou', 451100000000, '贺州市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:01', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4512, 45, 1, '河池', 'h', 'he chi', 451200000000, '河池市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4513, 45, 1, '来宾', 'l', 'lai bin', 451300000000, '来宾市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4514, 45, 1, '崇左', 'c', 'chong zuo', 451400000000, '崇左市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4601, 46, 1, '海口', 'h', 'hai kou', 460100000000, '海口市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4602, 46, 1, '三亚', 's', 'san ya', 460200000000, '三亚市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4603, 46, 1, '三沙', 's', 'san sha', 460300000000, '三沙市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(4604, 46, 1, '儋州', 'd', 'dan zhou', 460400000000, '儋州市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5001, 50, 1, '重庆城区', 'c', 'chong qing cheng qu', 500100000000, '重庆城区', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5002, 50, 1, '重庆郊县', 'c', 'chong qing jiao xian', 500200000000, '重庆郊县', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5101, 51, 1, '成都', 'c', 'cheng du', 510100000000, '成都市', 0, 'system', '2024-12-10 10:35:14', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5103, 51, 1, '自贡', 'z', 'zi gong', 510300000000, '自贡市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5104, 51, 1, '攀枝花', 'p', 'pan zhi hua', 510400000000, '攀枝花市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5105, 51, 1, '泸州', 'l', 'lu zhou', 510500000000, '泸州市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5106, 51, 1, '德阳', 'd', 'de yang', 510600000000, '德阳市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5107, 51, 1, '绵阳', 'm', 'mian yang', 510700000000, '绵阳市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5108, 51, 1, '广元', 'g', 'guang yuan', 510800000000, '广元市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5109, 51, 1, '遂宁', 's', 'sui ning', 510900000000, '遂宁市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5110, 51, 1, '内江', 'n', 'nei jiang', 511000000000, '内江市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5111, 51, 1, '乐山', 'l', 'le shan', 511100000000, '乐山市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5113, 51, 1, '南充', 'n', 'nan chong', 511300000000, '南充市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5114, 51, 1, '眉山', 'm', 'mei shan', 511400000000, '眉山市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5115, 51, 1, '宜宾', 'y', 'yi bin', 511500000000, '宜宾市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5116, 51, 1, '广安', 'g', 'guang an', 511600000000, '广安市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5117, 51, 1, '达州', 'd', 'da zhou', 511700000000, '达州市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5118, 51, 1, '雅安', 'y', 'ya an', 511800000000, '雅安市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5119, 51, 1, '巴中', 'b', 'ba zhong', 511900000000, '巴中市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5120, 51, 1, '资阳', 'z', 'zi yang', 512000000000, '资阳市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5132, 51, 1, '阿坝', 'a', 'a ba', 513200000000, '阿坝藏族羌族自治州', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5133, 51, 1, '甘孜', 'g', 'gan zi', 513300000000, '甘孜藏族自治州', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:02', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5134, 51, 1, '凉山', 'l', 'liang shan', 513400000000, '凉山彝族自治州', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5201, 52, 1, '贵阳', 'g', 'gui yang', 520100000000, '贵阳市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5202, 52, 1, '六盘水', 'l', 'liu pan shui', 520200000000, '六盘水市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5203, 52, 1, '遵义', 'z', 'zun yi', 520300000000, '遵义市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5204, 52, 1, '安顺', 'a', 'an shun', 520400000000, '安顺市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5205, 52, 1, '毕节', 'b', 'bi jie', 520500000000, '毕节市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5206, 52, 1, '铜仁', 't', 'tong ren', 520600000000, '铜仁市', 0, 'system', '2024-12-10 10:35:15', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5223, 52, 1, '黔西南', 'q', 'qian xi nan', 522300000000, '黔西南布依族苗族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5226, 52, 1, '黔东南', 'q', 'qian dong nan', 522600000000, '黔东南苗族侗族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5227, 52, 1, '黔南', 'q', 'qian nan', 522700000000, '黔南布依族苗族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5301, 53, 1, '昆明', 'k', 'kun ming', 530100000000, '昆明市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5303, 53, 1, '曲靖', 'q', 'qu jing', 530300000000, '曲靖市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5304, 53, 1, '玉溪', 'y', 'yu xi', 530400000000, '玉溪市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5305, 53, 1, '保山', 'b', 'bao shan', 530500000000, '保山市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5306, 53, 1, '昭通', 'z', 'zhao tong', 530600000000, '昭通市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5307, 53, 1, '丽江', 'l', 'li jiang', 530700000000, '丽江市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5308, 53, 1, '普洱', 'p', 'pu er', 530800000000, '普洱市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5309, 53, 1, '临沧', 'l', 'lin cang', 530900000000, '临沧市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5323, 53, 1, '楚雄', 'c', 'chu xiong', 532300000000, '楚雄彝族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5325, 53, 1, '红河', 'h', 'hong he', 532500000000, '红河哈尼族彝族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5326, 53, 1, '文山', 'w', 'wen shan', 532600000000, '文山壮族苗族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5328, 53, 1, '西双版纳', 'x', 'xi shuang ban na', 532800000000, '西双版纳傣族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5329, 53, 1, '大理', 'd', 'da li', 532900000000, '大理白族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5331, 53, 1, '德宏', 'd', 'de hong', 533100000000, '德宏傣族景颇族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5333, 53, 1, '怒江', 'n', 'nu jiang', 533300000000, '怒江傈僳族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5334, 53, 1, '迪庆', 'd', 'di qing', 533400000000, '迪庆藏族自治州', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5401, 54, 1, '拉萨', 'l', 'la sa', 540100000000, '拉萨市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5402, 54, 1, '日喀则', 'r', 'ri ka ze', 540200000000, '日喀则市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5403, 54, 1, '昌都', 'c', 'chang du', 540300000000, '昌都市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:03', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5404, 54, 1, '林芝', 'l', 'lin zhi', 540400000000, '林芝市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5405, 54, 1, '山南', 's', 'shan nan', 540500000000, '山南市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5406, 54, 1, '那曲', 'n', 'na qu', 540600000000, '那曲市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(5425, 54, 1, '阿里', 'a', 'a li', 542500000000, '阿里地区', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6101, 61, 1, '西安', 'x', 'xi an', 610100000000, '西安市', 0, 'system', '2024-12-10 10:35:16', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6102, 61, 1, '铜川', 't', 'tong chuan', 610200000000, '铜川市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6103, 61, 1, '宝鸡', 'b', 'bao ji', 610300000000, '宝鸡市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6104, 61, 1, '咸阳', 'x', 'xian yang', 610400000000, '咸阳市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6105, 61, 1, '渭南', 'w', 'wei nan', 610500000000, '渭南市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6106, 61, 1, '延安', 'y', 'yan an', 610600000000, '延安市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6107, 61, 1, '汉中', 'h', 'han zhong', 610700000000, '汉中市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6108, 61, 1, '榆林', 'y', 'yu lin', 610800000000, '榆林市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6109, 61, 1, '安康', 'a', 'an kang', 610900000000, '安康市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6110, 61, 1, '商洛', 's', 'shang luo', 611000000000, '商洛市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6201, 62, 1, '兰州', 'l', 'lan zhou', 620100000000, '兰州市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6202, 62, 1, '嘉峪关', 'j', 'jia yu guan', 620200000000, '嘉峪关市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6203, 62, 1, '金昌', 'j', 'jin chang', 620300000000, '金昌市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6204, 62, 1, '白银', 'b', 'bai yin', 620400000000, '白银市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6205, 62, 1, '天水', 't', 'tian shui', 620500000000, '天水市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6206, 62, 1, '武威', 'w', 'wu wei', 620600000000, '武威市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6207, 62, 1, '张掖', 'z', 'zhang ye', 620700000000, '张掖市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6208, 62, 1, '平凉', 'p', 'ping liang', 620800000000, '平凉市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6209, 62, 1, '酒泉', 'j', 'jiu quan', 620900000000, '酒泉市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6210, 62, 1, '庆阳', 'q', 'qing yang', 621000000000, '庆阳市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6211, 62, 1, '定西', 'd', 'ding xi', 621100000000, '定西市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6212, 62, 1, '陇南', 'l', 'long nan', 621200000000, '陇南市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6229, 62, 1, '临夏', 'l', 'lin xia', 622900000000, '临夏回族自治州', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:04', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6230, 62, 1, '甘南', 'g', 'gan nan', 623000000000, '甘南藏族自治州', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6301, 63, 1, '西宁', 'x', 'xi ning', 630100000000, '西宁市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6302, 63, 1, '海东', 'h', 'hai dong', 630200000000, '海东市', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6322, 63, 1, '海北', 'h', 'hai bei', 632200000000, '海北藏族自治州', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6323, 63, 1, '黄南', 'h', 'huang nan', 632300000000, '黄南藏族自治州', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6325, 63, 1, '海南', 'h', 'hai nan', 632500000000, '海南藏族自治州', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6326, 63, 1, '果洛', 'g', 'guo luo', 632600000000, '果洛藏族自治州', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6327, 63, 1, '玉树', 'y', 'yu shu', 632700000000, '玉树藏族自治州', 0, 'system', '2024-12-10 10:35:17', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6328, 63, 1, '海西', 'h', 'hai xi', 632800000000, '海西蒙古族藏族自治州', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6401, 64, 1, '银川', 'y', 'yin chuan', 640100000000, '银川市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6402, 64, 1, '石嘴山', 's', 'shi zui shan', 640200000000, '石嘴山市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6403, 64, 1, '吴忠', 'w', 'wu zhong', 640300000000, '吴忠市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6404, 64, 1, '固原', 'g', 'gu yuan', 640400000000, '固原市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6405, 64, 1, '中卫', 'z', 'zhong wei', 640500000000, '中卫市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6501, 65, 1, '乌鲁木齐', 'w', 'wu lu mu qi', 650100000000, '乌鲁木齐市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6502, 65, 1, '克拉玛依', 'k', 'ke la ma yi', 650200000000, '克拉玛依市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6504, 65, 1, '吐鲁番', 't', 'tu lu fan', 650400000000, '吐鲁番市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6505, 65, 1, '哈密', 'h', 'ha mi', 650500000000, '哈密市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6523, 65, 1, '昌吉', 'c', 'chang ji', 652300000000, '昌吉回族自治州', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6527, 65, 1, '博尔塔拉', 'b', 'bo er ta la', 652700000000, '博尔塔拉蒙古自治州', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6528, 65, 1, '巴音郭楞', 'b', 'ba yin guo leng', 652800000000, '巴音郭楞蒙古自治州', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6529, 65, 1, '阿克苏', 'a', 'a ke su', 652900000000, '阿克苏地区', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6530, 65, 1, '克孜勒苏', 'k', 'ke zi le su', 653000000000, '克孜勒苏柯尔克孜自治州', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6531, 65, 1, '喀什', 'k', 'ka shi', 653100000000, '喀什地区', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6532, 65, 1, '和田', 'h', 'he tian', 653200000000, '和田地区', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6540, 65, 1, '伊犁', 'y', 'yi li', 654000000000, '伊犁哈萨克自治州', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6542, 65, 1, '塔城', 't', 'ta cheng', 654200000000, '塔城地区', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(6543, 65, 1, '阿勒泰', 'a', 'a le tai', 654300000000, '阿勒泰地区', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7101, 71, 1, '台北', 't', 'tai bei', 710100000000, '台北市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7102, 71, 1, '高雄', 'g', 'gao xiong', 710200000000, '高雄市', 0, 'system', '2024-12-10 10:35:18', 'system', '2024-12-10 10:36:05', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7103, 71, 1, '台南', 't', 'tai nan', 710300000000, '台南市', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7104, 71, 1, '台中', 't', 'tai zhong', 710400000000, '台中市', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7106, 71, 1, '南投', 'n', 'nan tou', 710600000000, '南投县', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7107, 71, 1, '基隆', 'j', 'ji long', 710700000000, '基隆市', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7108, 71, 1, '新竹市', 'x', 'xin zhu shi', 710800000000, '新竹市', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7109, 71, 1, '嘉义市', 'j', 'jia yi shi', 710900000000, '嘉义市', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7111, 71, 1, '新北', 'x', 'xin bei', 711100000000, '新北市', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7112, 71, 1, '宜兰', 'y', 'yi lan', 711200000000, '宜兰县', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7113, 71, 1, '新竹县', 'x', 'xin zhu xian', 711300000000, '新竹县', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7114, 71, 1, '桃园', 't', 'tao yuan', 711400000000, '桃园市', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7115, 71, 1, '苗栗', 'm', 'miao li', 711500000000, '苗栗县', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7117, 71, 1, '彰化', 'z', 'zhang hua', 711700000000, '彰化县', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:06', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7119, 71, 1, '嘉义县', 'j', 'jia yi xian', 711900000000, '嘉义县', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7121, 71, 1, '云林', 'y', 'yun lin', 712100000000, '云林县', 0, 'system', '2024-12-10 10:35:19', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7124, 71, 1, '屏东', 'p', 'ping dong', 712400000000, '屏东县', 0, 'system', '2024-12-10 10:35:20', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7125, 71, 1, '台东', 't', 'tai dong', 712500000000, '台东县', 0, 'system', '2024-12-10 10:35:20', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7126, 71, 1, '花莲', 'h', 'hua lian', 712600000000, '花莲县', 0, 'system', '2024-12-10 10:35:20', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(7127, 71, 1, '澎湖', 'p', 'peng hu', 712700000000, '澎湖县', 0, 'system', '2024-12-10 10:35:20', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(8100, 81, 1, '香港', 'x', 'xiang gang', 810000000000, '香港特别行政区', 0, 'system', '2024-12-10 10:35:20', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(8200, 82, 1, '澳门', 'a', 'ao men', 820000000000, '澳门特别行政区', 0, 'system', '2024-12-10 10:35:20', 'system', '2024-12-10 10:36:07', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(110101, 1101, 2, '东城', 'd', 'dong cheng', 110101000000, '东城区', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(110102, 1101, 2, '西城', 'x', 'xi cheng', 110102000000, '西城区', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(110105, 1101, 2, '朝阳', 'c', 'chao yang', 110105000000, '朝阳区', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(110106, 1101, 2, '丰台', 'f', 'feng tai', 110106000000, '丰台区', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(110107, 1101, 2, '石景山', 's', 'shi jing shan', 110107000000, '石景山区', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO country_address
(id, pid, deep, name, pinyin_prefix, pinyin, ext_id, ext_name, deleted, create_by, create_time, update_by, update_time, tenant)
VALUES(110108, 1101, 2, '海淀', 'h', 'hai dian', 110108000000, '海淀区', 0, 'system', '2024-12-10 10:35:05', 'system', '2024-12-10 10:35:52', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
