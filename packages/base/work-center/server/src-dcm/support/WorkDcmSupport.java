package com.guhecloud.cube.biz.work.center.dcm.support;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
public class WorkDcmSupport {
    public static final String PROVIDER_WORK = "WORK_CENTER";
    public static final String VERSION = "1.0";
    public static final String CONFIG_TYPE_WORK_TAG = "WorkTag";
    public static final String CONFIG_TYPE_WORK_TAG_REF_BUSINESS_TYPE = "WorkTagReferBusinessType";
    public static final String CONFIG_TYPE_WORK_BUSINESS_URL = "WorkBusinessUrl";

    /**
     * 以下为集中定义的Rci kw args key, 以消除无意引入的差异.
     */
    public static final String KW_KEY_WORK_TAG_CODE = "workTag";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = asList(
            CONFIG_TYPE_WORK_TAG,
            CONFIG_TYPE_WORK_TAG_REF_BUSINESS_TYPE,
            CONFIG_TYPE_WORK_BUSINESS_URL
                                                                  );

    private WorkDcmSupport() {
        // disable instantiation
    }
}
