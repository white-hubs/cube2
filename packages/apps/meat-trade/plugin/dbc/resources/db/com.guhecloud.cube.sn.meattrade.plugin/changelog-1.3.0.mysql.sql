-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'merchant_spot_check_stat') AS column_exists;
CREATE TABLE `merchant_spot_check_stat` (
  `id` bigint(20) unsigned NOT NULL,
  `merchant_id` varchar(50) DEFAULT NULL COMMENT '商户id',
  `total_qty` int(10) unsigned DEFAULT NULL COMMENT '抽检总次数',
  `pass_qty` int(10) unsigned DEFAULT NULL COMMENT '通过总次数',
  `pass_rate` varchar(50) DEFAULT NULL COMMENT '检测合格率',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户食安抽检统计';
