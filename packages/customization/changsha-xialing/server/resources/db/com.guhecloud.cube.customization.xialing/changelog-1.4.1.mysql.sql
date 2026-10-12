-- liquibase formatted sql

-- changeset changsha:warehouse-temperature-trend-config-add-alarm-threshold-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_config' AND column_name = 'high_alarm_threshold';
ALTER TABLE `warehouse_temperature_trend_config`
  ADD COLUMN `high_alarm_threshold` decimal(10,2) NULL COMMENT '全仓超温预警线' AFTER `warehouse_id`;

-- changeset changsha:warehouse-temperature-trend-config-add-low-threshold-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_config' AND column_name = 'low_alarm_threshold';
ALTER TABLE `warehouse_temperature_trend_config`
  ADD COLUMN `low_alarm_threshold` decimal(10,2) NULL COMMENT '全仓低温预警线' AFTER `high_alarm_threshold`;

-- changeset changsha:warehouse-temperature-trend-config-fill-alarm-threshold-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:2 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_config' AND column_name IN ('high_alarm_threshold', 'low_alarm_threshold');
UPDATE `warehouse_temperature_trend_config`
SET `high_alarm_threshold` = IFNULL(`high_alarm_threshold`, 0),
    `low_alarm_threshold` = IFNULL(`low_alarm_threshold`, 0);

-- changeset changsha:warehouse-temperature-trend-config-not-null-alarm-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:2 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_config' AND column_name IN ('high_alarm_threshold', 'low_alarm_threshold');
ALTER TABLE `warehouse_temperature_trend_config`
  MODIFY COLUMN `high_alarm_threshold` decimal(10,2) NOT NULL COMMENT '全仓超温预警线',
  MODIFY COLUMN `low_alarm_threshold` decimal(10,2) NOT NULL COMMENT '全仓低温预警线';

-- changeset changsha:warehouse-temperature-trend-config-agg-comment-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_config' AND column_name = 'warehouse_agg_type';
ALTER TABLE `warehouse_temperature_trend_config`
  MODIFY COLUMN `warehouse_agg_type` varchar(20) NOT NULL COMMENT '全仓温度汇总取值规则：MAX-最高温，MIN-最低温';

-- changeset changsha:warehouse-temperature-trend-layer-not-null-alarm-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:2 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_layer_config' AND column_name IN ('high_alarm_threshold', 'low_alarm_threshold');
ALTER TABLE `warehouse_temperature_trend_layer_config`
  MODIFY COLUMN `high_alarm_threshold` decimal(10,2) NOT NULL COMMENT '超温预警线',
  MODIFY COLUMN `low_alarm_threshold` decimal(10,2) NOT NULL COMMENT '低温预警线';

-- changeset changsha:warehouse-temperature-trend-layer-agg-comment-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_layer_config' AND column_name = 'layer_agg_type';
ALTER TABLE `warehouse_temperature_trend_layer_config`
  MODIFY COLUMN `layer_agg_type` varchar(20) NOT NULL COMMENT '分层温度汇总取值规则：MAX-最高温，MIN-最低温';
