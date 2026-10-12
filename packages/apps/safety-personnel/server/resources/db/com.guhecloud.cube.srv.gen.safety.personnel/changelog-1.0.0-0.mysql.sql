-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_org_node';
CREATE TABLE `sp_org_node`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id`    bigint(20) unsigned NULL COMMENT '父级安全管理组织节点ID',
    `name`         varchar(64)         NOT NULL COMMENT '安全管理组织节点名称',
    `level`        int(10) unsigned    NULL COMMENT '节点层级',
    `sequence`     varchar(500)        NULL COMMENT '树序列号',
    `sort_no`      int(10) unsigned    NOT NULL DEFAULT 0 COMMENT '排序号',
    `tenant`       int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`    varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`   varchar(100)        NULL COMMENT '创建人ID',
    `creator_type` varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`  datetime            NULL COMMENT '创建时间',
    `update_by`    varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`   varchar(100)        NULL COMMENT '更新人ID',
    `updater_type` varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`  datetime            NULL COMMENT '更新时间',
    `deleted`      bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_org_node_name_deleted` (`tenant`, `parent_id`, `name`, `deleted`),
    KEY `idx_sp_org_node_parent` (`tenant`, `parent_id`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全管理组织展示节点';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_org_node_member';
CREATE TABLE `sp_org_node_member`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `org_node_id`  bigint(20) unsigned NOT NULL COMMENT '安全管理组织节点ID',
    `user_id`      bigint(20) unsigned NOT NULL COMMENT 'OUAA用户ID',
    `user_name`    varchar(64)         NOT NULL COMMENT '用户姓名',
    `dept_name`    varchar(128)        NULL COMMENT 'OUAA部门名称',
    `mobile`       varchar(32)         NULL COMMENT '手机号',
    `tenant`       int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`    varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`   varchar(100)        NULL COMMENT '创建人ID',
    `creator_type` varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`  datetime            NULL COMMENT '创建时间',
    `update_by`    varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`   varchar(100)        NULL COMMENT '更新人ID',
    `updater_type` varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`  datetime            NULL COMMENT '更新时间',
    `deleted`      bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_org_node_member_deleted` (`tenant`, `org_node_id`, `user_id`, `deleted`),
    KEY `idx_sp_org_node_member_user` (`tenant`, `user_id`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全管理组织展示成员';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_person';
CREATE TABLE `sp_person`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      bigint(20) unsigned NOT NULL COMMENT 'OUAA用户ID',
    `user_name`    varchar(64)         NOT NULL COMMENT '用户姓名',
    `phone`        varchar(32)         NULL COMMENT '手机号',
    `company_name` varchar(128)        NULL COMMENT 'OUAA公司名称',
    `dept_name`    varchar(128)        NULL COMMENT 'OUAA部门名称',
    `post_name`    varchar(128)        NULL COMMENT 'OUAA岗位名称',
    `status`       varchar(32)         NOT NULL DEFAULT 'ACTIVE' COMMENT '人员状态',
    `linked_at`    datetime            NULL COMMENT '纳入安全人员库时间',
    `tenant`       int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`    varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`   varchar(100)        NULL COMMENT '创建人ID',
    `creator_type` varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`  datetime            NULL COMMENT '创建时间',
    `update_by`    varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`   varchar(100)        NULL COMMENT '更新人ID',
    `updater_type` varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`  datetime            NULL COMMENT '更新时间',
    `deleted`      bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_person_user_deleted` (`tenant`, `user_id`, `deleted`),
    KEY `idx_sp_person_name` (`tenant`, `user_name`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全人员库';

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_person_type_rel';
CREATE TABLE `sp_person_type_rel`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `person_id`    bigint(20) unsigned NOT NULL COMMENT '安全人员ID',
    `type_code`    varchar(32)         NOT NULL COMMENT '人员类型编码',
    `tenant`       int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`    varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`   varchar(100)        NULL COMMENT '创建人ID',
    `creator_type` varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`  datetime            NULL COMMENT '创建时间',
    `update_by`    varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`   varchar(100)        NULL COMMENT '更新人ID',
    `updater_type` varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`  datetime            NULL COMMENT '更新时间',
    `deleted`      bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_person_type_deleted` (`tenant`, `person_id`, `type_code`, `deleted`),
    KEY `idx_sp_person_type_code` (`tenant`, `type_code`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全人员类型关系';

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_safety_committee';
CREATE TABLE `sp_safety_committee`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `committee_name` varchar(64)         NOT NULL COMMENT '安全生产委员会名称',
    `dept_id`        bigint(20) unsigned NULL COMMENT '负责单位/部门ID，来源OUAA组织',
    `dept_name`      varchar(128)        NULL COMMENT '负责单位/部门名称快照',
    `contact_phone`  varchar(32)         NULL COMMENT '联系电话',
    `introduction`   varchar(500)        NULL COMMENT '简介',
    `member_count`   int(10) unsigned    NOT NULL DEFAULT 0 COMMENT '成员数量',
    `tenant`         int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`      varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`     varchar(100)        NULL COMMENT '创建人ID',
    `creator_type`   varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`    datetime            NULL COMMENT '创建时间',
    `update_by`      varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`     varchar(100)        NULL COMMENT '更新人ID',
    `updater_type`   varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`    datetime            NULL COMMENT '更新时间',
    `deleted`        bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_safety_committee_name_deleted` (`tenant`, `committee_name`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全生产委员会';

-- changeset lanchun:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_committee_member';
CREATE TABLE `sp_committee_member`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `committee_id` bigint(20) unsigned NOT NULL COMMENT '安全生产委员会ID',
    `user_id`      bigint(20) unsigned NOT NULL COMMENT 'OUAA用户ID',
    `user_name`    varchar(64)         NOT NULL COMMENT '用户姓名',
    `phone`        varchar(32)         NULL COMMENT '手机号',
    `dept_name`    varchar(128)        NULL COMMENT 'OUAA部门名称',
    `post_name`    varchar(128)        NULL COMMENT 'OUAA岗位名称',
    `join_time`    datetime            NULL COMMENT '加入时间',
    `active`       tinyint(1) unsigned NOT NULL DEFAULT 1 COMMENT '是否在任',
    `tenant`       int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`    varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`   varchar(100)        NULL COMMENT '创建人ID',
    `creator_type` varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`  datetime            NULL COMMENT '创建时间',
    `update_by`    varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`   varchar(100)        NULL COMMENT '更新人ID',
    `updater_type` varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`  datetime            NULL COMMENT '更新时间',
    `deleted`      bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_committee_member_deleted` (`tenant`, `committee_id`, `user_id`, `deleted`),
    KEY `idx_sp_committee_member_user` (`tenant`, `user_id`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全生产委员会成员';

-- changeset lanchun:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_committee_change_log';
CREATE TABLE `sp_committee_change_log`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `committee_id`   bigint(20) unsigned NOT NULL COMMENT '安全生产委员会ID',
    `change_type`    varchar(64)         NOT NULL COMMENT '变更类型',
    `change_content` text                NULL COMMENT '变更内容JSON',
    `operator_id`    bigint(20) unsigned NULL COMMENT '操作人OUAA用户ID',
    `operator_name`  varchar(64)         NULL COMMENT '操作人姓名',
    `change_time`    datetime            NOT NULL COMMENT '变更时间',
    `tenant`         int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`      varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`     varchar(100)        NULL COMMENT '创建人ID',
    `creator_type`   varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`    datetime            NULL COMMENT '创建时间',
    `update_by`      varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`     varchar(100)        NULL COMMENT '更新人ID',
    `updater_type`   varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`    datetime            NULL COMMENT '更新时间',
    `deleted`        bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    KEY `idx_sp_committee_change_log_committee` (`tenant`, `committee_id`, `change_time`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全生产委员会变更记录';

-- changeset lanchun:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_committee_attachment';
CREATE TABLE `sp_committee_attachment`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `committee_id` bigint(20) unsigned NOT NULL COMMENT '安全生产委员会ID',
    `file_id`      bigint(20) unsigned NOT NULL COMMENT '文件ID',
    `file_name`    varchar(255)        NOT NULL COMMENT '文件名称',
    `file_size`    bigint(20) unsigned NULL COMMENT '文件大小',
    `file_type`    varchar(64)         NULL COMMENT '文件类型',
    `tenant`       int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`    varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`   varchar(100)        NULL COMMENT '创建人ID',
    `creator_type` varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`  datetime            NULL COMMENT '创建时间',
    `update_by`    varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`   varchar(100)        NULL COMMENT '更新人ID',
    `updater_type` varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`  datetime            NULL COMMENT '更新时间',
    `deleted`      bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_committee_attachment_deleted` (`tenant`, `committee_id`, `file_id`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='安全生产委员会附件';

-- changeset lanchun:ddl-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_cert_category';
CREATE TABLE `sp_cert_category`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id`    bigint(20) unsigned NULL COMMENT '父级证书分类ID',
    `name`         varchar(64)         NOT NULL COMMENT '证书分类名称',
    `level`        int(10) unsigned    NULL COMMENT '节点层级',
    `sequence`     varchar(500)        NULL COMMENT '树序列号',
    `sort_no`      int(10) unsigned    NOT NULL DEFAULT 0 COMMENT '排序号',
    `tenant`       int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`    varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`   varchar(100)        NULL COMMENT '创建人ID',
    `creator_type` varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`  datetime            NULL COMMENT '创建时间',
    `update_by`    varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`   varchar(100)        NULL COMMENT '更新人ID',
    `updater_type` varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`  datetime            NULL COMMENT '更新时间',
    `deleted`      bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_cert_category_name_deleted` (`tenant`, `parent_id`, `name`, `deleted`),
    KEY `idx_sp_cert_category_parent` (`tenant`, `parent_id`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='证书分类';

-- changeset lanchun:ddl-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_cert_type';
CREATE TABLE `sp_cert_type`
(
    `id`                 bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_id`        bigint(20) unsigned NOT NULL COMMENT '证书分类ID',
    `type_name`          varchar(64)         NOT NULL COMMENT '证书类型名称',
    `definition`         varchar(500)        NULL COMMENT '定义说明',
    `valid_period`       int(10) unsigned    NULL COMMENT '有效期数值',
    `valid_period_unit`  varchar(16)         NULL COMMENT '有效期单位',
    `warn_days`          int(10) unsigned    NULL COMMENT '到期提前提醒天数',
    `need_review`        tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否需要复审',
    `review_period`      int(10) unsigned    NULL COMMENT '复审周期数值',
    `review_period_unit` varchar(16)         NULL COMMENT '复审周期单位',
    `tenant`             int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`          varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`         varchar(100)        NULL COMMENT '创建人ID',
    `creator_type`       varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`        datetime            NULL COMMENT '创建时间',
    `update_by`          varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`         varchar(100)        NULL COMMENT '更新人ID',
    `updater_type`       varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`        datetime            NULL COMMENT '更新时间',
    `deleted`            bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_cert_type_name_deleted` (`tenant`, `category_id`, `type_name`, `deleted`),
    KEY `idx_sp_cert_type_category` (`tenant`, `category_id`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='证书类型';

-- changeset lanchun:ddl-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_employee_cert';
CREATE TABLE `sp_employee_cert`
(
    `id`                          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`                     bigint(20) unsigned NOT NULL COMMENT 'OUAA用户ID',
    `user_name`                   varchar(64)         NULL COMMENT '用户姓名快照',
    `category_id`                 bigint(20) unsigned NOT NULL COMMENT '证书分类ID',
    `type_id`                     bigint(20) unsigned NOT NULL COMMENT '证书类型ID',
    `cert_name`                   varchar(128)        NOT NULL COMMENT '证书名称',
    `cert_no`                     varchar(128)        NULL COMMENT '证书编号',
    `issue_org`                   varchar(128)        NULL COMMENT '发证机构',
    `issue_date`                  date                NULL COMMENT '发证日期',
    `valid_start`                 date                NULL COMMENT '有效期开始日期',
    `valid_end`                   date                NULL COMMENT '有效期结束日期',
    `valid_period_snapshot`       int(10) unsigned    NULL COMMENT '有效期数值快照',
    `valid_period_unit_snapshot`  varchar(16)         NULL COMMENT '有效期单位快照',
    `warn_days_snapshot`          int(10) unsigned    NULL COMMENT '到期提前提醒天数快照',
    `need_review`                 tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否需要复审',
    `review_period_snapshot`      int(10) unsigned    NULL COMMENT '复审周期数值快照',
    `review_period_unit_snapshot` varchar(16)         NULL COMMENT '复审周期单位快照',
    `review_status`               varchar(32)         NULL COMMENT '复审状态',
    `attachment_file_id`          bigint(20) unsigned NULL COMMENT '附件文件ID',
    `cert_status`                 varchar(32)         NOT NULL DEFAULT 'VALID' COMMENT '证书状态',
    `warn_notified`               tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否已发送到期提醒',
    `warn_notify_time`            datetime            NULL COMMENT '到期提醒发送时间',
    `tenant`                      int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`                   varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`                  varchar(100)        NULL COMMENT '创建人ID',
    `creator_type`                varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`                 datetime            NULL COMMENT '创建时间',
    `update_by`                   varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`                  varchar(100)        NULL COMMENT '更新人ID',
    `updater_type`                varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`                 datetime            NULL COMMENT '更新时间',
    `deleted`                     bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sp_employee_cert_no_deleted` (`tenant`, `user_id`, `cert_no`, `deleted`),
    KEY `idx_sp_employee_cert_user` (`tenant`, `user_id`, `deleted`),
    KEY `idx_sp_employee_cert_user_name` (`tenant`, `user_name`, `deleted`),
    KEY `idx_sp_employee_cert_valid_end` (`tenant`, `valid_end`, `warn_notified`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='人员证书';

-- changeset lanchun:ddl-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'sp_employee_cert_change_log';
CREATE TABLE `sp_employee_cert_change_log`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `cert_id`        bigint(20) unsigned NOT NULL COMMENT '人员证书ID',
    `change_type`    varchar(64)         NOT NULL COMMENT '变更类型',
    `change_content` text                NULL COMMENT '变更内容JSON',
    `operator_id`    bigint(20) unsigned NULL COMMENT '操作人OUAA用户ID',
    `operator_name`  varchar(64)         NULL COMMENT '操作人姓名',
    `change_time`    datetime            NOT NULL COMMENT '变更时间',
    `tenant`         int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`      varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`     varchar(100)        NULL COMMENT '创建人ID',
    `creator_type`   varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`    datetime            NULL COMMENT '创建时间',
    `update_by`      varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`     varchar(100)        NULL COMMENT '更新人ID',
    `updater_type`   varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`    datetime            NULL COMMENT '更新时间',
    `deleted`        bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`),
    KEY `idx_sp_employee_cert_change_log_cert` (`tenant`, `cert_id`, `change_time`, `deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='人员证书变更记录';
