-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'conference_room') AS column_exists;
CREATE TABLE `conference_room` (
  `id` bigint(20) unsigned NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `number` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `propose_user_id` bigint(20) unsigned DEFAULT NULL,
  `propose_time` datetime DEFAULT NULL,
  `conference_type` varchar(50) DEFAULT NULL,
  `media_type` varchar(50) DEFAULT NULL,
  `calling_type` varchar(50) DEFAULT NULL,
  `closing_time` datetime DEFAULT NULL,
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议房间';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'conference_room_user') AS column_exists;
CREATE TABLE `conference_room_user` (
  `id` bigint(20) unsigned NOT NULL,
  `conference_room_id` bigint(20) unsigned DEFAULT NULL,
  `conference_room_number` varchar(50) DEFAULT NULL,
  `user_id` bigint(20) unsigned DEFAULT NULL,
  `conference_user_type` varchar(50) DEFAULT NULL,
  `membership_time` datetime DEFAULT NULL,
  `departure_time` datetime DEFAULT NULL,
  `departure_type` varchar(20) DEFAULT NULL,
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议人员';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'conference_invite_record') AS column_exists;
CREATE TABLE `conference_invite_record` (
  `id` bigint(20) unsigned NOT NULL,
  `conference_room_id` bigint(20) unsigned DEFAULT NULL,
  `conference_room_number` varchar(50) DEFAULT NULL,
  `user_id` bigint(20) unsigned DEFAULT NULL,
  `invite_time` datetime DEFAULT NULL,
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议邀请记录';
