ALTER VIEW `all_process_instance` as
    SELECT
        floor((
                rand() * 100
            )) AS `id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.startUserId' )) AS `start_user_id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.startUserName' )) AS `start_user_name`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.organizationId' )) AS `organization_id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.organizationName' )) AS `organization_name`,
        'ACTIVE' AS `state`,
        `r`.`tenant` AS `tenant`,
        `t`.`PROC_INST_ID_` AS `proc_inst_id`,
        `t`.`ASSIGNEE_` AS `assignee`,
        `r`.`instance_name` AS `apply_name`,
        `r`.`start_user_info` AS `start_user_info`,
        `r`.`application_code` AS `business_model`,
        `r`.`business_type` AS `business_type`,
        `e`.`START_TIME_` AS `start_time`,
        NULL AS `end_time`,
        'APPROVAL' AS `type`,
        '' AS `copy_user`
    FROM
        `act_ru_task` `t`
            LEFT JOIN `process_instance_running` `r` ON `r`.`id` = `t`.`PROC_INST_ID_`
            LEFT JOIN `act_ru_execution` `e` ON `e`.`PROC_INST_ID_` = `t`.`PROC_INST_ID_`
    WHERE
        isnull( `e`.`PARENT_ID_` )
    UNION
    SELECT
        floor((
                rand() * 100
            )) AS `id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.startUserId' )) AS `start_user_id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.startUserName' )) AS `start_user_name`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.organizationId' )) AS `organization_id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.organizationName' )) AS `organization_name`,
        `r`.`result` AS `state`,
        `r`.`tenant` AS `tenant`,
        `p`.`PROC_INST_ID_` AS `proc_inst_id`,
        `t`.`ASSIGNEE_` AS `assignee`,
        `r`.`instance_name` AS `apply_name`,
        `r`.`start_user_info` AS `start_user_info`,
        `r`.`application_code` AS `business_model`,
        `r`.`business_type` AS `business_type`,
        `p`.`START_TIME_` AS `start_time`,
        `p`.`END_TIME_` AS `end_time`,
        'APPROVAL' AS `type`,
        '' AS `copy_user`
    FROM
        `act_hi_procinst` `p`
            LEFT JOIN `process_instance_running` `r` ON `r`.`id` = `p`.`PROC_INST_ID_`
            LEFT JOIN `act_hi_taskinst` `t` ON `t`.`PROC_INST_ID_` = `p`.`PROC_INST_ID_` AND ( `t`.`TASK_DEF_KEY_` <> 'root' )
            AND ( `t`.`END_TIME_` IS NOT NULL )
            AND ((`t`.`DELETE_REASON_` <> 'Multi-instance complete condition expression passed' ) OR isnull( `t`.`DELETE_REASON_` ))
    UNION
    SELECT
        floor((
                rand() * 100
            )) AS `id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.startUserId' )) AS `start_user_id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.startUserName' )) AS `start_user_name`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.organizationId' )) AS `organization_id`,
        json_unquote(
                json_extract( `r`.`start_user_info`, '$.organizationName' )) AS `organization_name`,
        `r`.`result` AS `state`,
        `r`.`tenant` AS `tenant`,
        `t`.`process_instance_id` AS `proc_inst_id`,
        `t`.`user_ids` AS `assignee`,
        `r`.`instance_name` AS `apply_name`,
        `r`.`start_user_info` AS `start_user_info`,
        `r`.`application_code` AS `business_model`,
        `r`.`business_type` AS `business_type`,
        `p`.`START_TIME_` AS `start_time`,
        `p`.`END_TIME_` AS `end_time`,
        'COPY' AS `type`,
        `t`.`user_ids` AS `copy_user`
    FROM
        `copy_log` `t`
            LEFT JOIN `process_instance_running` `r` ON `r`.`id` = `t`.`process_instance_id`
            LEFT JOIN `act_hi_procinst` `p` ON 	`p`.`PROC_INST_ID_` = `t`.`process_instance_id`;