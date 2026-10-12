package com.guhecloud.cube.biz.todo.dcm.support;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
public class TodoDcmSupport {
    public static final String PROVIDER_TODO = "TODO";
    public static final String VERSION = "1.0";
    public static final String CONFIG_TYPE_TODO_TAG = "TodoTag";
    public static final String CONFIG_TYPE_TODO_TAG_REF_BUSINESS_TYPE = "TodoTagReferBusinessType";
    public static final String CONFIG_TYPE_TODO_BUSINESS_URL = "TodoBusinessUrl";

    /**
     * 以下为集中定义的Rci kw args key, 以消除无意引入的差异.
     */
    public static final String KW_KEY_TODO_TAG_CODE = "todoTag";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = asList(
            CONFIG_TYPE_TODO_TAG,
            CONFIG_TYPE_TODO_TAG_REF_BUSINESS_TYPE,
            CONFIG_TYPE_TODO_BUSINESS_URL
                                                                  );

    private TodoDcmSupport() {
        // disable instantiation
    }
}
