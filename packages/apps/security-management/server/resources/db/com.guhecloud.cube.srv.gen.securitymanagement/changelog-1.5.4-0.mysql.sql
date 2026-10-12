-- liquibase formatted sql

-- changeset xiaofeng:ddl-1
-- ----------------------------
-- Table structure for meeting_apply
-- ----------------------------
CREATE TABLE IF NOT EXISTS `meeting_apply` (
    id bigint(20) NOT NULL,
    apply_code VARCHAR(30) DEFAULT NULL COMMENT '会见申请记录编号',
    apply_time DATETIME DEFAULT NULL COMMENT '申请时间，填写记录的时间，以三方返回为准',
    apply_person_id VARCHAR(100) DEFAULT NULL COMMENT '申请人id',
    apply_person_name VARCHAR(100) DEFAULT NULL COMMENT '申请人名称，填写记录的人，以三方返回为准',
    meeting_type VARCHAR(100) DEFAULT NULL COMMENT '会见类型，哪种类型比如：亲友会见、法律咨询、工作事宜等等，以三方返回为准',
    meeting_type_name VARCHAR(100) DEFAULT NULL COMMENT '会见类型名称',
    meeting_reason VARCHAR(2000) DEFAULT NULL COMMENT '会见事由，会见的原因，以三方返回为准',
    meeting_start_time DATETIME DEFAULT NULL COMMENT '会见时间，预约见面的开始时间，以三方返回为准 yyyy-MM-dd HH:mm:ss',
    meeting_end_time DATETIME DEFAULT NULL COMMENT '会见时间，预约见面的结束时间，以三方返回为准 yyyy-MM-dd HH:mm:ss',
    approval_result VARCHAR(255) DEFAULT NULL COMMENT '审批结果，申请是通过，还是不通过，不通过原因是什么，以三方返回为准',
    approval_result_reason VARCHAR(255) DEFAULT NULL COMMENT '审批结果说明',

    third_record_id VARCHAR(100) DEFAULT NULL COMMENT '第三方记录id 申请记录号',
    `deleted` bigint(20) DEFAULT NULL,
    `tenant` int(11) DEFAULT NULL,
    `create_by` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(255) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    CONSTRAINT idx_client_tenant UNIQUE (id, tenant),
    CONSTRAINT third_record_id_client UNIQUE (`third_record_id`,`id`,`tenant`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会见申请记录表';

-- changeset xiaofeng:ddl-2
-- ----------------------------
-- Table structure for meeting_apply_person
-- ----------------------------
CREATE TABLE IF NOT EXISTS `meeting_apply_person` (
    id bigint(20) NOT NULL,
    business_key bigint(20) NOT NULL COMMENT '会见申请记录id/会见记录id',
    business_code VARCHAR(30) NOT NULL COMMENT '会见申请记录id/会见记录id',
    business_type int(11) NOT NULL COMMENT '关联业务code 1:会见申请记录、2:会见记录',
    meeting_person_id VARCHAR(100) DEFAULT NULL COMMENT '第三方会见人id',
    meeting_person_type VARCHAR(10) DEFAULT NULL COMMENT '会见人类型',
    meeting_person_type_name VARCHAR(100) DEFAULT NULL COMMENT '会见人类型名称',
    meeting_person_name VARCHAR(100) DEFAULT NULL COMMENT '会见人名称',
    meeting_person_code VARCHAR(100) DEFAULT NULL COMMENT '会见人编码',
    meeting_person_document_type VARCHAR(10) DEFAULT NULL COMMENT '会见人证件类型',
    meeting_person_document_type_name VARCHAR(100) DEFAULT NULL COMMENT '会见人证件类型名称',
    meeting_person_document_number VARCHAR(100) DEFAULT NULL COMMENT '会见人证件号码',
    `deleted` bigint(20) DEFAULT NULL,
    `tenant` int(11) DEFAULT NULL,
    `create_by` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(255) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    CONSTRAINT idx_client_tenant UNIQUE (id, tenant),
    CONSTRAINT third_record_id_client UNIQUE (`id`,`business_key`,`meeting_person_id`,`tenant`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会见人员表';

-- changeset xiaofeng:ddl-3
-- ----------------------------
-- Table structure for meeting
-- ----------------------------
CREATE TABLE IF NOT EXISTS `meeting` (
    id bigint(20) NOT NULL,
    meeting_code VARCHAR(30) DEFAULT NULL COMMENT '会见记录号',
    meeting_type VARCHAR(100) DEFAULT NULL COMMENT '会见类型，哪种类型比如：亲友会见、法律咨询、工作事宜等等，以三方返回为准',
    meeting_type_name VARCHAR(100) DEFAULT NULL COMMENT '会见类型名称',
    meeting_reason VARCHAR(2000) DEFAULT NULL COMMENT '会见事由，会见的原因，以三方返回为准',
    meeting_start_time DATETIME DEFAULT NULL COMMENT '会见时间，预约见面的开始时间，以三方返回为准 yyyy-MM-dd HH:mm:ss',
    meeting_end_time DATETIME DEFAULT NULL COMMENT '会见时间，预约见面的结束时间，以三方返回为准 yyyy-MM-dd HH:mm:ss',
    meeting_process_audio VARCHAR(255) DEFAULT NULL COMMENT'会见过程录音 url',

    third_record_id VARCHAR(100) DEFAULT NULL COMMENT '第三方记录id',
    `deleted` bigint(20) DEFAULT NULL,
    `tenant` int(11) DEFAULT NULL,
    `create_by` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(255) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    CONSTRAINT idx_client_tenant UNIQUE (id, tenant),
    CONSTRAINT third_record_id_client UNIQUE (`third_record_id`,`id`,`tenant`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会见记录表';

-- changeset xiaofeng:ddl-4
-- ----------------------------
-- Table structure for person_roll_call_task
-- ----------------------------
CREATE TABLE IF NOT EXISTS `person_roll_call_task` (
    id bigint(20) NOT NULL,
    `task_code` VARCHAR(255) DEFAULT NULL COMMENT '点名任务编号',
    `task_name` VARCHAR(255) DEFAULT NULL COMMENT '点名任务名称',
    `location_name` VARCHAR(255) DEFAULT NULL COMMENT '场所名称',
    `call_type` VARCHAR(30) DEFAULT NULL COMMENT '点名类别',
    `call_start_time` DATETIME DEFAULT NULL COMMENT '点名开始时间',
    `call_end_time` DATETIME DEFAULT NULL COMMENT '点名结束时间',
    `status` int(5) DEFAULT NULL COMMENT '状态（0已结束，1执行中，2待执行）',
    `attended_count` int(11) DEFAULT NULL COMMENT '已点到人数',
    `unattended_count` int(11) DEFAULT NULL COMMENT '未点到人数',
    `special_count` int(11) DEFAULT NULL COMMENT '特殊人数',
    `duration` int(10) DEFAULT NULL COMMENT '点名时长',

    third_record_id VARCHAR(100) DEFAULT NULL COMMENT '第三方记录id 任务记录id',
    `deleted` bigint(20) DEFAULT NULL,
    `tenant` int(11) DEFAULT NULL,
    `create_by` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(255) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    CONSTRAINT idx_client_tenant UNIQUE (id, tenant),
    CONSTRAINT third_record_id_client UNIQUE (`third_record_id`,`id`,`tenant`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点名任务记录表';

-- changeset xiaofeng:ddl-5
-- ----------------------------
-- Table structure for person_roll_call
-- ----------------------------
CREATE TABLE IF NOT EXISTS `person_roll_call` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `record_code` VARCHAR(255) DEFAULT NULL COMMENT '点名任务记录编号',
    `person_name` VARCHAR(255) DEFAULT NULL COMMENT '人员姓名',
    `index_code` VARCHAR(255) DEFAULT NULL COMMENT '编号',
    `face_photo` VARCHAR(1000) DEFAULT NULL COMMENT '照片',
    `capture_photo` VARCHAR(1000) DEFAULT NULL COMMENT '抓拍照片',
    `device_code` VARCHAR(255) DEFAULT NULL COMMENT '点名设备编号',
    `device_name` VARCHAR(255) DEFAULT NULL COMMENT '点名设备名称',
    `task_type` VARCHAR(50) DEFAULT NULL COMMENT '任务类型',
    `similarity` FLOAT DEFAULT NULL COMMENT '人脸比对相似度',
    `status` INT(11) DEFAULT NULL COMMENT '状态：0-点到，1-特殊情况',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '特殊情况说明',
    `rollcall_time` DATETIME DEFAULT NULL COMMENT '点到时间',
    `area_index_code` VARCHAR(255) DEFAULT NULL COMMENT '点名对象所在监区编号',
    `department_index_code` VARCHAR(255) DEFAULT NULL COMMENT '部门索引代码',
    `department_name` VARCHAR(255) DEFAULT NULL COMMENT '部门名称',
    `area_name` VARCHAR(255) DEFAULT NULL COMMENT '场所名称',

    third_record_id VARCHAR(100) DEFAULT NULL COMMENT '第三方主键id 任务记录id',
    `deleted` bigint(20) DEFAULT NULL,
    `tenant` int(11) DEFAULT NULL,
    `create_by` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(255) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    CONSTRAINT idx_client_tenant UNIQUE (id, tenant),
    CONSTRAINT third_record_id_client UNIQUE (`third_record_id`,`id`,`tenant`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员点到明细表';
