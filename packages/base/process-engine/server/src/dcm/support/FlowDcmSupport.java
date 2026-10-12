package com.guhecloud.cube.base.flow.dcm.support;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-25  21:13
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class FlowDcmSupport {
    public static final String PROVIDER_FLOW = "FLOW";
    public static final String VERSION = "1.0";
    public static final String CONFIG_TYPE_FORM = "FormFlow";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = asList(
            CONFIG_TYPE_FORM
                                                                  );
}
