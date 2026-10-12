-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'meat_farm_supply_ranking_stat') AS column_exists;
CREATE TABLE `meat_farm_supply_ranking_stat` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `time_range` varchar(100) DEFAULT NULL COMMENT '时间范围',
  `start_date` datetime DEFAULT NULL COMMENT '周期开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '周期结束时间',
  `total_tally_quantity` int(10) unsigned DEFAULT NULL COMMENT '供货片数',
  `total_tally_weight` decimal(16,2) unsigned DEFAULT NULL COMMENT '供货重量',
  `meat_farm_code` varchar(64) DEFAULT NULL COMMENT '屠宰场编号',
  `meat_farm_name` varchar(100) DEFAULT NULL COMMENT '屠宰场名称',
  `prev_total_tally_quantity` int(10) unsigned DEFAULT NULL COMMENT '上周期供货片数',
  `prev_total_tally_weight` decimal(16,2) unsigned DEFAULT NULL COMMENT '上期供货重量',
  `tally_quantity_rate` varchar(20) DEFAULT NULL COMMENT '供货片数环比',
  `tally_weight_rate` varchar(20) DEFAULT NULL COMMENT '供货重量环比',
  `seller_count_rate` varchar(20) DEFAULT NULL COMMENT '供货商户数环比',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户ID',
  `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_time_range_code_tenant_deleted` (`time_range`,`meat_farm_code`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='白条来货数据统计信息';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'meat_testing_ranking_stat') AS column_exists;
CREATE TABLE `meat_testing_ranking_stat` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `time_range` varchar(100) DEFAULT NULL COMMENT '时间范围',
  `start_date` datetime DEFAULT NULL COMMENT '周期开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '周期结束时间',
  `meat_testing_item` varchar(64) DEFAULT NULL COMMENT '检测项目id',
  `meat_testing_item_code` varchar(100) DEFAULT NULL COMMENT '检测项目编号',
  `meat_testing_item_name` varchar(100) DEFAULT NULL COMMENT '检测项目名称',
  `total_check_num_sum` int(10) unsigned DEFAULT NULL COMMENT '总检查片数',
  `total_sum` int(10) unsigned DEFAULT NULL COMMENT '总检查批次',
  `not_pass_sum` int(10) unsigned DEFAULT NULL COMMENT '不合格批次数',
  `prev_total_check_num_sum` int(10) unsigned DEFAULT NULL COMMENT '上周期检查片数',
  `prev_total_sum` int(10) unsigned DEFAULT NULL COMMENT '上周期检查批次',
  `prev_not_pass_sum` int(10) unsigned DEFAULT NULL COMMENT '上周期不合格批次数',
  `check_num_sum_rate` varchar(20) DEFAULT NULL COMMENT '检查数量环比',
  `total_sum_rate` varchar(20) DEFAULT NULL COMMENT '检查批次环比',
  `not_pass_sum_rate` varchar(20) DEFAULT NULL COMMENT '不合格数量环比',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户ID',
  `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_time_rang_item_tenant_deleted` (`time_range`,`meat_testing_item`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='食安数据统计信息';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'seller_supply_sum_stat') AS column_exists;
