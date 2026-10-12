-- liquibase formatted sql

-- changeset chx:ddl-1
-- ----------------------------
-- Table structure for electricity_node
-- ----------------------------
CREATE TABLE IF NOT EXISTS `electricity_node` (
          id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
          code VARCHAR(100) DEFAULT NULL COMMENT '节点编码',
          name VARCHAR(100) DEFAULT NULL COMMENT '节点名称',
          type VARCHAR(100) DEFAULT NULL COMMENT '节点类型',
          device_ids VARCHAR(500) DEFAULT NULL COMMENT '节点关联设备 ids',
          children_ids text DEFAULT NULL COMMENT '子结点 ids',
          location VARCHAR(200) DEFAULT NULL COMMENT '所在位置',
          description VARCHAR(500) DEFAULT NULL COMMENT '描述',
          web_style text DEFAULT NULL COMMENT '网页样式 (前端定位用)',
          deleted bigint(20) DEFAULT NULL COMMENT '删除时间戳0表示未删除',
          tenant int(11) DEFAULT NULL COMMENT '租户id',
          create_by varchar(255) DEFAULT NULL COMMENT '创建用户账号',
          create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
          update_by varchar(255) DEFAULT NULL COMMENT '修改用户账号',
          update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
          creator_id varchar(100) DEFAULT NULL COMMENT '创建用户id',
          creator_type varchar(50) DEFAULT NULL COMMENT '创建用户类型',
          updater_id varchar(100) DEFAULT NULL COMMENT '更新用户id',
          updater_type varchar(50) DEFAULT NULL COMMENT '更新用户类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供电节点表';

-- changeset chx:ddl-2
-- ----------------------------
-- Table structure for electricity_connection
-- ----------------------------
CREATE TABLE IF NOT EXISTS `electricity_connection` (
        id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
        line_name VARCHAR(100) DEFAULT NULL COMMENT '线路名称',
        from_node_id bigint(20) DEFAULT NULL COMMENT '开始节点 id',
        to_node_id bigint(20) DEFAULT NULL COMMENT '结束节点 id',
        web_style text DEFAULT NULL COMMENT '网页样式 (前端定位用)',
        deleted bigint(20) DEFAULT NULL COMMENT '删除时间戳0表示未删除',
        tenant int(11) DEFAULT NULL COMMENT '租户id',
        create_by varchar(255) DEFAULT NULL COMMENT '创建用户账号',
        create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        update_by varchar(255) DEFAULT NULL COMMENT '修改用户账号',
        update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
        creator_id varchar(100) DEFAULT NULL COMMENT '创建用户id',
        creator_type varchar(50) DEFAULT NULL COMMENT '创建用户类型',
        updater_id varchar(100) DEFAULT NULL COMMENT '更新用户id',
        updater_type varchar(50) DEFAULT NULL COMMENT '更新用户类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供电关系 (供电节点连接关系) 表';

-- changeset chx:ddl-3
-- ----------------------------
-- Table structure for line_loss_node
-- ----------------------------
CREATE TABLE IF NOT EXISTS `line_loss_node` (
          id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
          code VARCHAR(100) DEFAULT NULL COMMENT '节点编码',
          name VARCHAR(100) DEFAULT NULL COMMENT '节点名称',
          type VARCHAR(100) DEFAULT NULL COMMENT '节点类型',
          device_ids VARCHAR(500) DEFAULT NULL COMMENT '节点关联设备 ids',
          children_ids VARCHAR(225) DEFAULT NULL COMMENT '子结点 ids',
          location VARCHAR(200) DEFAULT NULL COMMENT '所在位置',
          description VARCHAR(500) DEFAULT NULL COMMENT '描述',
          web_style text DEFAULT NULL COMMENT '网页样式 (前端定位用)',
          deleted bigint(20) DEFAULT NULL COMMENT '删除时间戳 0 表示未删除',
          tenant int(11) DEFAULT NULL COMMENT '租户 id',
          create_by varchar(255) DEFAULT NULL COMMENT '创建用户账号',
          create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
          update_by varchar(255) DEFAULT NULL COMMENT '修改用户账号',
          update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
          creator_id varchar(100) DEFAULT NULL COMMENT '创建用户 id',
          creator_type varchar(50) DEFAULT NULL COMMENT '创建用户类型',
          updater_id varchar(100) DEFAULT NULL COMMENT '更新用户 id',
          updater_type varchar(50) DEFAULT NULL COMMENT '更新用户类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线损节点表';

-- changeset chx:ddl-4
-- ----------------------------
-- Table structure for line_loss_connection
-- ----------------------------
CREATE TABLE IF NOT EXISTS `line_loss_connection` (
        id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
        line_name VARCHAR(100) DEFAULT NULL COMMENT '线路名称',
        from_node_id bigint(20) DEFAULT NULL COMMENT '开始节点 id',
        to_node_id bigint(20) DEFAULT NULL COMMENT '结束节点 id',
        web_style text DEFAULT NULL COMMENT '网页样式 (前端定位用)',
        from_device_id bigint(20) DEFAULT NULL COMMENT '输入设备 id',
        to_device_id bigint(20) DEFAULT NULL COMMENT '输出设备 id',
        line_loss_rate DECIMAL(10,4) DEFAULT NULL COMMENT '最近一次的线损率',
        location VARCHAR(200) DEFAULT NULL COMMENT '所在位置',
        deleted bigint(20) DEFAULT NULL COMMENT '删除时间戳 0 表示未删除',
        tenant int(11) DEFAULT NULL COMMENT '租户 id',
        create_by varchar(255) DEFAULT NULL COMMENT '创建用户账号',
        create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        update_by varchar(255) DEFAULT NULL COMMENT '修改用户账号',
        update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
        creator_id varchar(100) DEFAULT NULL COMMENT '创建用户 id',
        creator_type varchar(50) DEFAULT NULL COMMENT '创建用户类型',
        updater_id varchar(100) DEFAULT NULL COMMENT '更新用户 id',
        updater_type varchar(50) DEFAULT NULL COMMENT '更新用户类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线损关系 (线损节点连接关系) 表';

-- changeset chx:ddl-5
-- ----------------------------
-- Table structure for line_loss_collect_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `line_loss_job_config` (
          id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
          scene_type VARCHAR(50) DEFAULT NULL COMMENT '周期类型 (每日，每月，每周，每年)',
          period_type VARCHAR(50) DEFAULT NULL COMMENT '周期类型 (每日，每月，每周，每年)',
          days VARCHAR(100) DEFAULT NULL COMMENT '周期值 (周 1-7,月 1-31,年 1-12)',
          hour_time VARCHAR(10) DEFAULT NULL COMMENT '执行时间',
          enable tinyint(1) DEFAULT NULL COMMENT '是否启用 (0-禁用，1-启用)',
          cron VARCHAR(100) DEFAULT NULL COMMENT 'cron 表达式',
          last_execute_time datetime DEFAULT NULL COMMENT '最后一次执行时间',
          next_execute_time datetime DEFAULT NULL COMMENT '下次执行时间',
          attribute_code VARCHAR(100) DEFAULT NULL COMMENT '采集的属性code',
          remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
          deleted bigint(20) DEFAULT NULL COMMENT '删除时间戳 0 表示未删除',
          tenant int(11) DEFAULT NULL COMMENT '租户 id',
          create_by varchar(255) DEFAULT NULL COMMENT '创建用户账号',
          create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
          update_by varchar(255) DEFAULT NULL COMMENT '修改用户账号',
          update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
          creator_id varchar(100) DEFAULT NULL COMMENT '创建用户 id',
          creator_type varchar(50) DEFAULT NULL COMMENT '创建用户类型',
          updater_id varchar(100) DEFAULT NULL COMMENT '更新用户 id',
          updater_type varchar(50) DEFAULT NULL COMMENT '更新用户类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线损 job 配置表';

-- changeset chx:ddl-6
-- ----------------------------
-- Table structure for line_loss_alarm_rule
-- ----------------------------
CREATE TABLE IF NOT EXISTS `line_loss_alarm_rule` (
          id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
          triggered_rule_id bigint(20) DEFAULT NULL COMMENT '触发的规则 id',
          name VARCHAR(100) DEFAULT NULL COMMENT '规则名称',
          `remark` VARCHAR(500) DEFAULT NULL COMMENT '描述',
          alarm_level_id bigint(20) DEFAULT NULL COMMENT '报警级别id',
          alarm_level_name VARCHAR(100) DEFAULT NULL COMMENT '报警级别名称',
          estimated_handler_time int(11) DEFAULT NULL COMMENT '处理时限值',
          estimated_handler_time_name VARCHAR(100) DEFAULT NULL COMMENT '处理时限名称',
          estimated_handler_unit VARCHAR(50) DEFAULT NULL COMMENT '处理时限单位',
          emergency_event_type_id bigint(20) DEFAULT NULL COMMENT '事件类型 id',
          emergency_event_type_code VARCHAR(100) DEFAULT NULL COMMENT '事件类型编码',
          emergency_event_type_name VARCHAR(100) DEFAULT NULL COMMENT '事件类型名称',
          `condition` VARCHAR(500) DEFAULT NULL COMMENT '触发条件，符合和值用逗号隔开，多组条件用分号隔开即：符合，值;符合，值 (例如：EQ,10;GE,12)',
          `enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 (0-禁用，1-启用)',
          deleted bigint(20) DEFAULT NULL COMMENT '删除时间戳 0 表示未删除',
          tenant int(11) DEFAULT NULL COMMENT '租户 id',
          create_by varchar(255) DEFAULT NULL COMMENT '创建用户账号',
          create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
          update_by varchar(255) DEFAULT NULL COMMENT '修改用户账号',
          update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
          creator_id varchar(100) DEFAULT NULL COMMENT '创建用户 id',
          creator_type varchar(50) DEFAULT NULL COMMENT '创建用户类型',
          updater_id varchar(100) DEFAULT NULL COMMENT '更新用户 id',
          updater_type varchar(50) DEFAULT NULL COMMENT '更新用户类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线损报警规则表';

-- changeset chx:ddl-7
-- ----------------------------
-- Table structure for line_loss_alarm_rule_line
-- ----------------------------
CREATE TABLE IF NOT EXISTS `line_loss_alarm_rule_line` (
          id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
          alarm_rule_id bigint(20) DEFAULT NULL COMMENT '报警规则 id',
          line_id bigint(20) DEFAULT NULL COMMENT '线路 id',
          deleted bigint(20) DEFAULT NULL COMMENT '删除时间戳 0 表示未删除',
          tenant int(11) DEFAULT NULL COMMENT '租户 id',
          create_by varchar(255) DEFAULT NULL COMMENT '创建用户账号',
          create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
          update_by varchar(255) DEFAULT NULL COMMENT '修改用户账号',
          update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
          creator_id varchar(100) DEFAULT NULL COMMENT '创建用户 id',
          creator_type varchar(50) DEFAULT NULL COMMENT '创建用户类型',
          updater_id varchar(100) DEFAULT NULL COMMENT '更新用户 id',
          updater_type varchar(50) DEFAULT NULL COMMENT '更新用户类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警规则的线路表';
