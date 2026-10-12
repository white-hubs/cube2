-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `transfer_files` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转存后的信息' AFTER `pic_paths`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` MODIFY COLUMN `pratique_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '检疫证号' AFTER `vehicle_plate`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pratique_register` MODIFY COLUMN `pratique_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '检疫证号' AFTER `pratique_tickets`;