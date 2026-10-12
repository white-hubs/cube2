-- liquibase formatted sql
-- changeset wql:addTable-1
CREATE TABLE IF NOT EXISTS  `center_task` (
  `id` bigint(20) NOT NULL COMMENT '任务id',
  `worker_no` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '执行编号',
  `task_name` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务名称',
  `task_type` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务类型',
  `param` LONGTEXT COLLATE utf8mb4_bin COMMENT '入参',
  `task_state` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务状态',
  `remark` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `retry_num` int(10) DEFAULT '0' COMMENT '重试次数',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `result_detail` LONGTEXT COLLATE utf8mb4_bin COMMENT '结果明细',
  `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='物联任务';

-- changeset wql:addFiled-1
ALTER TABLE iot_dev_device ADD COLUMN `edge_dev_identify` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '边缘侧设备标识';