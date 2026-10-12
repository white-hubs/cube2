-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_calendar_holiday' ) AS column_exists;
CREATE table IF NOT EXISTS `att_calendar_holiday` (
    `id` bigint(20) NOT NULL COMMENT 'id',
    `day_time` date NOT NULL COMMENT '班次类型',
    `day_format` varchar(20) NOT NULL COMMENT '班次类型',
    `year` int(11) NOT NULL COMMENT '年',
    `month` int(11) NOT NULL COMMENT '一年的第几月',
    `week` int(11) NOT NULL COMMENT '一周的第几天',
    `day` int(11) NOT NULL COMMENT '一月的第几天',
    `holiday` char(1) NOT NULL COMMENT '休息日true表示休息，false表示上班(除周末和节日，周一到五和调班都是上班)',
    `holiday_type` int(11) NOT NULL COMMENT '(0, 1, 2, 3),  节假日类型，分别表示 工作日、周末、节日、调班。',
    `tenant` int(10) NOT NULL COMMENT '租户主键',
    `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
    `create_by` varchar(100) NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_by` varchar(100) NOT NULL COMMENT '更新用户',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='日历节假日表';