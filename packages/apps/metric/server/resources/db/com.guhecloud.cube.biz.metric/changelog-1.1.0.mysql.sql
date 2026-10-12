-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_coefficient') AS column_exists;
CREATE TABLE `rule_coefficient` (
  `id` bigint(20) unsigned NOT NULL,
  `rule_id` bigint(20) unsigned NOT NULL COMMENT '规则id',
  `greater_than` decimal(5,2) DEFAULT NULL COMMENT '大于',
  `less_than_or_equal` decimal(5,2) DEFAULT NULL COMMENT '小于等于',
  `score_ratio` decimal(5,2) DEFAULT NULL COMMENT '得分占比',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规则系数';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'metric_library') AS column_exists;
CREATE TABLE `metric_library` (
  `id` bigint(20) unsigned NOT NULL,
  `code` varchar(50) NOT NULL COMMENT '指标编号',
  `name` varchar(50) NOT NULL COMMENT '指标编号',
  `compute_type` varchar(50) DEFAULT NULL COMMENT '计算方式',
  `initialized` tinyint(1) DEFAULT NULL COMMENT '是否为初始化的',
  `compute_formula` varchar(500) DEFAULT NULL COMMENT '指标计算表达式，当计算方式为多项指标组合结果时使用',
  `compute_formula_metrics` varchar(500) DEFAULT NULL COMMENT '参与计算的指标id集合(多个用,隔开)',
  `executable_formula` varchar(500) DEFAULT NULL COMMENT '后端生成的执行表达式（含嵌套解析）',
  `all_base_metric_ids` varchar(500) DEFAULT NULL COMMENT '所有基础指标ID（递归展开后，如"1,2,456"）',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指标库';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_library') AS column_exists;
CREATE TABLE `rule_library` (
  `id` bigint(20) unsigned NOT NULL,
  `item_code` varchar(100) NOT NULL COMMENT '每次生成的请求id',
  `item_name` varchar(100) DEFAULT NULL,
  `item_type` varchar(100) NOT NULL COMMENT '主题',
  `parent_id` bigint(20) unsigned DEFAULT NULL COMMENT '父级id',
  `level` int(5) unsigned DEFAULT NULL COMMENT '层级',
  `sequence` varchar(500) DEFAULT NULL COMMENT '序列号',
  `item_code_concat` varchar(300) DEFAULT NULL COMMENT '主题concat',
  `weight_allot` decimal(5,2) DEFAULT NULL COMMENT '权重',
  `compute_metric` bigint(20) unsigned DEFAULT NULL COMMENT '计算指标',
  `rule_content_template` varchar(500) DEFAULT NULL COMMENT '规则内容模版',
  `template_metric_variables` varchar(500) DEFAULT NULL COMMENT '模版指标变量keys',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规则库';

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_score') AS column_exists;
CREATE TABLE `rule_score` (
  `id` bigint(20) unsigned NOT NULL,
  `request_id` varchar(100) NOT NULL COMMENT '每次生成的请求id',
  `topic` varchar(100) NOT NULL COMMENT '主题',
  `topic_concat` varchar(300) DEFAULT NULL COMMENT '主题concat',
  `score` decimal(5,2) DEFAULT NULL COMMENT '分数',
  `weight_score` decimal(5,2) DEFAULT NULL COMMENT '权重得分',
  `suggestion` varchar(500) DEFAULT NULL COMMENT '建议',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `data` blob COMMENT '规则详情',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规则分数';

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'safety_report_score' AND column_name = 'data' ) AS column_exists;
ALTER TABLE `safety_report_score` MODIFY COLUMN `data` BLOB;
