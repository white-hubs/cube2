package com.guhecloud.cube.biz.ouaa.metadata.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-06  09:51
 * @Description:
 * @Version: 1.0
 */
public enum DictionaryEnum {
    SERVICE_CONFIGURATION("手机号加密字典"),
    MEI_SHAN_URL_ADDRESS("环天智慧园区数字大脑字典项"),
    ACCESS_TOCKEN("环天智慧园区数字大脑code交换token"),
    LOGIN_OAUTH("环天智慧园区数字大脑获取登录用户信息"),
    OAUTH_TOCKEN("环天智慧园区数字大脑获取token"),
    USER_GET_PAGE("环天智慧园区数字大脑分页获取用户信息"),
    USER_GET_ALL("环天智慧园区数字大脑分页获取用户信息"),
    ORGANIZATION_GET_ALL("环天智慧园区数字大脑分页获取部门"),

    ;
    private String remarks;

    DictionaryEnum(String remarks) {
        this.remarks = remarks;
    }
}
