USE `cube-base-dictionary`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

INSERT IGNORE INTO `dictionary_item`(`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `change_notice`) VALUES (1848276221907501058, 'SUBSYSTEM_ENDPOINT_CONFIG', '子系统端点配置key', 0, '17311369782', '2024-10-21 16:12:45', '17311369782', '2024-10-21 16:12:45', 'ROCKET_MQ,REDIS');
INSERT IGNORE INTO `dictionary_item`(`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `change_notice`) VALUES (1848276329671266306, 'SUBSYSTEM_ADAPTER_CONFIG', '子系统适配配置key', 0, '17311369782', '2024-10-21 16:13:11', '17311369782', '2024-10-21 16:13:11', 'ROCKET_MQ,REDIS');
