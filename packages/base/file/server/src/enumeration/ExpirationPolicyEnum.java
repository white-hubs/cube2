package com.guhecloud.cube.base.file.server.enumeration;

/**
 * @author lijie
 * @date 2023/5/17
 */
public enum ExpirationPolicyEnum {
    BASED_ON_TIME_EXPIRATION("基于时间过期"),
    BASED_ON_SIZE_EXPIRATION("基于文件大小"),
    BASED_ON_LEAST_RECENTLY_USED_EXPIRATION("基于最近使用次数最少");
    private String remark;

    ExpirationPolicyEnum(String remark) {
        this.remark = remark;
    }
}
