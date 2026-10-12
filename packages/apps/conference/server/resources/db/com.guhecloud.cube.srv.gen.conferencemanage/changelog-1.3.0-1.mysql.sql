-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'field_configuration') AS column_exists;
CREATE TABLE `field_configuration` (
                                       `id` BIGINT ( 20 ) NOT NULL,
                                       `field_code` VARCHAR ( 255 ) DEFAULT NULL COMMENT '字段编码',
                                       `field_name` VARCHAR ( 255 ) DEFAULT NULL COMMENT '字段名称',
                                       `required` BIGINT ( 20 ) DEFAULT NULL COMMENT '是否必输',
                                       `tenant` VARCHAR ( 255 ) DEFAULT NULL,
                                       `create_by` VARCHAR ( 32 ) DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_by` VARCHAR ( 32 ) DEFAULT NULL COMMENT '更新人',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `deleted` VARCHAR ( 255 ) DEFAULT NULL,
                                       PRIMARY KEY ( `id` )
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '字段必输配置';
-- changeset zengyadong:insert-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'field_configuration') AS column_exists;
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123103815294978, 'conferenceType', '会议类型', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-19 10:22:19', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123320803418114, 'conferenceTheme', '会议主题', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123396611268609, 'goodsNames', '会议物品', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123845573689346, 'bookNum', '参会人数', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123917011075073, 'content', '会议内容', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, 'cancelReason', '取消原因', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
