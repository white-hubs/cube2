-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_plan' AND column_name='user_id') AS column_exists;
ALTER TABLE patrol_plan MODIFY COLUMN user_id text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '巡更人员';

-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_task_location' AND column_name='superintendent_user_id') AS column_exists;
-- 任务的巡更点
UPDATE patrol_task_location AS ptl
JOIN patrol_task AS pt
  ON ptl.task_id = pt.id
SET
  ptl.superintendent_user_id = pt.user_id
WHERE
  pt.status = 'IN_EXECUTION'
  and pt.user_id is not null
  and ptl.superintendent_user_id is null
  AND pt.deleted = 0
  AND ptl.deleted = 0;

-- changeset chx:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_plan' AND column_name='condition_type') AS column_exists;
-- 计划
UPDATE patrol_plan
SET
    exception_user_ids = TRIM(SUBSTRING_INDEX(user_id, ',', 1)),
    condition_type = 'USER'
WHERE
    condition_type is null
    and user_id IS NOT NULL
    AND user_id != ''
    AND deleted = 0;

-- changeset chx:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_plan_superintendent') AS column_exists;
-- 计划负责人
INSERT INTO patrol_plan_superintendent (
    id,
    master_id,
    superintendent_type,
    target_id,
    create_by,
    update_by,
    tenant,
    create_time,
    update_time,
    deleted
)
SELECT
    -- 生成自增 ID（从 1 开始）
    @row_number := @row_number + 1 AS id,
    p.id AS master_id,
    'USER' AS superintendent_type,
    CAST(
        TRIM(
            SUBSTRING_INDEX(
                SUBSTRING_INDEX(p.user_id, ',', nums.n),
                ',',
                -1
            )
        ) AS UNSIGNED
    ) AS target_id,
    '1' AS create_by,
    '1' AS update_by,
    p.tenant AS tenant,
    NOW() AS create_time,
    NOW() AS update_time,
    0 AS deleted
FROM patrol_plan p
-- 构造数字序列 1 到 50
JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
    UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
    UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15
    UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
    UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25
    UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30
    UNION SELECT 31 UNION SELECT 32 UNION SELECT 33 UNION SELECT 34 UNION SELECT 35
    UNION SELECT 36 UNION SELECT 37 UNION SELECT 38 UNION SELECT 39 UNION SELECT 40
    UNION SELECT 41 UNION SELECT 42 UNION SELECT 43 UNION SELECT 44 UNION SELECT 45
    UNION SELECT 46 UNION SELECT 47 UNION SELECT 48 UNION SELECT 49 UNION SELECT 50
) AS nums
ON nums.n <= (CHAR_LENGTH(p.user_id) - CHAR_LENGTH(REPLACE(p.user_id, ',', '')) + 1)
CROSS JOIN (SELECT @row_number := 0) AS r
WHERE
  p.user_id IS NOT NULL
  AND p.user_id != ''
  AND p.deleted = 0;