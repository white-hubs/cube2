CREATE DATABASE IF NOT EXISTS `cube_base_iot_product_model` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `cube_base_iot_product_model`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS `iot_md_product_event_v3`
(
    `id`          bigint          NOT NULL COMMENT '事件ID',
    `tenant`      int unsigned    NOT NULL DEFAULT '0' COMMENT '租户编号',
    `prod_id`     bigint          NOT NULL COMMENT '产品ID',
    `name`        varchar(128)    NOT NULL COMMENT '事件名称',
    `event_code`  varchar(64)     NOT NULL COMMENT '事件编码,产品下唯一',
    `category_id` bigint                   DEFAULT NULL COMMENT '事件类别ID(内部使用,查询时通过code转换)',
    `config`      text COMMENT '事件配置(JSON格式,包含输出参数/触发条件等)',
    `remark`      varchar(512)             DEFAULT NULL COMMENT '描述',
    `status`      tinyint         NOT NULL DEFAULT '1' COMMENT '状态: 1启用 0禁用',
    `sort_order`  int             NOT NULL DEFAULT '0' COMMENT '排序号',
    `deleted`     bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标记(0未删除,>0为删除时间戳ms)',
    `create_by`   varchar(64)              DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(64)              DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_prod_event_code` (`tenant`, `prod_id`, `event_code`, `deleted`),
    KEY `idx_tenant` (`tenant`),
    KEY `idx_prod_id` (`prod_id`),
    KEY `idx_event_code` (`event_code`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='产品事件表v3版本';

CREATE TABLE IF NOT EXISTS `iot_md_product_method_v3`
(
    `id`              bigint          NOT NULL COMMENT '方法ID',
    `tenant`          int unsigned    NOT NULL DEFAULT '0' COMMENT '租户编号',
    `prod_id`         bigint          NOT NULL COMMENT '产品ID',
    `name`            varchar(128)    NOT NULL COMMENT '方法名称',
    `method_code`     varchar(64)     NOT NULL COMMENT '方法编码,产品下唯一',
    `call_type`       varchar(16)              DEFAULT NULL COMMENT '调用类型: sync同步 async异步',
    `input_params`    text COMMENT '输入参数定义(JSON格式)',
    `output_params`   text COMMENT '输出参数定义(JSON格式)',
    `timeout_seconds` int                      DEFAULT NULL COMMENT '超时时间(秒)',
    `retry_count`     int                      DEFAULT NULL COMMENT '重试次数',
    `retry_interval`  int                      DEFAULT NULL COMMENT '重试间隔(秒)',
    `remark`          varchar(512)             DEFAULT NULL COMMENT '方法描述',
    `status`          tinyint         NOT NULL DEFAULT '1' COMMENT '状态: 1启用 0禁用',
    `sort_order`      int             NOT NULL DEFAULT '0' COMMENT '排序号',
    `deleted`         bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标记(0未删除,>0为删除时间戳ms)',
    `create_by`       varchar(64)              DEFAULT NULL COMMENT '创建用户',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       varchar(64)              DEFAULT NULL COMMENT '更新用户',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_prod_method_code` (`tenant`, `prod_id`, `method_code`, `deleted`),
    KEY `idx_tenant` (`tenant`),
    KEY `idx_prod_id` (`prod_id`),
    KEY `idx_method_code` (`method_code`),
    KEY `idx_call_type` (`call_type`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='产品方法表v3版本(新增功能)';

CREATE TABLE IF NOT EXISTS `iot_md_product_model_version_v3`
(
    `id`                 bigint          NOT NULL COMMENT '版本ID',
    `tenant`             int unsigned    NOT NULL DEFAULT '0' COMMENT '租户编号',
    `prod_id`            bigint          NOT NULL COMMENT '产品ID',
    `prod_number`        varchar(64)     NOT NULL COMMENT '产品编号',
    `version_number`     varchar(32)     NOT NULL COMMENT '版本号(格式: v1.0.0)',
    `version_name`       varchar(128)             DEFAULT NULL COMMENT '版本名称',
    `model_snapshot`     longtext        NOT NULL COMMENT '完整的物模型JSON快照(使用业务code而非ID)',
    `change_description` varchar(512)             DEFAULT NULL COMMENT '变更说明',
    `change_type`        varchar(16)              DEFAULT NULL COMMENT '变更类型: create/update/delete',
    `change_summary`     text COMMENT '变更内容摘要(JSON格式)',
    `is_current`         tinyint         NOT NULL DEFAULT '0' COMMENT '是否当前版本: 1是 0否',
    `status`             tinyint         NOT NULL DEFAULT '1' COMMENT '版本状态: 1正常 0已归档',
    `deleted`            bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标记(0未删除,>0为删除时间戳ms)',
    `create_by`          varchar(64)              DEFAULT NULL COMMENT '创建用户',
    `create_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(64)              DEFAULT NULL COMMENT '更新用户',
    `update_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_prod_version_number` (`tenant`, `prod_id`, `version_number`, `deleted`),
    KEY `idx_tenant` (`tenant`),
    KEY `idx_prod_id` (`prod_id`),
    KEY `idx_prod_number` (`prod_number`),
    KEY `idx_version_number` (`version_number`),
    KEY `idx_is_current` (`is_current`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='物模型版本快照表v3版本(新增功能)';

CREATE TABLE IF NOT EXISTS `iot_md_product_prop_v3`
(
    `id`            bigint          NOT NULL COMMENT '属性ID',
    `tenant`        int unsigned    NOT NULL DEFAULT '0' COMMENT '租户编号',
    `prod_id`       bigint          NOT NULL COMMENT '产品ID',
    `name`          varchar(128)    NOT NULL COMMENT '属性名称',
    `prop_code`     varchar(64)     NOT NULL COMMENT '属性Code,产品下唯一',
    `data_type`     varchar(32)     NOT NULL COMMENT '数据类型: int/long/float/double/string/bool/date/enum/struct/array',
    `precision`     int                      DEFAULT NULL COMMENT '数据精度(小数位数)',
    `unit_symbol`   varchar(32)              DEFAULT NULL COMMENT '数据单位符号(如: ℃, %, m/s)',
    `data_write`    tinyint(1)      NOT NULL DEFAULT '0' COMMENT '读写类型: 0只读 1读写',
    `data_config`   text COMMENT '数据约束配置(JSON格式)',
    `remark`        varchar(512)             DEFAULT NULL COMMENT '属性描述',
    `default_value` varchar(255)             DEFAULT NULL COMMENT '默认值',
    `sort_order`    int             NOT NULL DEFAULT '0' COMMENT '排序号',
    `deleted`       bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标记(0未删除,>0为删除时间戳ms)',
    `create_by`     varchar(64)              DEFAULT NULL COMMENT '创建用户',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     varchar(64)              DEFAULT NULL COMMENT '更新用户',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_prod_prop_code` (`tenant`, `prod_id`, `prop_code`, `deleted`),
    KEY `idx_tenant` (`tenant`),
    KEY `idx_prod_id` (`prod_id`),
    KEY `idx_prop_code` (`prop_code`),
    KEY `idx_data_type` (`data_type`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='产品属性表v3版本';

CREATE TABLE IF NOT EXISTS `iot_md_product_v3`
(
    `id`               bigint          NOT NULL COMMENT '产品ID',
    `tenant`           int unsigned    NOT NULL DEFAULT '0' COMMENT '租户编号',
    `prod_number`      varchar(64)     NOT NULL COMMENT '产品编号(业务编码)',
    `prod_name`        varchar(128)    NOT NULL COMMENT '产品名称',
    `prod_name_pinyin` varchar(128)             DEFAULT NULL COMMENT '产品名称拼音首字母(用于检索)',
    `prod_category_id` bigint                   DEFAULT NULL COMMENT '产品品类ID(内部使用,查询时通过code转换)',
    `node_type`        int                      DEFAULT NULL COMMENT '节点类型: 1直连设备 2网关设备 3网关子设备',
    `prod_brand_id`    bigint                   DEFAULT NULL COMMENT '产品品牌ID(内部使用,查询时通过code转换)',
    `prod_model`       varchar(128)             DEFAULT NULL COMMENT '产品型号',
    `protocol_id`      bigint                   DEFAULT NULL COMMENT '消息协议ID(内部使用,查询时通过code转换)',
    `remark`           varchar(512)             DEFAULT NULL COMMENT '产品描述',
    `extend_config`    text COMMENT '产品扩展字段,更多描述的可能(JSON等)',
    `status`           tinyint         NOT NULL DEFAULT '1' COMMENT '产品状态: 1正常 0禁用',
    `prod_version`     varchar(64)              DEFAULT NULL COMMENT '产品版本号',
    `prod_icon`        bigint                   DEFAULT NULL COMMENT '产品图标(文件ID)',
    `prod_pic`         bigint                   DEFAULT NULL COMMENT '产品图片(文件ID)',
    `deleted`          bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标记(0未删除,>0为删除时间戳ms)',
    `create_by`        varchar(64)              DEFAULT NULL COMMENT '创建用户',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        varchar(64)              DEFAULT NULL COMMENT '更新用户',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_prod_number` (`tenant`, `prod_number`, `deleted`),
    KEY `idx_tenant` (`tenant`),
    KEY `idx_prod_name` (`prod_name`),
    KEY `idx_prod_name_pinyin` (`prod_name_pinyin`),
    KEY `idx_prod_category_id` (`prod_category_id`),
    KEY `idx_prod_brand_id` (`prod_brand_id`),
    KEY `idx_protocol_id` (`protocol_id`),
    KEY `idx_node_type` (`node_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='产品定义v3版本';

CREATE TABLE IF NOT EXISTS `iot_shared_value_dict_v3`
(
    `id`          bigint          NOT NULL COMMENT '字典ID',
    `tenant`      int unsigned    NOT NULL DEFAULT '0' COMMENT '租户编号',
    `dict_type`   varchar(64)     NOT NULL COMMENT '字典类型: unit_symbol/category_name/brand_name/protocol_name',
    `dict_value`  varchar(128)    NOT NULL COMMENT '字典值',
    `dict_label`  varchar(128)             DEFAULT NULL COMMENT '字典显示名称',
    `use_count`   int             NOT NULL DEFAULT '0' COMMENT '使用次数(用于排序,越常用越靠前)',
    `sort_order`  int             NOT NULL DEFAULT '0' COMMENT '排序号',
    `status`      tinyint         NOT NULL DEFAULT '1' COMMENT '状态: 1启用 0禁用',
    `remark`      varchar(512)             DEFAULT NULL COMMENT '备注',
    `deleted`     bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标记(0未删除,>0为删除时间戳ms)',
    `create_by`   varchar(64)              DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(64)              DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_dict_type_value` (`tenant`, `dict_type`, `dict_value`, `deleted`),
    KEY `idx_tenant` (`tenant`),
    KEY `idx_dict_type` (`dict_type`),
    KEY `idx_use_count` (`use_count`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='共享值字典表v3版本(新增功能)';

CREATE TABLE IF NOT EXISTS `iot_sys_dict_item_v3`
(
    `id`               bigint          NOT NULL COMMENT '字典项ID',
    `tenant`           int unsigned    NOT NULL DEFAULT '0' COMMENT '租户编号',
    `dict_id`          bigint          NOT NULL COMMENT '字典类型ID',
    `item_code`        varchar(64)     NOT NULL COMMENT '字典编码(v3新增,用于跨环境关联)',
    `item_parent_id`   bigint                   DEFAULT NULL COMMENT '字典上级ID',
    `item_sort`        int             NOT NULL DEFAULT '0' COMMENT '字典排序',
    `item_level_code`  varchar(256)             DEFAULT NULL COMMENT '字典编码层级(点分隔,如: root.parent.child)',
    `item_level_label` varchar(512)             DEFAULT NULL COMMENT '字典name层级(斜杠分隔,如: 根节点/父节点/子节点)',
    `item_label`       varchar(128)    NOT NULL COMMENT '字典标签(显示名称)',
    `item_value`       varchar(128)             DEFAULT NULL COMMENT '字典键值',
    `status`           tinyint         NOT NULL DEFAULT '0' COMMENT '状态: 0正常 1停用',
    `remark`           varchar(512)             DEFAULT NULL COMMENT '备注',
    `admin_id`         bigint                   DEFAULT NULL COMMENT '产品库中对应的ID',
    `deleted`          bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标记(0未删除,>0为删除时间戳ms)',
    `create_by`        varchar(64)              DEFAULT NULL COMMENT '创建用户',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        varchar(64)              DEFAULT NULL COMMENT '更新用户',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_dict_item_code` (`tenant`, `dict_id`, `item_code`, `deleted`),
    KEY `idx_tenant` (`tenant`),
    KEY `idx_dict_id` (`dict_id`),
    KEY `idx_item_parent_id` (`item_parent_id`),
    KEY `idx_item_level_code` (`item_level_code`),
    KEY `idx_item_value` (`item_value`),
    KEY `idx_status` (`status`),
    KEY `idx_admin_id` (`admin_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='字典项v3版本(增强版)';

CREATE TABLE IF NOT EXISTS `iot_sys_dict_v3`
(
    `id`          bigint          NOT NULL COMMENT '字典主键',
    `dict_name`   varchar(128)    NOT NULL COMMENT '字典名称',
    `dict_code`   varchar(64)     NOT NULL COMMENT '字典编码(用于查询,v3核心字段)',
    `status`      tinyint         NOT NULL DEFAULT '0' COMMENT '状态: 0正常 1停用',
    `type`        tinyint         NOT NULL DEFAULT '0' COMMENT '字典类型: 0系统字典 1业务字典',
    `remark`      varchar(512)             DEFAULT NULL COMMENT '备注',
    `deleted`     bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标记(0未删除,>0为删除时间戳ms)',
    `create_by`   varchar(64)              DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(64)              DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_code_deleted` (`dict_code`, `deleted`),
    KEY `idx_dict_code` (`dict_code`),
    KEY `idx_status` (`status`),
    KEY `idx_type` (`type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='字典类型v3版本(增强版)';

INSERT IGNORE INTO `iot_sys_dict_v3` (`id`, `dict_name`, `dict_code`, `status`, `type`, `remark`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1000, '品牌', 'PRODUCT_BRAND', 1, 0, NULL, 0, 'system', '2025-12-17 17:40:06', 'system', '2025-12-17 17:40:08');
INSERT IGNORE INTO `iot_sys_dict_v3` (`id`, `dict_name`, `dict_code`, `status`, `type`, `remark`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1001, '品类', 'PRODUCT_CATEGORY', 1, 0, NULL, 0, 'system', '2025-12-17 17:40:36', 'system', '2025-12-17 17:40:41');
INSERT IGNORE INTO `iot_sys_dict_v3` (`id`, `dict_name`, `dict_code`, `status`, `type`, `remark`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1002, '事件类别', 'PRODUCT_EVENT_CATEGORY', 10, 0, NULL, 0, 'system', '2025-12-19 16:22:37', 'system', '2025-12-19 16:22:54');
INSERT IGNORE INTO `iot_sys_dict_v3` (`id`, `dict_name`, `dict_code`, `status`, `type`, `remark`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1003, '协议', 'PRODUCT_PROTOCOL', 1, 0, NULL, 0, 'system', '2025-12-17 17:40:36', 'system', '2025-12-17 17:40:41');

SET FOREIGN_KEY_CHECKS = 1;
