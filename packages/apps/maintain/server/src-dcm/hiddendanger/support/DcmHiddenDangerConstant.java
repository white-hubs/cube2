package com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support;

import java.util.List;

import static java.util.Arrays.asList;

public class DcmHiddenDangerConstant {

    public static final String PROVIDER_HIDDEN_DANGER = "HIDDEN_DANGER";
    public static final String CONFIG_TYPE_HIDDEN_DANGER_CATEGORY = "HiddenDangerCategory";
    public static final String CONFIG_TYPE_HIDDEN_DANGER_TYPE = "HiddenDangerType";
    public static final String DCM_RESOURCE_PATH = "classpath*:dcm/dcm-declaration-hidden-danger.yml";
    /**
     * 以下为集中定义的Rci kw args key, 以消除无意引入的差异.
     */
    public static final String KW_KEY_HIDDEN_DANGER_CATEGORY = "hiddenDangerCategory";

    public static final String DEFAULT_HIDDEN_DANGER_CATEGORY = "未分类";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = asList(
            CONFIG_TYPE_HIDDEN_DANGER_CATEGORY, CONFIG_TYPE_HIDDEN_DANGER_TYPE
                                                                  );
}
