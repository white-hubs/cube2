-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

UPDATE `cube-biz-ouaa`.`client`
SET `code` = 'client_2',
    `name` = '数字建筑',
    `remark` = NULL,
    `secret` = '{bcrypt}$2a$10$xEKX6LcP9UWRFpy/wii59eYjmP4V5B7uDd7WvfWip4dVjCqboScPW',
    `secret_plain` = NULL,
    `scopes` = 'select',
    `grant_types` = 'password,client_credentials,refresh_token,pwd,wechat,pwd_simple_code,exchange_token',
    `access_token_timeout` = 100000000,
    `refresh_token_timeout` = 100000000,
    `enabled` = 1,
    `tenant` = '0',
    `deleted` = 0,
    `create_by` = 'admin',
    `create_time` = '2022-05-18 16:13:12',
    `update_by` = 'admin',
    `update_time` = '2023-11-13 16:02:17'
    WHERE `id` = 4;
