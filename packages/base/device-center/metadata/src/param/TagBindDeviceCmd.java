package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.Set;

@Data
public class TagBindDeviceCmd {
    private String tagCode;

    private Set<Long> businessKeys;

    public static TagBindDeviceCmd of(String tagCode, Set<Long> businessKeys) {
        TagBindDeviceCmd param = new TagBindDeviceCmd();
        param.setTagCode(tagCode);
        param.setBusinessKeys(businessKeys);
        return param;
    }
}
