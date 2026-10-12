-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'metric_score') AS column_exists;
CREATE TABLE `metric_score` (
  `id` bigint(20) unsigned NOT NULL,
  `request_id` varchar(100) NOT NULL COMMENT '每次生成的请求id',
  `app_code` varchar(100) DEFAULT NULL,
  `topic` varchar(100) NOT NULL COMMENT '主题',
  `topic_concat` varchar(300) DEFAULT NULL COMMENT '主题concat',
  `score` decimal(5,2) DEFAULT NULL COMMENT '分数',
  `weight_score` decimal(5,2) DEFAULT NULL COMMENT '权重得分',
  `suggestion` varchar(500) DEFAULT NULL COMMENT '建议',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `data` text COMMENT '指标项详情',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指标分数';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'metric_result') AS column_exists;
CREATE TABLE `metric_result`
(
    `id`          bigint(20) unsigned NOT NULL,
    `request_id`  varchar(100) NOT NULL COMMENT '每次生成的请求id',
    `app_code`    varchar(100) DEFAULT NULL,
    `metric`      varchar(100) NOT NULL COMMENT '指标',
    `begin_time`  datetime     DEFAULT NULL COMMENT '开始时间',
    `end_time`    datetime     DEFAULT NULL COMMENT '结束时间',
    `data`        text COMMENT '指标结果',
    `tenant`      int(10) unsigned DEFAULT NULL,
    `deleted`     bigint(20) unsigned DEFAULT NULL,
    `create_by`   varchar(20)  DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_by`   varchar(20)  DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指标项结果';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_report_score') AS column_exists;
CREATE TABLE `safety_report_score`
(
    `id`          bigint(20) unsigned NOT NULL,
    `request_id`  varchar(100)  DEFAULT NULL COMMENT '请求id',
    `name`        varchar(200)  DEFAULT NULL COMMENT '报告名称',
    `begin_time`  datetime      DEFAULT NULL COMMENT '开始时间',
    `end_time`    datetime      DEFAULT NULL COMMENT '结束时间',
    `score`       decimal(5, 2) DEFAULT NULL COMMENT '分数',
    `data`        mediumtext COMMENT '报告内容',
    `tenant`      int(10) unsigned DEFAULT NULL,
    `deleted`     bigint(20) DEFAULT NULL,
    `create_by`   varchar(20)   DEFAULT NULL,
    `create_time` datetime      DEFAULT NULL,
    `update_by`   varbinary(20) DEFAULT NULL,
    `update_time` datetime      DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='安全分析报告';
