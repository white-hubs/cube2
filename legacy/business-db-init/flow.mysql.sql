USE `cube-base-flow`;
-- `cube-base-flow`.all_process_instance source 初始化flow视图
create or replace
    algorithm = UNDEFINED view `cube-base-flow`.`all_process_instance` as
select
    floor((rand() * 100)) as `id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.startUserId')) as `start_user_id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.startUserName')) as `start_user_name`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.organizationId')) as `organization_id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.organizationName')) as `organization_name`,
    'ACTIVE' as `state`,
    `r`.`tenant` as `tenant`,
    `t`.`PROC_INST_ID_` as `proc_inst_id`,
    `t`.`ASSIGNEE_` as `assignee`,
    `t`.`CREATE_TIME_` as `task_create_time`,
    null as `task_end_time`,
    `r`.`instance_name` as `apply_name`,
    `r`.`start_user_info` as `start_user_info`,
    `r`.`application_code` as `business_model`,
    `r`.`business_type` as `business_type`,
    `e`.`START_TIME_` as `start_time`,
    null as `end_time`,
    'APPROVAL' as `type`,
    '' as `copy_user`
from
    ((`cube-base-flow`.`act_ru_task` `t`
        left join `cube-base-flow`.`process_instance_running` `r` on
        ((`r`.`id` = `t`.`PROC_INST_ID_`)))
        left join `cube-base-flow`.`act_hi_procinst` `e` on
        ((`e`.`PROC_INST_ID_` = `t`.`PROC_INST_ID_`)))
union
select
    floor((rand() * 100)) as `id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.startUserId')) as `start_user_id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.startUserName')) as `start_user_name`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.organizationId')) as `organization_id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.organizationName')) as `organization_name`,
    ifnull(`r`.`result`, 'ACTIVE') as `state`,
    `r`.`tenant` as `tenant`,
    `p`.`PROC_INST_ID_` as `proc_inst_id`,
    `t`.`ASSIGNEE_` as `assignee`,
    null as `task_create_time`,
    `t`.`END_TIME_` as `task_end_time`,
    `r`.`instance_name` as `apply_name`,
    `r`.`start_user_info` as `start_user_info`,
    `r`.`application_code` as `business_model`,
    `r`.`business_type` as `business_type`,
    `p`.`START_TIME_` as `start_time`,
    `p`.`END_TIME_` as `end_time`,
    'APPROVAL' as `type`,
    '' as `copy_user`
from
    ((`cube-base-flow`.`act_hi_procinst` `p`
        left join `cube-base-flow`.`process_instance_running` `r` on
        ((`r`.`id` = `p`.`PROC_INST_ID_`)))
        left join `cube-base-flow`.`act_hi_taskinst` `t` on
        (((`t`.`PROC_INST_ID_` = `p`.`PROC_INST_ID_`)
            and (`t`.`TASK_DEF_KEY_` <> 'root')
            and (`t`.`END_TIME_` is not null)
            and ((`t`.`DELETE_REASON_` <> 'Multi-instance complete condition expression passed')
                or isnull(`t`.`DELETE_REASON_`)))))
union
select
    floor((rand() * 100)) as `id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.startUserId')) as `start_user_id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.startUserName')) as `start_user_name`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.organizationId')) as `organization_id`,
    json_unquote(json_extract(`r`.`start_user_info`, '$.organizationName')) as `organization_name`,
    ifnull(`r`.`result`, 'ACTIVE') as `state`,
    `r`.`tenant` as `tenant`,
    `t`.`process_instance_id` as `proc_inst_id`,
    `t`.`user_ids` as `assignee`,
    null as `task_create_time`,
    null as `task_end_time`,
    `r`.`instance_name` as `apply_name`,
    `r`.`start_user_info` as `start_user_info`,
    `r`.`application_code` as `business_model`,
    `r`.`business_type` as `business_type`,
    `p`.`START_TIME_` as `start_time`,
    `p`.`END_TIME_` as `end_time`,
    'COPY' as `type`,
    `t`.`user_ids` as `copy_user`
from
    ((`cube-base-flow`.`copy_log` `t`
        left join `cube-base-flow`.`process_instance_running` `r` on
        ((`r`.`id` = `t`.`process_instance_id`)))
        left join `cube-base-flow`.`act_hi_procinst` `p` on
        ((`p`.`PROC_INST_ID_` = `t`.`process_instance_id`)));

