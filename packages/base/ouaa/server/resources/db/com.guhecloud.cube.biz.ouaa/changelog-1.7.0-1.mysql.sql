-- liquibase formatted sql

-- changeset hetao:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_tenant' AND column_name='belong_tenant') AS column_exists;
ALTER TABLE `user_tenant` ADD COLUMN `enabled` VARCHAR(20) DEFAULT NULL COMMENT '是否启用';

-- changeset hetao:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_tenant' AND column_name='belong_tenant') AS column_exists;
ALTER TABLE `user_tenant` ADD COLUMN `belong_tenant` VARCHAR(20) DEFAULT NULL COMMENT '是否归属在该租户下';

-- changeset hetao:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user' AND column_name = 'mobile' ) AS column_exists;
ALTER TABLE `user` MODIFY COLUMN `mobile` VARCHAR(100) NULL COMMENT '手机号';



-- changeset hetao:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user_tel') AS column_exists;

CREATE TABLE `user_tel` (
  `id` bigint(20) unsigned NOT NULL COMMENT '用户手机号表主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `tel` varchar(50) NOT NULL COMMENT '手机号',
  `is_bind` varchar(20) DEFAULT 'BIND' COMMENT '是否绑定',
  `create_by` varchar(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` bigint(20) unsigned NOT NULL COMMENT '逻辑删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户手机号表';

-- changeset hetao:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_wechat' AND column_name='avatar') AS column_exists;
ALTER TABLE `user_wechat` ADD COLUMN `avatar` VARCHAR(512) DEFAULT NULL COMMENT '微信头像';

-- changeset hetao:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_wechat' AND column_name='nickname') AS column_exists;
ALTER TABLE `user_wechat` ADD COLUMN `nickname` VARCHAR(100) DEFAULT NULL COMMENT '微信昵称';
