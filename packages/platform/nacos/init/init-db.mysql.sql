CREATE DATABASE IF NOT EXISTS `cube_nacos` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube_nacos`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

/**
copy from https://github.com/alibaba/nacos/blob/master/distribution/conf/mysql-schema.sql?spm=5238cd80.2ef5001f.0.0.3f613b7ckC6LUL&file=mysql-schema.sql
 */
/******************************************/
/*   表名称 = config_info                  */
/******************************************/
CREATE TABLE IF NOT EXISTS `config_info` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                               `group_id` varchar(128) DEFAULT NULL COMMENT 'group_id',
                               `content` longtext NOT NULL COMMENT 'content',
                               `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                               `src_user` text COMMENT 'source user',
                               `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
                               `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                               `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
                               `c_desc` varchar(256) DEFAULT NULL COMMENT 'configuration description',
                               `c_use` varchar(64) DEFAULT NULL COMMENT 'configuration usage',
                               `effect` varchar(64) DEFAULT NULL COMMENT '配置生效的描述',
                               `type` varchar(64) DEFAULT NULL COMMENT '配置的类型',
                               `c_schema` text COMMENT '配置的模式',
                               `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='config_info';

/******************************************/
/*   表名称 = config_info  since 2.5.0                */
/******************************************/
CREATE TABLE IF NOT EXISTS `config_info_gray` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                    `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                    `content` longtext NOT NULL COMMENT 'content',
                                    `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                                    `src_user` text COMMENT 'src_user',
                                    `src_ip` varchar(100) DEFAULT NULL COMMENT 'src_ip',
                                    `gmt_create` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_create',
                                    `gmt_modified` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_modified',
                                    `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                                    `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
                                    `gray_name` varchar(128) NOT NULL COMMENT 'gray_name',
                                    `gray_rule` text NOT NULL COMMENT 'gray_rule',
                                    `encrypted_data_key` varchar(256) NOT NULL DEFAULT '' COMMENT 'encrypted_data_key',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_configinfogray_datagrouptenantgray` (`data_id`,`group_id`,`tenant_id`,`gray_name`),
                                    KEY `idx_dataid_gmt_modified` (`data_id`,`gmt_modified`),
                                    KEY `idx_gmt_modified` (`gmt_modified`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='config_info_gray';

/******************************************/
/*   表名称 = config_tags_relation         */
/******************************************/
CREATE TABLE IF NOT EXISTS `config_tags_relation` (
                                        `id` bigint(20) NOT NULL COMMENT 'id',
                                        `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
                                        `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
                                        `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                        `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                        `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
                                        `nid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
                                        PRIMARY KEY (`nid`),
                                        UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
                                        KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='config_tag_relation';

/******************************************/
/*   表名称 = group_capacity               */
/******************************************/
CREATE TABLE IF NOT EXISTS `group_capacity` (
                                  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
                                  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
                                  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
                                  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
                                  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
                                  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
                                  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
                                  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   表名称 = his_config_info              */
/******************************************/
CREATE TABLE IF NOT EXISTS `his_config_info` (
                                   `id` bigint(20) unsigned NOT NULL COMMENT 'id',
                                   `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
                                   `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                   `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                   `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                                   `content` longtext NOT NULL COMMENT 'content',
                                   `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   `src_user` text COMMENT 'source user',
                                   `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
                                   `op_type` char(10) DEFAULT NULL COMMENT 'operation type',
                                   `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
                                   `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
                                   `publish_type` varchar(50)  DEFAULT 'formal' COMMENT 'publish type gray or formal',
                                   `gray_name` varchar(50)  DEFAULT NULL COMMENT 'gray name',
                                   `ext_info`  longtext DEFAULT NULL COMMENT 'ext info',
                                   PRIMARY KEY (`nid`),
                                   KEY `idx_gmt_create` (`gmt_create`),
                                   KEY `idx_gmt_modified` (`gmt_modified`),
                                   KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='多租户改造';


/******************************************/
/*   表名称 = tenant_capacity              */
/******************************************/
CREATE TABLE IF NOT EXISTS `tenant_capacity` (
                                   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
                                   `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
                                   `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
                                   `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
                                   `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
                                   `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
                                   `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='租户容量信息表';


CREATE TABLE IF NOT EXISTS `tenant_info` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `kp` varchar(128) NOT NULL COMMENT 'kp',
                               `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
                               `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
                               `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
                               `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
                               `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
                               `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
                               KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='tenant_info';

CREATE TABLE IF NOT EXISTS `users` (
                         `username` varchar(50) NOT NULL PRIMARY KEY COMMENT 'username',
                         `password` varchar(500) NOT NULL COMMENT 'password',
                         `enabled` boolean NOT NULL COMMENT 'enabled'
);

CREATE TABLE IF NOT EXISTS `roles` (
                         `username` varchar(50) NOT NULL COMMENT 'username',
                         `role` varchar(50) NOT NULL COMMENT 'role',
                         UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE IF NOT EXISTS `permissions` (
                               `role` varchar(50) NOT NULL COMMENT 'role',
                               `resource` varchar(128) NOT NULL COMMENT 'resource',
                               `action` varchar(8) NOT NULL COMMENT 'action',
                               UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT IGNORE INTO `tenant_info` VALUES (1, '1', '25361486-9737-46a8-b488-4768cfd1fe77', 'bimops', 'bimops', 'nacos', 1754640338308, 1754640338308);

INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(1, 'common-env.yaml', 'DEFAULT_GROUP', 'common-env:
  jpush:
    appKey:
    masterSecret:
    officialWebsite: 1
  default-infer:
    ip: 127.0.0.1
  liqui-base:
    execute: true
  feign:
    connectTimeout: 3000
    readTimeout: 10000
  db:
    ip: ${common-env.default-infer.ip}
    port: 3306
    username:
    password:
  clickhouse:
    port: 8123
    jdbc-url: jdbc:clickhouse://${common-env.default-infer.ip}:${common-env.clickhouse.port}
    username:
    password:
  influxdb:
    url: http://127.0.0.1:8086
    db-name-energy: energy
    token:
    dborg:
  rabbit:
    host: ${common-env.default-infer.ip}
    port: 5672
    username:
    password:
    virtual-host:
  redis:
    port: 6379
    database: 3
    password:
  xxl:
    addresses: http://${common-env.default-infer.ip}:8080/xxl-job-admin
    username:
    password:
    executor:
      ip: 127.0.0.1
  gateway:
    port: 3200
    clientcredential:
      client-id: inner_server
      client-secret: cli@pwd#ghy!123
  rocketmq:
    port: 9876
  jwt:
    url: http://${common-env.ouaa.host}/oauth2/jwt.key
  ouaa:
    ip: 127.0.0.1
    port: 4009
    host: ${common-env.ouaa.ip}:${common-env.ouaa.port}
    jwt:
      url: http://${common-env.ouaa.host}/oauth2/jwt.key
    access-token-uri: http://${common-env.ouaa.host}/oauth2/token
    key-pair-password: keypairpass
    key-store-password: keystorepass
    registration:
      client-id: inner_server
      client-secret: cli@pwd#ghy!123
  scada:
    rtdb:
      url: http://127.0.0.1:9005
      header:
        authorization: RTDB 15B4D3461F177624206A:xQE0diMbLRepdf3YB+FIEXAMPLE=
    file:
      storeDir: /data/pic
    figure:
      visitUrlPath: http://127.0.0.1:3200
    tenant: -1
  dispatch:
    platform: PT
  identity:
    pre-url: http://127.0.0.1:40201/apps/qrcode-transfer/transfer?code=
  maintain:
    ousrouce:
      h5: http://fake-url
  security-mgt:
    project:
      flag: cnp
  energy:
    energy:
      weatherUrl: http://www.nmc.cn/f/rest/real/
      province: 59493
      queryLimit: 9
    gdprod:
      serviceAddr: http://127.0.0.1:8021
      systemServiceAddr: http://127.0.0.1:8020
      iotServiceAddr: http://127.0.0.1:8040/iotserver
      weatherUrl: http://www.nmc.cn/f/rest/real/
      province: 59493
      distributionTypeId: 234c1f1ab6c143879ffd218802a68d9c
      queryLimit: 9
  safety-mgt:
    monthly-report:
      template:
  safety-edu:
    rocketmq-config:
      ip-address: http://127.0.0.1
      port: 9876
      app-key: test
      producer-group: MARKET_PUSH_GROUP_GROUP
      market-receive-group: safetyEducation_group_market
      fail-retry-times: 3
  product-market:
    third:
      mysql:
        ip: 127.0.0.1
        port: 3306
        db:
        username:
        password:
      oracle:
        ip: 127.0.0.1
        port: 1521
        db:
        username:
        password:
  prepaid:
    gh-third:
      request:
        url:
          base-url: "http://yunsocket.acrel-eem.com/"
      login:
        username:
        password:
      tenant: 58197260
  license:
   base-url: http://${common-env.default-infer.ip}:9091
  file:
   type: MINIO
   endpoint: http://${common-env.default-infer.ip}:9091
   accessKeyId:
   accessKeySecret:
   accessPrefix: /minio
  meat-trade:
    arrange:
      rocketmq-config:
        ip-address: http://127.0.0.1
        port: 9876
        app-key: test
        producer-group: tally_report_group
        tally-push-producer:
          topic: tally_report_topic_test
        couple-trade-completed-consumer:
          topic: trade_completed_tally_topic
          group: trade_completed_tally_group
          tag: TagA
        fail-retry-times: 3
  dabaicai-cabbage:
    third:
      request:
        base-url: "https://testypt.dbc61.com/dbcdataoutput/"
        # 正式环境地址
        #base-url: https://aliyun.dbc61.com/dbcdataoutput/
      login:
        access-key: "U8tHgEkNQYVhf4SB"
        access-secret: "hOz2ofSovG48Ldu6QDYLcAGogIeIhDI7"
  ', 'ddaa15b871859df7a9d769acb20ce8dc', '2025-08-08 16:05:51', '2025-10-17 16:35:57', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(9, 'common-jackson.yaml', 'DEFAULT_GROUP', 'spring:
  jackson:
    time-zone: GMT+8
    serialization:
      write-dates-as-timestamps: true', '42c64159492dedb596303942fd12c764', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', NULL, NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(11, 'common-redis.yaml', 'DEFAULT_GROUP', 'spring:
  data:
    redis:
      host: ${common-env.default-infer.ip}
      port: ${common-env.redis.port}
      database: ${common-env.redis.database}
      password: ${common-env.redis.password}', '1bc1a0f90e3051fe360c45f45d6f06cf', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(13, 'common-security-client.yaml', 'DEFAULT_GROUP', 'spring:
  security:
    oauth2:
      client:
        registration:
          ouaa:
            client-Id: ${common-env.ouaa.registration.client-id}
            client-secret: ${common-env.ouaa.registration.client-secret}
        provider:
          ouaa:
            token-uri: ${common-env.ouaa.access-token-uri}', 'c1a9b5e4297d42550cd7a95a62a35fde', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', NULL, NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(15, 'common-security-authorization-server.yaml', 'DEFAULT_GROUP', 'spring:
  security:
    oauth2:
      authorizationserver:
        token:
          key-pair:
            alias: jwt
            resource: demojwt.jks
            key-pair-password: ${common-env.ouaa.key-pair-password}
            key-store-password: ${common-env.ouaa.key-store-password}', '2b885c97b3660d90dc8766bcd03cfe6b', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(17, 'common-security-resource-server.yaml', 'DEFAULT_GROUP', 'spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: ${common-env.jwt.url}', 'a6957fcece62d4eff4bd8157f8f3816e', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(19, 'common-mybatis-plus.yaml', 'DEFAULT_GROUP', 'mybatis-plus:
  # 不能默认配置，有些项目（device-center）是多模块，可能存在在 jar 包中搜索 mapper 的行为，需要单独配置；classpath* 效率比 classpath 低，默认由服务自行配置
  # mapper-locations: classpath:mapper/*.xml
  global-config:
    db-config:
      update-strategy: ALWAYS
      db-type: mysql
      column-format: "`%s`"
  configuration:
    map-underscore-to-camel-case: true
    # mybatis默认情况下, 当返回 null 时, 不会调用put方法，会导致 mapResult.get 时不存在该 key
    # see https://baomidou.com/reference/question/
    call-setters-on-nulls: true
    # mybatis默认情况下, 当返回行的所有列都是空时(含属性无法自动映射), 默认返回 null
    # see https://baomidou.com/reference/question/
    return-instance-for-empty-row: true', '2ff6ed22ca615120b9cb42f58d45dbc5', '2025-08-08 16:05:51', '2025-09-24 10:57:37', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(23, 'common-rocketmq.yaml', 'DEFAULT_GROUP', 'rocketmq:
  name-server: http://${common-env.default-infer.ip}:${common-env.rocketmq.port}
  producer:
    group: ${spring.application.name}_group
    send-message-timeout: 300000', '812f64d38c547affb340bb3d18cd3baa', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(25, 'common-platform-fegin.yaml', 'DEFAULT_GROUP', 'platform:
  feign:
    security:
      secretKey: inneFeign@secret
      timeout: 600', '0c5dbf26a32e5f7fcd8460ea1a0a17cf', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO cube_nacos.config_info
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(27, 'common-platform-security.yaml', 'DEFAULT_GROUP', 'platform:
  security:
    resource:
      permit: /oauth2/**, /authenticated/app-version/new
    jwt:
      tenant-validator:
        ignoreUriPrefixes: /context/, /file/, /file-ref/, /user-tenant
        ignoreUris: /oauth2/token, /user/mine, /tenant/mine, /tenants, /tenant/recent-login, /file, /file-ref, /authenticated/tenant, /authenticated/tenants, /supplier-mp, /supplier/mine/tenant, /tenant/market-mp, /authenticated/user/save, /authenticated/tenant/mine, /supplier/mine/web, /authenticated/tenant/page, /goods-category/mp/list, /authenticated/tenant/codes, /parking-lane/tree/list, /authenticated/user
        ignoreTerminals: asc-purchaser-mp, asc-meat-purchaser-mp', '4913ef568f0e27165ef75ea6ed4884fe', '2025-08-08 16:05:51', '2025-11-20 15:47:41', 'nacos', '192.168.4.130', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(29, 'common-platform-httpclient.yaml', 'DEFAULT_GROUP', 'platform:
  httpclient:
    pool:
      connect-timeout: 10000
      read-timeout: 60000', '6755267bd4c2a70e9ea7bec5b0c5a1fe', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(31, 'common-datasource.yaml', 'DEFAULT_GROUP', 'spring:
  datasource:
    username: ${common-env.db.username}
    password: ${common-env.db.password}
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      pool-name: ${spring.application.name}
      maximum-pool-size: 5
      max-lifetime: 60000', 'd39a50c88274b3057923c0f328f25af4', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(33, 'common-xxl.yaml', 'DEFAULT_GROUP', 'xxl:
  enable: false
  job:
    admin:
      addresses: ${common-env.xxl.addresses}
    accessToken: default_token
    executor:
      appname: bimops-be-servone
      address:
      ip:
      port: 0
      logpath: logs/${spring.application.name}/job
      logretentiondays: 7
  username: ${common-env.xxl.username}
  password: ${common-env.xxl.password}', 'eb7820b1a2b9792d460edb863ea66dad', '2025-08-08 16:05:51', '2025-08-29 16:14:43', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(35, 'common-clickhouse.yaml', 'DEFAULT_GROUP', 'clickHouse:
  jdbcUrl: ${common-env.clickhouse.jdbc-url}
  #driverClassName: ru.yandex.clickhouse.ClickHouseDriver # com.github.housepower.jdbc.ClickHouseDriver
  driverClassName: com.clickhouse.jdbc.ClickHouseDriver
  username: ${common-env.clickhouse.username}
  password: ${common-env.clickhouse.password}', 'f353cf0ace6a09ea13504432a17abdfd', '2025-08-08 16:05:51', '2025-08-26 16:56:49', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(37, 'common-iot-rabbit.yaml', 'DEFAULT_GROUP', 'thirdparty:
  iot:
    subscribe:
      full:
        consumer:
          rabbit:
            host: ${common-env.rabbit.host}
            port: ${common-env.rabbit.port}
            username: ${common-env.rabbit.username}
            password: ${common-env.rabbit.password}
            virtual-host: ${common-env.rabbit.virtual-host}', '3bd5052bca0f312da6fb15a8563a66cf', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(39, 'common-influxdb.yaml', 'DEFAULT_GROUP', 'influx:
  url: ${common-env.influxdb.url}
  dbNameEnergy: ${common-env.influxdb.db-name-energy}
  token: ${common-env.influxdb.token}
  dborg: ${common-env.influxdb.dborg}', '44033bda375a8f7216ffaaf01f49d49b', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(41, 'common-energy.yaml', 'DEFAULT_GROUP', 'influx:
  url: ${common-env.influxdb.url}
  dbNameEnergy: ${common-env.influxdb.db-name-energy}
  token: ${common-env.influxdb.token}
  dborg: ${common-env.influxdb.dborg}', '44033bda375a8f7216ffaaf01f49d49b', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(43, 'sharding-db.yaml', 'DEFAULT_GROUP', 'mode:
  type: Standalone
  repository:
    type: JDBC
dataSources: # 数据源配置 YamlJDBCConfiguration
  db:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://${common-env.default-infer.ip}:${common-env.db.port}/cube-base-file?characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false&allowMultiQueries=true&allowPublicKeyRetrieval=true
    username: ${common-env.db.username}
    password: ${common-env.db.password}

rules: # 配置  表规则
  - !SHARDING
    tables:
      file: # 分表，表名
        # 配置分库策略
        # 由数据源名 + 表名组成，以小数点分隔。多个表以逗号分隔，支持inline表达式。缺省表示使用已知数据源与逻辑表名称生成数据节点，用于广播表（即每个库中都需要一个同样的表用于关联查询，多为字典表）或只分库不分表且所有库的表结构完全一致的情况
        actualDataNodes: db.file_$->{0..15}  #数据节点,均匀分布
        tableStrategy:  # 配置分表策略
          standard:
            shardingColumn: id
            shardingAlgorithmName: file_inline
      file_ref: # 分表，表名
        # 配置分库策略
        # 由数据源名 + 表名组成，以小数点分隔。多个表以逗号分隔，支持inline表达式。缺省表示使用已知数据源与逻辑表名称生成数据节点，用于广播表（即每个库中都需要一个同样的表用于关联查询，多为字典表）或只分库不分表且所有库的表结构完全一致的情况
        actualDataNodes: db.file_ref_$->{0..15}  #数据节点,均匀分布
        tableStrategy: # 配置分表策略
          standard:
            shardingColumn: id
            shardingAlgorithmName: file_ref_inline
    # 配置 分片算法
    shardingAlgorithms:
      file_inline:
        type: INLINE
        props:
          algorithm-expression: file_$->{id.longValue() % 16}  #按模运算分配
          allow-range-query-with-inline-sharding: true
      file_ref_inline:
        type: INLINE
        props:
          algorithm-expression: file_ref_$->{id.longValue() % 16}  #按模运算分配
          allow-range-query-with-inline-sharding: true
  - !SINGLE
    tables:
      # MySQL 风格
      - db.* # 加载指定数据源中的全部单表
    defaultDataSource: db # 默认数据源，仅在执行 CREATE TABLE IF NOT EXISTS 创建单表时有效。缺失值为空，表示随机单播路由。
props:
  sql-show: true', 'b097bd21d1b29b29d7d579e882a4735c', '2025-08-08 16:05:51', '2025-08-08 16:05:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();

INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(51, 'gateway.yaml', 'DEFAULT_GROUP', 'spring:
  security:
    oauth2:
      resourceclient:
        clientcredential:
          client-Id: ${common-env.ouaa.registration.client-id}
          client-secret: ${common-env.ouaa.registration.client-secret}
          access-token-uri: ${common-env.ouaa.access-token-uri}', 'a26d6d42058e38bb020f98272fe68a58', '2025-08-13 14:31:51', '2025-08-13 14:31:51', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', NULL, NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO config_info
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(57, 'common-license.yaml', 'DEFAULT_GROUP', 'license:
  subject: test
  publicAlias: guhecloudPublicCert
  storePass: guheCloudPrivate@1234
  licensePath: ${common-env.license.base-url}/ghcloud-license/license.lic
  publicKeysStorePath: ${common-env.license.base-url}/ghcloud-license/publicCerts.keystore
', '18406a3535108819b5caa7f7776ca457', '2025-09-10 10:32:20', '2025-10-16 20:36:35', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO `config_info`
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(59, 'common-job.yaml', 'DEFAULT_GROUP', 'job:
  scheduler:
    type: xxl-job
  stop:
    jobIds:
    jobNames: [refreshFlowDataJob]
  impl:
    xxl-job:
      admin:
        addresses: ${common-env.xxl.addresses}
      accessToken: default_token
      executor:
        appname: bimops-be-servone
        address:
        ip: ${common-env.xxl.executor.ip}
        port: 0
        logpath: logs/${spring.application.name}/job
        logretentiondays: 7
      username: ${common-env.xxl.username}
      password: ${common-env.xxl.password}
', '404929f8ae073e49d07ef05808210e26', '2025-09-10 11:06:15', '2025-09-24 09:29:09', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();
INSERT INTO cube_nacos.config_info
(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key)
VALUES(63, 'file.yaml', 'DEFAULT_GROUP', 'file:
    type: ${common-env.file.type}
    endpoint: ${common-env.file.endpoint}
    accessKeyId: ${common-env.file.accessKeyId}
    accessKeySecret: ${common-env.file.accessKeySecret}
    accessPrefix: ${common-env.file.accessPrefix}', 'bff25748e047d7035d335bda51ab8753', '2025-10-13 14:42:21', '2025-10-17 16:36:07', 'nacos', '127.0.0.1', '', '25361486-9737-46a8-b488-4768cfd1fe77', '', NULL, NULL, 'yaml', NULL, '') ON DUPLICATE KEY UPDATE `gmt_modified` = NOW();


SET FOREIGN_KEY_CHECKS = 1;