CREATE TABLE `seller_supply_sum_stat` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `time_range` varchar(100) DEFAULT NULL COMMENT '时间范围',
  `start_date` datetime DEFAULT NULL COMMENT '周期开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '周期结束时间',
  `total_tally_quantity` int(10) unsigned DEFAULT NULL COMMENT '供货片数',
  `total_tally_weight` decimal(16,3) unsigned DEFAULT NULL COMMENT '供货重量',
  `total_tally_count` int(10) unsigned DEFAULT NULL COMMENT '理货总数（来货总数）',
  `total_seller_count` int(10) unsigned DEFAULT NULL COMMENT '供货商户数',
  `prev_total_tally_quantity` int(10) unsigned DEFAULT NULL COMMENT '上周期供货片数',
  `prev_total_tally_weight` decimal(16,3) unsigned DEFAULT NULL COMMENT '上周期供货重量',
  `prev_total_tally_count` int(10) unsigned DEFAULT NULL COMMENT '上周期理货总数',
  `prev_seller_count` int(10) unsigned DEFAULT NULL COMMENT '上周期供货商户数',
  `tally_quantity_rate` varchar(20) DEFAULT NULL COMMENT '供货片数环比',
  `tally_weight_rate` varchar(20) DEFAULT NULL COMMENT '供货重量环比',
  `tally_count_rate` varchar(20) DEFAULT NULL COMMENT '理货数环比',
  `seller_count_rate` varchar(20) DEFAULT NULL COMMENT '供货商户数环比',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户ID',
  `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_time_range_tenant_deleted` (`time_range`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='供货数据统计信息';

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'sellers_comparison_top_stat') AS column_exists;
CREATE TABLE `sellers_comparison_top_stat` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `time_range` varchar(100) DEFAULT NULL COMMENT '时间范围',
  `start_date` datetime DEFAULT NULL COMMENT '周期开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '周期结束时间',
  `total_net` decimal(16,3) unsigned DEFAULT NULL COMMENT '交易净重',
  `total_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易实际金额',
  `seller` varchar(64) NOT NULL COMMENT '商家id',
  `seller_name` varchar(100) NOT NULL COMMENT '商家名称',
  `prev_total_net` decimal(16,3) unsigned DEFAULT NULL COMMENT '上周期交易净重',
  `prev_total_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易实际金额',
  `net_rate` varchar(20) DEFAULT NULL COMMENT '交易净重环比',
  `payable_rate` varchar(20) DEFAULT NULL COMMENT '交易实际金额环比',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户ID',
  `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_time_range_seller_tenant_deleted` (`time_range`,`seller`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='白条交易大户统计信息';

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'transaction_comparison_stat') AS column_exists;
CREATE TABLE `transaction_comparison_stat` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `time_range` varchar(100) DEFAULT NULL COMMENT '时间范围',
  `start_date` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '结束时间',
  `total_quantity` int(10) unsigned DEFAULT NULL COMMENT '交易片数',
  `total_weight` decimal(16,3) unsigned DEFAULT NULL COMMENT '交易重量',
  `total_net` decimal(16,3) unsigned DEFAULT NULL COMMENT '交易净重',
  `total_amount` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易金额',
  `total_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易实际金额',
  `total_seller_fee` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易佣金',
  `avg_amount` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易均价',
  `avg_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易实际均价',
  `prev_total_quantity` int(10) unsigned DEFAULT NULL COMMENT '上周期交易片数',
  `prev_total_weight` decimal(16,3) unsigned DEFAULT NULL COMMENT '上周期交易重量',
  `prev_total_net` decimal(16,3) unsigned DEFAULT NULL COMMENT '上周期交易净重',
  `prev_total_amount` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易金额',
  `prev_total_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易实际金额',
  `prev_total_seller_fee` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易佣金',
  `prev_avg_amount` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易均价',
  `prev_avg_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易实际均价',
  `quantity_rate` varchar(20) DEFAULT NULL COMMENT '交易片数环比',
  `weight_rate` varchar(20) DEFAULT NULL COMMENT '交易重量环比',
  `net_rate` varchar(20) DEFAULT NULL COMMENT '交易净重环比',
  `amount_rate` varchar(20) DEFAULT NULL COMMENT '交易金额环比',
  `payable_rate` varchar(20) DEFAULT NULL COMMENT '交易实际金额环比',
  `seller_fee_rate` varchar(20) DEFAULT NULL COMMENT '交易佣金环比',
  `avg_amount_rate` varchar(20) DEFAULT NULL COMMENT '交易均价环比',
  `avg_payable_rate` varchar(20) DEFAULT NULL COMMENT '实际均价环比',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户ID',
  `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_time_range_tenant_deleted` (`time_range`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='交易信息统计';

-- changeset lanchun:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'transaction_comparison_stat_detail') AS column_exists;
CREATE TABLE `transaction_comparison_stat_detail` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `time_range` varchar(100) DEFAULT NULL COMMENT '时间范围',
  `start_date` datetime DEFAULT NULL COMMENT '当前时间',
  `prev_start_date` datetime DEFAULT NULL COMMENT '上周期时间',
  `total_quantity` int(10) unsigned DEFAULT '0' COMMENT '交易片数',
  `total_weight` decimal(16,3) unsigned DEFAULT NULL COMMENT '交易重量',
  `total_net` decimal(16,3) unsigned DEFAULT NULL COMMENT '交易净重',
  `total_amount` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易金额',
  `total_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易实际金额',
  `total_seller_fee` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易佣金',
  `avg_amount` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易均价',
  `avg_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '交易实际均价',
  `prev_total_quantity` int(10) unsigned DEFAULT '0' COMMENT '上周期交易片数',
  `prev_total_weight` decimal(16,3) unsigned DEFAULT NULL COMMENT '上周期交易重量',
  `prev_total_net` decimal(16,3) unsigned DEFAULT NULL COMMENT '上周期交易净重',
  `prev_total_amount` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易金额',
  `prev_total_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易实际金额',
  `prev_total_seller_fee` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易佣金',
  `prev_avg_amount` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易均价',
  `prev_avg_payable` decimal(16,2) unsigned DEFAULT NULL COMMENT '上周期交易实际均价',
  `quantity_rate` varchar(20) DEFAULT NULL COMMENT '交易片数环比',
  `weight_rate` varchar(20) DEFAULT NULL COMMENT '交易重量环比',
  `net_rate` varchar(20) DEFAULT NULL COMMENT '交易净重环比',
  `amount_rate` varchar(20) DEFAULT NULL COMMENT '交易金额环比',
  `payable_rate` varchar(20) DEFAULT NULL COMMENT '交易实际金额环比',
  `seller_fee_rate` varchar(20) DEFAULT NULL COMMENT '交易佣金环比',
  `avg_amount_rate` varchar(20) DEFAULT NULL COMMENT '交易均价环比',
  `avg_payable_rate` varchar(20) DEFAULT NULL COMMENT '实际均价环比',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户ID',
  `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='交易数据详细统计';
