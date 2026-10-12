package com.guhecloud.cube.srv.gen.maintain.dcm.fault.support;

import java.util.List;

import static java.util.Arrays.asList;

public class DcmFaultConstant {
    public static final String PROVIDER_FAULT = "FAULT";
    public static final String CONFIG_TYPE_FAULT_TYPE = "FaultType";
    public static final String DCM_RESOURCE_PATH = "classpath*:dcm/dcm-declaration-fault.yml";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = asList(
            CONFIG_TYPE_FAULT_TYPE
                                                                  );
}
