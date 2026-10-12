-- liquibase formatted sql


-- changeset ht:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_area') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_area
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_area` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_id` bigint(20) NOT NULL COMMENT '消杀项目Id',
  `area_name` varchar(100) NOT NULL COMMENT '消杀区域名称',
  `area_detail` varchar(500) DEFAULT NULL COMMENT '消杀区域详情',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀区域';

-- changeset ht:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_data_type') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_data_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_data_type` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `data_type` varchar(20) NOT NULL COMMENT '业务类型',
  `data_name` varchar(100) NOT NULL COMMENT '业务名称',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `icon` bigint(20) DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀业务分类表';

-- changeset ht:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_group') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_group
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_group` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_id` bigint(20) NOT NULL COMMENT '消杀项目ID',
  `group_name` varchar(100) NOT NULL COMMENT '消杀小组名称',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀小组';


-- changeset ht:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_group_user') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_group_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_group_user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) NOT NULL COMMENT '小组id',
  `user_id` bigint(20) NOT NULL COMMENT '组员id',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀小组成员表';

-- changeset ht:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_key_place') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_key_place
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_key_place` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `place_name` varchar(100) NOT NULL COMMENT '场所名称',
  `place_type_id` bigint(20) NOT NULL COMMENT '场所分类',
  `place_type_name` varchar(100) DEFAULT NULL COMMENT '场所分类名称',
  `place_detail` varchar(100) DEFAULT NULL COMMENT '重点场所详情',
  `longitude` decimal(50,15) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(50,15) DEFAULT NULL COMMENT '纬度',
  `imgs` varchar(500) DEFAULT NULL COMMENT '场所图片',
  `place_description` varchar(500) DEFAULT NULL COMMENT '场所说明',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `district` varchar(50) DEFAULT NULL COMMENT '地址所在区',
  `place_address` varchar(255) DEFAULT NULL COMMENT '经纬度地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀重点场所';

-- changeset ht:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_material') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_material
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_material` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `file_name` varchar(100) NOT NULL COMMENT '资料名称',
  `file_type_id` bigint(20) NOT NULL COMMENT '文件类型',
  `file_type_name` varchar(100) DEFAULT NULL COMMENT '文件分类名称',
  `file_id` varchar(100) NOT NULL COMMENT '上传的文件Id',
  `upload_user_id` bigint(20) NOT NULL COMMENT '上传人id',
  `upload_time` datetime NOT NULL COMMENT '上传时间',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀资料';
-- changeset ht:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_notify_record') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_notify_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_notify_record` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `template_id` bigint(20) NOT NULL COMMENT '模板id',
  `scene_code` varchar(200) DEFAULT NULL COMMENT '参加code',
  `business_id` bigint(20) NOT NULL COMMENT '通知的业务主键',
  `notify_users` varchar(1000) DEFAULT NULL COMMENT '通知用户',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀通知记录';
-- changeset ht:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_plan') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_plan
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_plan` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `plan_name` varchar(100) NOT NULL COMMENT '计划名称',
  `project_id` bigint(20) NOT NULL COMMENT '所属项目',
  `begin_time` datetime NOT NULL COMMENT '计划生效时间',
  `end_time` datetime DEFAULT NULL COMMENT '计划失效时间（为空表示永不失效）',
  `task_generation_cycle_config` text NOT NULL COMMENT '任务生成周期配置（json）',
  `project_leader_id` bigint(20) NOT NULL COMMENT '项目负责人Id',
  `task_time_limit_config` varchar(200) DEFAULT NULL COMMENT '任务时限配置（为空表示永不截至）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` varchar(20) NOT NULL COMMENT '计划状态',
  `enable` varchar(20) NOT NULL COMMENT '是否启用',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀计划';
-- changeset ht:ddl-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_plan_task_config') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_plan_task_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_plan_task_config` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  `task_type_id` bigint(20) NOT NULL COMMENT '任务类型',
  `task_type_name` varchar(100) DEFAULT NULL COMMENT '任务类型名称',
  `task_name` varchar(100) NOT NULL COMMENT '消杀任务名称',
  `task_area_id` bigint(20) NOT NULL COMMENT '消杀区域id',
  `disinfection_frequency` varchar(255) DEFAULT NULL COMMENT '消杀频次',
  `notification_type` varchar(50) NOT NULL COMMENT '通知方式',
  `notification_group_id` bigint(20) DEFAULT NULL COMMENT '通知消杀小组ID',
  `notification_dept_id` bigint(20) DEFAULT NULL COMMENT '通知消杀的人员（为空时，通知整个小组）',
  `notification_post_Id` bigint(20) DEFAULT NULL,
  `notification_role_id` bigint(20) DEFAULT NULL,
  `generate_count` int(11) NOT NULL DEFAULT '0' COMMENT '配置生成次数',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀任务配置';
