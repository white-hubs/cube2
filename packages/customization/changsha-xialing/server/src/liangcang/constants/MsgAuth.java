package com.guhecloud.cube.customization.xialing.server.liangcang.constants;

/** 租户字典 / 三方 token 等提示。 */
public interface MsgAuth {

    String TENANT_DICT_BASE_URL_EMPTY = "租户字典中的三方 baseUrl 为空";
    String WAREHOUSE_TOKEN_BUSY = "获取智慧仓库 token 繁忙，请稍后重试";
    String WAREHOUSE_TOKEN_FAILED = "获取智慧仓库 token 失败，请检查租户字典中的账号密码与 baseUrl 配置";
    String WAREHOUSE_TOKEN_INTERRUPTED = "获取智慧仓库 token 被中断";
    String TENANT_ACCOUNT_PASSWORD_EMPTY = "租户字典中的三方账号或密码为空";
    String USER_LOGIN_NOT_SUCCESS_PREFIX = "userLogin 非成功响应: ";
    String USER_LOGIN_NO_TOKEN = "userLogin 未返回 token";
    String DICT_ITEM_CODE_NOT_CONFIGURED = "未配置租户字典 itemCode";
    String TENANT_DICT_WAREHOUSE_LOGIN_MISSING = "租户字典未配置智慧仓库登录信息";
    String TENANT_DICT_VARIABLE_PARSE_FAILED = "租户字典 variable 解析失败，请检查 JSON 结构";
    String TENANT_DICT_LIANGCANG_LOCATION_CARD_PARSE_FAILED = "租户字典中货位卡配置（variable）JSON 解析失败，请检查与 LiangcangLocationCardConfigDTO 字段是否一致";
}
