package com.guhecloud.cube.srv.gen.loose.works.enumeration;

/**
 * @author wql
 * @describe 反馈操作类型
 * @date 2023/9/14 20:09
 */
public enum FeedbackTypeEnum {
    FEEDBACK("反馈"),
    CHECK_ACCEPT("验收");
    final String describe;

    FeedbackTypeEnum(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}
