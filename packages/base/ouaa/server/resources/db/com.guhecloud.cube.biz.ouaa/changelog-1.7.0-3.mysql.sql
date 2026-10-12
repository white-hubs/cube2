-- liquibase formatted sql


-- changeset hetao:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user_wechat' AND column_name = 'open_id' ) AS column_exists;
ALTER TABLE `user_wechat` MODIFY COLUMN `open_id` VARCHAR(100) NULL COMMENT '默认为微信公众号openid';
