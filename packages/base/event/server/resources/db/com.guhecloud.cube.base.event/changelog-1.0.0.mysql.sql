-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'event_record') AS column_exists;
CREATE TABLE `event_record` (
  `id` bigint(20) NOT NULL,
  `data` mediumtext COMMENT '事件数据包',
  `business_event_type` varchar(100) DEFAULT NULL COMMENT '业务事件类型',
  `topic` varchar(100) DEFAULT NULL COMMENT '订阅主题',
  `timestamp` bigint(20) DEFAULT NULL COMMENT '业务事件触发时间戳',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件表';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'event_record') AS column_exists;
CREATE TABLE `event_consumer_record` (
  `id` bigint(20) NOT NULL,
  `event_id` bigint(20) DEFAULT NULL COMMENT '事件记录id',
  `timestamp` bigint(20) DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件消费记录表';

