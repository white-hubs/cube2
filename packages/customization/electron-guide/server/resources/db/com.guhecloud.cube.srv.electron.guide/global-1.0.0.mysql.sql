-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `guide_area_manage`
(
    `id`          bigint(20) NOT NULL,
    `name`        varchar(255) COLLATE utf8mb4_german2_ci  DEFAULT NULL COMMENT '名称',
    `coordinate`  varchar(255) COLLATE utf8mb4_german2_ci  DEFAULT NULL COMMENT '坐标',
    `deleted`     bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(100) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`      int(11) DEFAULT NULL,
    `zoom`        varchar(255) COLLATE utf8mb4_german2_ci  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;

CREATE TABLE `layer_manage`
(
    `id`          bigint(20) NOT NULL,
    `layer_name`  varchar(255) COLLATE utf8mb4_german2_ci NOT NULL COMMENT '图层名称',
    `layer_file`  varchar(255) COLLATE utf8mb4_german2_ci NOT NULL COMMENT '图层附件',
    `state`       varchar(50) COLLATE utf8mb4_german2_ci  NOT NULL COMMENT '使用状态',
    `deleted`     bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`   varchar(100) CHARACTER SET utf8mb4         NOT NULL COMMENT '创建用户',
    `create_time` datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(100) CHARACTER SET utf8mb4                  DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime                                         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`      int(11) DEFAULT NULL,
    `southwest`   varchar(255) COLLATE utf8mb4_german2_ci          DEFAULT NULL COMMENT '西南角',
    `northeast`   varchar(255) COLLATE utf8mb4_german2_ci          DEFAULT NULL COMMENT '东北角',
    `center`      varchar(255) COLLATE utf8mb4_german2_ci          DEFAULT NULL COMMENT '中心点',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;

CREATE TABLE `point_manage`
(
    `id`          bigint(20) NOT NULL,
    `name`        varchar(255) COLLATE utf8mb4_german2_ci  DEFAULT NULL COMMENT '点位名称',
    `area_id`     bigint(20) DEFAULT NULL COMMENT '区域id',
    `address`     varchar(255) COLLATE utf8mb4_german2_ci  DEFAULT NULL COMMENT '点位地址',
    `coordinate`  varchar(255) COLLATE utf8mb4_german2_ci  DEFAULT NULL COMMENT '点位坐标',
    `remarks`     varchar(255) COLLATE utf8mb4_german2_ci  DEFAULT NULL COMMENT '备注',
    `deleted`     bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(100) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`      int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;