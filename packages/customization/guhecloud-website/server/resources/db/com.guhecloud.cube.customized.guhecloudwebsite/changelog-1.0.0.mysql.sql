-- liquibase formatted sql

-- changeset lj:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'build_record') AS column_exists;
CREATE TABLE `build_record` (
                                `id` bigint NOT NULL COMMENT '主键ID',
                                `artifact_name` varchar(256) DEFAULT NULL COMMENT '产物文件名，如 sg-website-1001-20260419143000.tgz',
                                `artifact_url` varchar(1024) DEFAULT NULL COMMENT '产物在 Nexus 中的访问地址',
                                `status` varchar(20) DEFAULT '1' COMMENT '构建状态：1=成功，2=失败，3=已发布',
                                `duration_ms` bigint DEFAULT NULL COMMENT '构建耗时（毫秒）',
                                `error_message` text COMMENT '构建失败时的错误信息',
                                `tenant` int DEFAULT NULL COMMENT '租户ID，框架自动填充',
                                `deleted` bigint DEFAULT NULL COMMENT '逻辑删除标识：0=未删除，非0=已删除',
                                `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                `release_status` varchar(255) DEFAULT NULL COMMENT '发布状态',
                                `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '构建任务id',
                                `deploy_id` varchar(64) DEFAULT NULL COMMENT '发布任务id',
                                `deploy_status` varchar(255) DEFAULT NULL,
                                `file_url` varchar(512) DEFAULT NULL COMMENT 'ZIP文件在文件服务器的存储路径',
                                `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='静态站点构建记录表';

-- changeset lj:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'friend_link') AS column_exists;
CREATE TABLE `friend_link` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `link_name` varchar(100) NOT NULL COMMENT '友链名称',
                               `link_url` varchar(500) NOT NULL COMMENT '友链地址',
                               `status` tinyint NOT NULL DEFAULT '0' COMMENT '启用状态：0-禁用，1-启用',
                               `tenant` varchar(64) NOT NULL COMMENT '租户ID',
                               `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
                               `create_by` varchar(64) NOT NULL COMMENT '创建人',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                               `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                               `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                               `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                               PRIMARY KEY (`id`),
                               KEY `idx_tenant` (`tenant`),
                               KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2046541197880664068 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='友情链接表';

-- changeset lj:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'material') AS column_exists;
CREATE TABLE `material` (
                            `id` bigint NOT NULL COMMENT '主键ID',
                            `path_id` bigint DEFAULT NULL COMMENT '所属文件夹ID',
                            `name` varchar(256) NOT NULL COMMENT '文件名',
                            `ext` varchar(32) DEFAULT NULL COMMENT '文件扩展名',
                            `mime_type` varchar(128) DEFAULT NULL COMMENT 'MIME类型',
                            `size` bigint DEFAULT '0' COMMENT '文件大小（字节）',
                            `url` varchar(512) DEFAULT NULL COMMENT '存储路径/URL',
                            `deleted` tinyint(1) DEFAULT '0' COMMENT '是否已删除',
                            `create_by` varchar(64) DEFAULT NULL COMMENT '创建用户',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_by` varchar(64) DEFAULT NULL COMMENT '更新用户',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `tenant` int DEFAULT NULL COMMENT '租户编号',
                            `file_id` bigint DEFAULT NULL,
                            `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                            `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                            `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                            `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                            PRIMARY KEY (`id`),
                            KEY `idx_folder_id` (`path_id`),
                            KEY `idx_tenant` (`tenant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='素材';

-- changeset lj:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'material_folder') AS column_exists;
CREATE TABLE `material_folder` (
                                   `id` bigint NOT NULL COMMENT '主键ID',
                                   `path_id` bigint DEFAULT '0' COMMENT '父文件夹ID，0表示根目录',
                                   `name` varchar(128) NOT NULL COMMENT '文件夹名称',
                                   `sort` int DEFAULT '0' COMMENT '排序',
                                   `deleted` tinyint(1) DEFAULT '0' COMMENT '是否已删除',
                                   `create_by` varchar(64) DEFAULT NULL COMMENT '创建用户',
                                   `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_by` varchar(64) DEFAULT NULL COMMENT '更新用户',
                                   `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `tenant` int DEFAULT NULL COMMENT '租户编号',
                                   `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                   `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                   `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                   `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_parent_id` (`path_id`),
                                   KEY `idx_tenant` (`tenant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='素材文件夹';

-- changeset lj:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_article') AS column_exists;
CREATE TABLE `sys_article` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `title` varchar(200) NOT NULL COMMENT '文章标题',
                               `category_id` bigint NOT NULL COMMENT '分类ID',
                               `cover_image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文章封面',
                               `summary` varchar(500) DEFAULT NULL COMMENT '内容简介',
                               `content` longtext COMMENT '文章内容',
                               `author` varchar(100) DEFAULT NULL COMMENT '作者',
                               `status` tinyint DEFAULT '1' COMMENT '状态 1显示 0不显示',
                               `view_count` int DEFAULT '0' COMMENT '浏览量',
                               `tenant` int DEFAULT NULL,
                               `deleted` bigint DEFAULT NULL,
                               `create_by` varchar(20) DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               `update_by` varchar(20) DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `date` varchar(32) DEFAULT NULL COMMENT '发布日期',
                               `sort` int DEFAULT '0' COMMENT '排序',
                               `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                               `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                               `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                               `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2048987788624359427 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章管理';

-- changeset lj:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_article_category') AS column_exists;
CREATE TABLE `sys_article_category` (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                        `category_name` varchar(100) NOT NULL COMMENT '分类名称',
                                        `sort` int DEFAULT '0' COMMENT '排序',
                                        `status` tinyint DEFAULT '1' COMMENT '状态 1显示 0不显示',
                                        `article_count` int DEFAULT '0' COMMENT '文章数量',
                                        `tenant` int DEFAULT NULL,
                                        `deleted` bigint DEFAULT NULL,
                                        `create_by` varchar(20) DEFAULT NULL,
                                        `create_time` datetime DEFAULT NULL,
                                        `update_by` varchar(20) DEFAULT NULL,
                                        `update_time` datetime DEFAULT NULL,
                                        `parent_id` bigint DEFAULT NULL,
                                        `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                        `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                        `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                        `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2048984595613970435 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章分类';

-- changeset lj:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_side_icon') AS column_exists;
CREATE TABLE `sys_side_icon` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `title` varchar(100) DEFAULT NULL COMMENT '标题',
                                 `icon` varchar(500) DEFAULT NULL COMMENT '图标',
                                 `summary` varchar(500) DEFAULT NULL COMMENT '概述',
                                 `image` varchar(500) DEFAULT NULL COMMENT '图片',
                                 `link` varchar(500) DEFAULT NULL COMMENT '跳转链接',
                                 `sort` int DEFAULT '0' COMMENT '排序',
                                 `status` tinyint DEFAULT '1' COMMENT '状态 1显示 0隐藏',
                                 `tenant` int DEFAULT NULL,
                                 `deleted` bigint DEFAULT NULL,
                                 `create_by` varchar(20) DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `update_by` varchar(20) DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 `is_open` tinyint DEFAULT NULL COMMENT '是否开启',
                                 `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                 `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                 `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                 `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2046542470499606531 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='侧边图标';

-- changeset lj:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_site_layout') AS column_exists;
CREATE TABLE `sys_site_layout` (
                                   `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `layout_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '布局名称',
                                   `config` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '布局配置JSON（组件列表）',
                                   `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '软删除标志：0-未删除 1-已删除',
                                   `tenant` int DEFAULT NULL COMMENT '租户ID，框架自动填充',
                                   `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `creator_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人id',
                                   `creator_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                   `updater_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人id',
                                   `updater_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_layout_name` (`layout_name`,`deleted`),
                                   KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=2048361345467662339 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='布局表';

-- changeset lj:ddl-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_site_nav') AS column_exists;
CREATE TABLE `sys_site_nav` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `nav_name` varchar(100) NOT NULL COMMENT '导航名称',
                                `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '上级导航ID',
                                `nav_icon` varchar(500) DEFAULT NULL COMMENT '导航图标',
                                `nav_type` tinyint DEFAULT NULL COMMENT '导航类型 1页面 2外链',
                                `nav_url` varchar(500) DEFAULT NULL COMMENT '导航地址',
                                `sort` int DEFAULT '0' COMMENT '排序',
                                `open_type` tinyint DEFAULT NULL COMMENT '打开方式 1当前 2新窗口',
                                `status` tinyint DEFAULT '1' COMMENT '状态 1启用 0禁用',
                                `tenant` int DEFAULT NULL,
                                `deleted` bigint DEFAULT NULL,
                                `create_by` varchar(20) DEFAULT NULL,
                                `create_time` datetime DEFAULT NULL,
                                `update_by` varchar(20) DEFAULT NULL,
                                `update_time` datetime DEFAULT NULL,
                                `nav_page_name` varchar(255) DEFAULT NULL COMMENT '导航页面名称',
                                `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2047151998278615042 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='站点导航';

-- changeset lj:ddl-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_site_page') AS column_exists;
CREATE TABLE `sys_site_page` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `page_name` varchar(100) NOT NULL COMMENT '页面名称',
                                 `page_title` varchar(200) DEFAULT NULL COMMENT '页面标题',
                                 `tenant` int DEFAULT NULL,
                                 `deleted` bigint DEFAULT NULL,
                                 `create_by` varchar(20) DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `update_by` varchar(20) DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 `page_route` varchar(256) DEFAULT NULL COMMENT '页面路由',
                                 `layout_id` bigint unsigned NOT NULL COMMENT '绑定布局ID',
                                 `meta_title` varchar(256) DEFAULT NULL COMMENT 'SEO标题',
                                 `meta_description` varchar(512) DEFAULT NULL COMMENT 'SEO描述',
                                 `meta_keywords` varchar(512) DEFAULT NULL COMMENT 'SEO关键词',
                                 `meta_nav_style` varchar(255) DEFAULT NULL,
                                 `components` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
                                 `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                 `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                 `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                 `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2049014382754381826 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='页面管理';

-- changeset lj:ddl-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_site_publish_config') AS column_exists;
CREATE TABLE `sys_site_publish_config` (
                                           `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                           `provider` varchar(64) DEFAULT NULL COMMENT '对象存储服务商，如 qiniu',
                                           `provider_config` longtext COMMENT '对象存储配置JSON',
                                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           `create_by` bigint unsigned DEFAULT NULL,
                                           `update_by` bigint unsigned DEFAULT NULL,
                                           `deleted` tinyint unsigned NOT NULL DEFAULT '0',
                                           `tenant` bigint unsigned DEFAULT NULL,
                                           `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                           `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                           `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                           `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2048394144815816707 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='网站发布配置表';

-- changeset lj:ddl-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_site_setting') AS column_exists;
CREATE TABLE `sys_site_setting` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `site_logo` varchar(500) DEFAULT NULL COMMENT '网站LOGO',
                                    `copyright` varchar(500) DEFAULT NULL COMMENT '版权信息',
                                    `icp_info` varchar(200) DEFAULT NULL COMMENT '备案信息',
                                    `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                                    `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
                                    `official_account` varchar(500) DEFAULT NULL COMMENT '公众号',
                                    `video_account` varchar(500) DEFAULT NULL COMMENT '视频号',
                                    `douyin` varchar(255) DEFAULT NULL COMMENT '抖音',
                                    `toutiao` varchar(255) DEFAULT NULL COMMENT '头条',
                                    `zhihu` varchar(255) DEFAULT NULL COMMENT '知乎',
                                    `xiaohongshu` varchar(255) DEFAULT NULL COMMENT '小红书',
                                    `statistics_code` text COMMENT '统计代码',
                                    `tenant` int DEFAULT NULL,
                                    `deleted` bigint DEFAULT NULL,
                                    `create_by` varchar(20) DEFAULT NULL,
                                    `create_time` datetime DEFAULT NULL,
                                    `update_by` varchar(20) DEFAULT NULL,
                                    `update_time` datetime DEFAULT NULL,
                                    `site_name` varchar(255) DEFAULT NULL COMMENT '网站名称',
                                    `og_image` varchar(255) DEFAULT NULL COMMENT 'OG分享图片',
                                    `favicon` varchar(255) DEFAULT NULL COMMENT '网站图标',
                                    `primary_color` varchar(50) DEFAULT NULL COMMENT '主题主色',
                                    `default_title` varchar(255) DEFAULT NULL COMMENT '默认标题',
                                    `default_description` varchar(500) DEFAULT NULL COMMENT '默认描述',
                                    `default_keywords` varchar(500) DEFAULT NULL COMMENT '默认关键词',
                                    `tracking_scripts` text COMMENT '统计脚本/第三方脚本',
                                    `url` varchar(512) DEFAULT NULL COMMENT '网站地址',
                                    `nav_style` varchar(32) DEFAULT 'light' COMMENT '导航风格 light/dark',
                                    `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                    `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                    `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                    `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2049484760693956610 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='网站设置';

-- changeset lj:ddl-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sys_user_feedback') AS column_exists;
CREATE TABLE `sys_user_feedback` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                     `user_name` varchar(50) NOT NULL COMMENT '姓名',
                                     `contact_way` varchar(50) NOT NULL COMMENT '联系方式（手机号/电话）',
                                     `company_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
                                     `application_scenario` varchar(500) DEFAULT NULL COMMENT '应用场景',
                                     `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0-正常，1-删除）',
                                     `tenant` varchar(64) DEFAULT '',
                                     `creator_id` varchar(50) DEFAULT NULL COMMENT '创建人id',
                                     `creator_type` varchar(20) DEFAULT NULL COMMENT '创建人类型 (USER/SYSTEM等)',
                                     `updater_id` varchar(50) DEFAULT NULL COMMENT '更新人id',
                                     `updater_type` varchar(20) DEFAULT NULL COMMENT '更新人类型 (USER/SYSTEM等)',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2049368734762627075 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='留言';