CREATE DATABASE IF NOT EXISTS`iot-v2-access` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `iot-v2-access`;

CREATE TABLE IF NOT EXISTS `acc_channel`
(
    `id`                  bigint(20)                      NOT NULL COMMENT '设备事件id',
    `gateway_id`          bigint(20)                      NOT NULL COMMENT '网关id',
    `name`                varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '通道名称',
    `dynamic_config`      text COLLATE utf8mb4_bin COMMENT '配置参数',
    `enable`              tinyint(1)                       DEFAULT '1' COMMENT '是否启用:0未启用 1已启用',
    `remark`              varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
    `comm_code`           varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '通信协议code',
    `interface_code`      varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '通信接口code',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='通道';

CREATE TABLE IF NOT EXISTS `acc_controller`
(
    `id`             bigint(20)                      NOT NULL COMMENT '主键id',
    `channel_id`     bigint(20)                      NOT NULL COMMENT '通道id',
    `name`           varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
    `ctl_code`       varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'name-of-ctl-uniq-in-the-channel',
    `dynamic_config` text COLLATE utf8mb4_bin COMMENT '参数配置',
    `status`         tinyint(1)                       DEFAULT '1' COMMENT '状态:0,离线 1在线',
    `enable`         tinyint(1)                       DEFAULT '0' COMMENT '是否启用:0未启用 1已启用',
    `remark`         varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
    `online_time`    datetime                         DEFAULT NULL COMMENT '最后一次在线时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `channel_id` (`channel_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='控制器';

CREATE TABLE IF NOT EXISTS `acc_gateway`
(
    `id`              bigint(20)                      NOT NULL COMMENT '设备事件id',
    `env_instance_id` bigint(20)                                       DEFAULT NULL COMMENT '协议实例id',
    `protocol`        varchar(255) COLLATE utf8mb4_bin                 DEFAULT NULL COMMENT 'protocol code',
    `name`            varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '网关名称',
    `status`          tinyint(1)                                       DEFAULT '1' COMMENT '状态-1:未激活 0:离线 1:在线',
    `link_status`     int(11)                                          DEFAULT '0' COMMENT '0 停止 1 启动中  2 运行中 3 停止中  4 异常',
    `dynamic_config`  text COLLATE utf8mb4_bin COMMENT '基于schema动态配置',
    `device_id`       varchar(255) COLLATE utf8mb4_bin                 DEFAULT NULL COMMENT '业务平台设备ID',
    `auth_user_name`  varchar(255) COLLATE utf8mb4_bin                 DEFAULT NULL COMMENT '认证账号',
    `auth_password`   varchar(255) COLLATE utf8mb4_bin                 DEFAULT NULL COMMENT '认证秘钥',
    `device_number`   varchar(255) COLLATE utf8mb4_bin                 DEFAULT NULL COMMENT '网关编码(设备编码)',
    `remark`          varchar(500) COLLATE utf8mb4_bin                 DEFAULT NULL COMMENT '备注',
    `enable`          tinyint(4)                                       DEFAULT NULL COMMENT '是否启用',
    `online_time`     datetime                                         DEFAULT NULL COMMENT '最后一次在线时间',
    `tenant_id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='网关';

CREATE TABLE IF NOT EXISTS `acc_variable`
(
    `id`                     bigint(20)                       NOT NULL COMMENT '设备事件id',
    `gateway_id`             bigint(20)                       NOT NULL COMMENT '冗余网关id',
    `controller_id`          bigint(20)                       NOT NULL COMMENT '控制器id',
    `var_code`               varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT 'name-of-var-uniq-in-the-controller',
    `name`                   varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
    `data_type`              varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '数据类型',
    `dynamic_config`         text COLLATE utf8mb4_bin COMMENT '配置参数',
    `read_type`              tinyint(1)                       DEFAULT '0' COMMENT '读写类型:0只读 1读写',
    `enable`                 tinyint(1)                       DEFAULT '1' COMMENT '是否启用:0未启用 1已启用',
    `remark`                 varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
    `convert_style_config`   text COLLATE utf8mb4_bin COMMENT '变量转换样式配置',
    `report_strategy_config` text COLLATE utf8mb4_bin COMMENT '上报策略配置',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `tb_variable_controller_id_index` (`controller_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='变量';

CREATE TABLE IF NOT EXISTS `aggregate_group`
(
    `id`                    bigint(20)                    NOT NULL,
    `access_gateway_id`     bigint(20)                    NOT NULL COMMENT '网关id',
    `access_channel_id`     bigint(20)                    NOT NULL COMMENT '通道id',
    `access_controller_ids` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '关联控制器id(废弃 aggregate_group_controller替代)',
    `name`                  varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '聚合组name',
    `device_number`         varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '孪生设备编号,用于和业务层进行关联',
    `device_id`             bigint(20)                    DEFAULT NULL COMMENT '设备ID,预留坑位(后期允许独立生成)',
    `tenant_id`             varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '聚合组Name在租户下唯一',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `grp_name_and_tenant` (`name`, `tenant_id`) USING BTREE COMMENT '聚合组&租户Id',
    KEY `access_controller_ids` (`access_controller_ids`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `aggregate_group_controller`
(
    `aggregate_group_id` bigint(20) NOT NULL,
    `controller_id`      bigint(20) NOT NULL,
    PRIMARY KEY (`aggregate_group_id`, `controller_id`) USING BTREE,
    KEY `controller_id` (`controller_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `aggregate_group_rel`
(
    `id`              bigint(20) NOT NULL,
    `source_group_id` bigint(20) DEFAULT NULL COMMENT '源',
    `target_group_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `unikey` (`source_group_id`, `target_group_id`) USING BTREE COMMENT '联合索引',
    UNIQUE KEY `source_group_id` (`source_group_id`) USING BTREE,
    UNIQUE KEY `source_group_id_2` (`source_group_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `aggregate_group_variable`
(
    `id`                   bigint(20) NOT NULL,
    `aggregate_group_id`   bigint(20)                    DEFAULT NULL COMMENT '聚合组id(为空表示未绑定)',
    `access_controller_id` bigint(20)                    DEFAULT NULL COMMENT '关联控制器id(冗余信息,为空表示未绑定)',
    `access_variable_id`   bigint(20)                    DEFAULT NULL COMMENT '实际对应的变量id(为空表示未绑定)',
    `virtual_name`         varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '虚拟变量名(聚合变量),可选',
    `virtual_code`         varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '虚拟code(聚合变量标识)',
    `zoom_expr`            varchar(255) COLLATE utf8mb4_bin DEFAULT '1' COMMENT '缩放表达式',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `access_controller_id` (`access_controller_id`) USING BTREE,
    KEY `access_variable_id` (`access_variable_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `command`
(
    `id`              bigint(20) NOT NULL,
    `msgId`           varchar(400) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台下发消息ID',
    `gateway_id`      varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '网关id',
    `command`         varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '下发命令',
    `command_content` longtext COLLATE utf8mb4_bin COMMENT '命令详情',
    `reply_state`     int(11)                       DEFAULT '0' COMMENT '回复状态: 0 未回复 1 已回复',
    `reply_topic`     varchar(400) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '回复的topic',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `persistent_config`
(
    `unique_key` varchar(500) COLLATE utf8mb4_bin NOT NULL,
    `config`     longtext COLLATE utf8mb4_bin,
    PRIMARY KEY (`unique_key`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `scan_controller`
(
    `id`             bigint(20) NOT NULL COMMENT '主键id',
    `channel_id`     bigint(20) NOT NULL COMMENT '发起扫描的接口id',
    `name`           varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
    `ctl_code`       varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址(通道下唯一)',
    `is_bind`        tinyint(4) NOT NULL              DEFAULT '0' COMMENT '是否绑定:0没有绑定 1已绑定',
    `dynamic_config` text COLLATE utf8mb4_bin COMMENT '动态配置项',
    `task_id`        bigint(20)                       DEFAULT NULL COMMENT '任务项',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_controller_scan_scan_info` (`channel_id`, `ctl_code`(191)) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='扫描到的控制器记录';

CREATE TABLE IF NOT EXISTS `scan_task`
(
    `id`              bigint(20) NOT NULL,
    `name`            varchar(255) COLLATE utf8mb4_bin  DEFAULT NULL COMMENT '任务名称',
    `config`          text COLLATE utf8mb4_bin COMMENT '任务动态参数',
    `retry_times`     int(11)                        DEFAULT NULL COMMENT '重试次数',
    `time_out`        int(11)                        DEFAULT NULL COMMENT '扫描超时时间',
    `task_type`       int(11)                        DEFAULT NULL COMMENT '1: 控制器变量  2:控制器扫描  3:变量扫描',
    `task_result`     int(11)                        DEFAULT NULL COMMENT '任务状态: 1、进行中 2、扫描已完成 3、扫描超时退出(不支持) 4、查找时间超时退出',
    `task_result_msg` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务结果描述',
    `channel_id`      bigint(20)                     DEFAULT NULL COMMENT '本次任务扫描的通道ID',
    `controller_id`   bigint(20)                     DEFAULT NULL COMMENT '本次任务扫描的控制器ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `scan_variable`
(
    `id`                 bigint(20)                       NOT NULL,
    `controller_id`      bigint(20)                                DEFAULT NULL COMMENT '关联控制器',
    `scan_controller_id` bigint(20)                                DEFAULT NULL COMMENT '控制器变量扫描,控制器临时id',
    `name`               varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '设备属性名称',
    `var_code`           varchar(200) COLLATE utf8mb4_bin NOT NULL COMMENT '地址(控制器下唯一)',
    `is_bind`            tinyint(4)                       NOT NULL DEFAULT '0' COMMENT '是否已经绑定',
    `dynamic_config`     text COLLATE utf8mb4_bin COMMENT '配置参数',
    `data_type`          varchar(50) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '数据类型',
    `read_type`          int(11)                                   DEFAULT NULL COMMENT '0:只读 1读写',
    `dev_name`           varchar(255) COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '聚合组(设备)name',
    `dev_num`            varchar(255) COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '聚合组(设备)devNum',
    `prop_code`          varchar(255) COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '属性code',
    `prop_name`          varchar(255) COLLATE utf8mb4_bin          DEFAULT NULL COMMENT '属性名称',
    `task_id`            bigint(20)                                DEFAULT NULL COMMENT '任务项',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='扫描到的控制器变量';

CREATE TABLE IF NOT EXISTS `user`
(
    `id`        bigint(20) NOT NULL,
    `user_name` varchar(400) COLLATE utf8mb4_bin DEFAULT NULL,
    `pwd`       varchar(400) COLLATE utf8mb4_bin DEFAULT NULL,
    `tenant`    varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

INSERT INTO `persistent_config` (`unique_key`, `config`) VALUES ('acc-os:sql_change_key', '[{\"fileName\":\"01-remove_protocol.sql\",\"ts\":1748242024278}]');
