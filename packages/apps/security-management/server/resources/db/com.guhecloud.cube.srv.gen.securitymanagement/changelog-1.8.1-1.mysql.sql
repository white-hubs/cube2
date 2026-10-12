-- liquibase formatted sql

-- changeset xiaofeng:ddl-1
-- ----------------------------
-- Table structure for program_schedule
-- ----------------------------
CREATE TABLE IF NOT EXISTS `program_schedule` (
                                  id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                  client_id VARCHAR(100) DEFAULT NULL COMMENT '终端id',
                                  client_name VARCHAR(100) DEFAULT NULL COMMENT '终端名称',
                                  third_party_id VARCHAR(100) DEFAULT NULL COMMENT '第三方节目单id，和发送id是同一个概念',
                                  third_program_id VARCHAR(100) DEFAULT NULL COMMENT '第三方节目id',
                                  `name` VARCHAR(100) DEFAULT NULL COMMENT '节目名称',
                                  begin_date datetime DEFAULT NULL COMMENT '开始日期',
                                  end_date datetime DEFAULT NULL COMMENT '结束日期',
                                  begin_time time DEFAULT NULL COMMENT '开始时间',
                                  end_time time DEFAULT NULL COMMENT '结束时间',
                                  weeks VARCHAR(30) DEFAULT NULL COMMENT '有效星期 1-6分别表示星期一至星期六 ，0:表示星期天',
                                  priority VARCHAR(10) DEFAULT NULL COMMENT '优先级 0普通 2插播',
                                  `state` VARCHAR(10) DEFAULT NULL COMMENT '发送状态0表示发送不存在,1:等待下载,2:下载中,3：下载失败,4：播放中,5:播放完成,6：等待播放,7:已取消',
                                  `type` VARCHAR(10) DEFAULT NULL COMMENT '节目单类型 normal插播节目、text插播文字，reserved预留节目',
                                  `deleted` bigint(20) DEFAULT NULL,
                                  `tenant` int(11) DEFAULT NULL,
                                  `create_by` varchar(255) DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  `update_by` varchar(255) DEFAULT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  CONSTRAINT idx_client_tenant UNIQUE (third_party_id, tenant)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节目单实体表';
