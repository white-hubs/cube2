-- liquibase formatted sql


-- changeset hetao:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_wechat' AND column_name='web_openid') AS column_exists;
ALTER TABLE `user_wechat` ADD COLUMN `web_openid` VARCHAR(255) DEFAULT NULL COMMENT '网页端openid';

-- changeset hetao:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_wechat' AND column_name='app_openid') AS column_exists;
ALTER TABLE `user_wechat` ADD COLUMN `app_openid` VARCHAR(255) DEFAULT NULL COMMENT 'app openid';

-- changeset hetao:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_wechat' AND column_name='mp_openid') AS column_exists;
ALTER TABLE `user_wechat` ADD COLUMN `mp_openid` VARCHAR(255) DEFAULT NULL COMMENT '小程序openid';

