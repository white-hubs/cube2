-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `test001`  (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `dictionary_id` bigint(20) NOT NULL COMMENT '字典id',
                                 `superiors_ids` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所有父级id',
                                 `next_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所有子集id',
                                 `level` int(11) NOT NULL COMMENT '层级',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1674617912241598466 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


CREATE TABLE `test002`  (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `dictionary_id` bigint(20) NOT NULL COMMENT '字典id',
                                 `superiors_ids` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所有父级id',
                                 `next_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所有子集id',
                                 `level` int(11) NOT NULL COMMENT '层级',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1674617912241598466 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
