-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'area_tag' ) AS column_exists;
CREATE TABLE IF NOT EXISTS `area_tag` (
                                          `id` bigint(20) unsigned NOT NULL,
                                          `name` varchar(50) NOT NULL COMMENT '标签名称',
                                          `describe` varchar(300) DEFAULT NULL COMMENT '标签描述',
                                          `ico_file_id` bigint(20) DEFAULT NULL COMMENT '图标',
                                          `tenant` int(10) unsigned DEFAULT NULL,
                                          `deleted` bigint(255) unsigned DEFAULT NULL,
                                          `create_by` varchar(20) DEFAULT NULL,
                                          `create_time` datetime DEFAULT NULL,
                                          `update_by` varchar(20) DEFAULT NULL,
                                          `update_time` datetime DEFAULT NULL,
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='区域标签表';


CREATE TABLE IF NOT EXISTS `area_status` (
                                             `id` bigint(20) unsigned NOT NULL,
                                             `name` varchar(50) NOT NULL COMMENT '状态名称',
                                             `describe` varchar(300) DEFAULT NULL COMMENT '状态描述',
                                             `tenant` int(10) unsigned DEFAULT NULL,
                                             `deleted` bigint(255) unsigned DEFAULT NULL,
                                             `create_by` varchar(20) DEFAULT NULL,
                                             `create_time` datetime DEFAULT NULL,
                                             `update_by` varchar(20) DEFAULT NULL,
                                             `update_time` datetime DEFAULT NULL,
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='区域状态表';

CREATE TABLE IF NOT EXISTS `area_feature` (
                                              `id` bigint(20) unsigned NOT NULL,
                                              `name` varchar(50) NOT NULL COMMENT '特征名称',
                                              `describe` varchar(300) DEFAULT NULL COMMENT '特征描述',
                                              `tenant` int(10) unsigned DEFAULT NULL,
                                              `deleted` bigint(255) unsigned DEFAULT NULL,
                                              `create_by` varchar(20) DEFAULT NULL,
                                              `create_time` datetime DEFAULT NULL,
                                              `update_by` varchar(20) DEFAULT NULL,
                                              `update_time` datetime DEFAULT NULL,
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='区域特征表';

-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'area' AND column_name = 'status_id' ) AS column_exists;
ALTER TABLE `area` ADD COLUMN `status_id` bigint(20) NULL COMMENT '空间状态id';
-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'area' AND column_name = 'feature_ids' ) AS column_exists;
ALTER TABLE `area` ADD COLUMN `feature_ids` text NULL COMMENT '特征ids' ;
-- changeset chx:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'area' AND column_name = 'tag_ids' ) AS column_exists;
ALTER TABLE `area` ADD COLUMN `tag_ids` text NULL COMMENT '标签ids' ;