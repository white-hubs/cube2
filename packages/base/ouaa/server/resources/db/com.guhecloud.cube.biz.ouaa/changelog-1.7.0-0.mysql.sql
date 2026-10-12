-- liquibase formatted sql


-- changeset hetao:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user' AND column_name = 'avatar' ) AS column_exists;
ALTER TABLE `user` MODIFY COLUMN `avatar` VARCHAR(1000) NULL COMMENT '头像';

-- changeset hetao:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_wechat' AND column_name='access_token') AS column_exists;
ALTER TABLE `user_wechat` ADD COLUMN `access_token` VARCHAR(512) DEFAULT NULL COMMENT '微信access_token';

-- changeset hetao:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_wechat' AND column_name='refresh_token') AS column_exists;
ALTER TABLE `user_wechat` ADD COLUMN `refresh_token` VARCHAR(512) DEFAULT NULL COMMENT '微信refresh_token';

