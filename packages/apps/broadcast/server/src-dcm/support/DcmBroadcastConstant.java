package com.guhecloud.cube.biz.subsystem.broadcast.dcm.support;

import java.util.List;

import static java.util.Arrays.asList;

public class DcmBroadcastConstant {
    public static final String PROVIDER_BROADCAST = "BROADCAST";
    public static final String CONFIG_TYPE_BROADCAST = "BroadcastTemplate";
    public static final String DCM_RESOURCE_PATH = "classpath*:dcm/dcm-declaration-broadcast.yml";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = asList(
            CONFIG_TYPE_BROADCAST
                                                                  );
}
