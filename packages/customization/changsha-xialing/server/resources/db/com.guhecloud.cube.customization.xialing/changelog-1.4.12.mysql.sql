-- liquibase formatted sql

-- changeset changsha:drone-alarm-dispatch-capture-config-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'drone_alarm_dispatch_config' AND column_name = 'media_action';
ALTER TABLE `drone_alarm_dispatch_config`
  ADD COLUMN `media_action` varchar(32) DEFAULT NULL COMMENT '到点执行动作: photo拍照 video录像 空不执行' AFTER `scene_code`,
  ADD COLUMN `gimbal_yt_type` int(11) DEFAULT NULL COMMENT '云台挂载类型' AFTER `media_action`,
  ADD COLUMN `arrival_distance_threshold_meters` decimal(18,2) DEFAULT NULL COMMENT '到点距离阈值(米)' AFTER `gimbal_yt_type`,
  ADD COLUMN `arrival_speed_threshold_cmps` decimal(18,2) DEFAULT NULL COMMENT '到点水平速度阈值(厘米/秒)' AFTER `arrival_distance_threshold_meters`,
  ADD COLUMN `video_duration_seconds` int(11) DEFAULT NULL COMMENT '录像持续时长(秒)' AFTER `arrival_speed_threshold_cmps`;

-- changeset changsha:drone-alarm-dispatch-capture-record-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'drone_alarm_dispatch_record' AND column_name = 'media_action';
ALTER TABLE `drone_alarm_dispatch_record`
  ADD COLUMN `media_action` varchar(32) DEFAULT NULL COMMENT '到点执行动作: photo拍照 video录像 空不执行' AFTER `nest_name`,
  ADD COLUMN `media_status` int(11) DEFAULT NULL COMMENT '媒体触发状态: 1待触发 2成功 3失败 4无需触发' AFTER `media_action`,
  ADD COLUMN `media_fail_reason` text COMMENT '媒体触发失败原因' AFTER `media_status`,
  ADD COLUMN `media_request_body` longtext COMMENT '媒体触发请求体' AFTER `media_fail_reason`,
  ADD COLUMN `media_response_body` longtext COMMENT '媒体触发响应体' AFTER `media_request_body`,
  ADD COLUMN `media_trigger_time` datetime DEFAULT NULL COMMENT '媒体触发时间' AFTER `media_response_body`;
