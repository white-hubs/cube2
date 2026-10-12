-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_route' AND column_name='number') AS column_exists;
ALTER TABLE `patrol_route` ADD COLUMN `number` int(11) NULL DEFAULT NULL COMMENT '巡更点数量';

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_plan' AND column_name='user_id') AS column_exists;
ALTER TABLE `patrol_plan` MODIFY COLUMN `user_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更人员';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_task' AND column_name='belonger') AS column_exists;
ALTER TABLE `patrol_task` MODIFY COLUMN `belonger` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务归属人ids';