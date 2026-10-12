-- liquibase formatted sql

-- changeset lijie:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger_extend_info') AS column_exists;
CREATE TABLE `risk_source_ledger_extend_info`
(
    `id`                bigint(11) NOT NULL,
    `ledger_id`         bigint(11) NOT NULL COMMENT '主表id',
    `receive_user_type` varchar(255) DEFAULT NULL COMMENT '使用人类型',
    `purchasing_date`   datetime     DEFAULT NULL COMMENT '购买日期',
    `efficiency`        varchar(255) DEFAULT NULL COMMENT '功率',
    `plate_number`      varchar(255) DEFAULT NULL COMMENT '车牌号',
    `insurance`         varchar(255) DEFAULT NULL COMMENT '是否购买保险',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;