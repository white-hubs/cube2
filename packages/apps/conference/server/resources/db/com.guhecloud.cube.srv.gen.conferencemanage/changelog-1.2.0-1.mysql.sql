-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'conference_goods') AS column_exists;
CREATE TABLE `conference_goods` (
                                    `id` BIGINT ( 20 ) NOT NULL,
                                    `goods_name` VARCHAR ( 255 ) DEFAULT NULL COMMENT '物品名称',
                                    `illustrate` VARCHAR ( 255 ) DEFAULT NULL COMMENT '说明',
                                    `area_id` BIGINT ( 20 ) DEFAULT NULL COMMENT '区域id',
                                    `area_name` VARCHAR ( 100 ) DEFAULT NULL COMMENT '区域名称',
                                    `tenant` VARCHAR ( 255 ) DEFAULT NULL,
                                    `create_by` VARCHAR ( 32 ) DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_by` VARCHAR ( 32 ) DEFAULT NULL COMMENT '更新人',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `deleted` VARCHAR ( 255 ) DEFAULT NULL,
                                    PRIMARY KEY ( `id` )
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '会议物品表';
-- changeset zengyadong:insert-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'appointment_record' AND column_name='goods_names' ) AS column_exists;
ALTER TABLE `cube-srv-gen-conferencemanage`.`appointment_record`
    ADD COLUMN `goods_names` VARCHAR ( 255 ) NULL COMMENT '会议物品id';
-- changeset zengyadong:insert-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'book_room' AND column_name='goods_names' ) AS column_exists;
ALTER TABLE `cube-srv-gen-conferencemanage`.`book_room`
    ADD COLUMN `goods_names` VARCHAR ( 255 ) NULL COMMENT '会议物品id';