-- changeset ht:ddl-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_project') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_project
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_project` (
  `id` bigint(20) NOT NULL,
  `project_code` varchar(100) NOT NULL COMMENT '项目编号',
  `project_name` varchar(50) NOT NULL COMMENT '项目名称',
  `project_type_id` bigint(20) NOT NULL COMMENT '项目类型',
  `project_type_name` varchar(100) DEFAULT NULL COMMENT '项目类型名称',
  `company` varchar(50) DEFAULT NULL COMMENT '业主单位',
  `begin_time` datetime NOT NULL COMMENT '项目开始时间',
  `end_time` datetime NOT NULL COMMENT '项目结束时间',
  `leader_id` bigint(20) NOT NULL COMMENT '项目负责人id',
  `disinfection_area` varchar(255) DEFAULT NULL COMMENT '项目面积',
  `disinfection_location_name` varchar(100) DEFAULT NULL COMMENT '项目所在地名称',
  `disinfection_location` varchar(500) NOT NULL COMMENT '项目所在地经纬度',
  `disinfection_scope` text NOT NULL COMMENT '项目消杀范围经纬度，多个',
  `description` varchar(1000) DEFAULT NULL COMMENT '项目说明',
  `status` varchar(20) NOT NULL COMMENT '项目状态',
  `deleted` bigint(20) NOT NULL,
  `tenant` int(11) NOT NULL,
  `create_by` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_by` varchar(100) NOT NULL,
  `update_time` datetime NOT NULL,
  `image` bigint(20) DEFAULT NULL COMMENT '项目图片',
  `district` varchar(50) DEFAULT NULL COMMENT '地址所在区',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀项目表';
-- changeset ht:ddl-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_task') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_task
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_task` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `plan_id` bigint(20) DEFAULT NULL COMMENT '所属计划ID(通过计划创建就有)',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `project_id` bigint(20) NOT NULL COMMENT '所属项目',
  `task_type_id` bigint(20) NOT NULL COMMENT '任务类型',
  `task_type_name` varchar(100) DEFAULT NULL COMMENT '任务类型名称',
  `task_time_limit_config` varchar(100) DEFAULT NULL COMMENT '任务时限配置json（为空表示永不截至）',
  `project_leader_id` bigint(20) DEFAULT NULL COMMENT '项目负责人id',
  `disinfection_frequency` varchar(100) DEFAULT NULL COMMENT '消杀频次',
  `notification_type` varchar(50) NOT NULL COMMENT '通知类型',
  `notification_group_id` bigint(20) DEFAULT NULL COMMENT '通知的小组id',
  `notification_dept_id` bigint(20) DEFAULT NULL COMMENT '通知的部门id',
  `notification_post_id` bigint(20) DEFAULT NULL COMMENT '通知的岗位id',
  `notification_role_id` bigint(20) DEFAULT NULL COMMENT '通知的角色id',
  `task_area_id` bigint(20) NOT NULL COMMENT '消杀区域id',
  `appendix` varchar(255) DEFAULT NULL COMMENT '附件（保存多个的文件id）',
  `executor_id` bigint(20) DEFAULT NULL COMMENT '项目执行人Id',
  `executor_group_id` bigint(20) DEFAULT NULL COMMENT '执行人所属小组id',
  `deadline` datetime DEFAULT NULL COMMENT '任务截至日期',
  `process` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '任务进度',
  `status` varchar(20) NOT NULL COMMENT '任务状态',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀任务表';
-- changeset ht:ddl-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_task_check_in_record') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_task_check_in_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_task_check_in_record` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `longitude` decimal(10,2) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,2) DEFAULT NULL COMMENT '纬度',
  `files` varchar(200) DEFAULT NULL COMMENT '打卡相关图片',
  `location_name` varchar(200) DEFAULT NULL COMMENT '打卡地点名称',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务签到记录';
-- changeset ht:ddl-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_task_execute_record') AS column_exists;
-- ----------------------------
-- Table structure for disinfection_task_execute_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `disinfection_task_execute_record` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `executor_id` bigint(20) NOT NULL COMMENT '执行者id',
  `process` decimal(10,2) NOT NULL COMMENT '任务进度',
  `task_desc` varchar(500) DEFAULT NULL COMMENT '任务说明',
  `appendix` varchar(500) DEFAULT NULL COMMENT '任务上传的附件',
  `key_place_record` text COMMENT '重点场所消杀记录（json）',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) NOT NULL COMMENT '0为未删除',
  `update_by` varchar(100) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消杀任务执行记录';

