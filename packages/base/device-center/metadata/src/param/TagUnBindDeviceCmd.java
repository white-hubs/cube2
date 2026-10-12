package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.Set;

@Data
public class TagUnBindDeviceCmd {
    /**
     * 内置标签
     * {@link com.guhecloud.cube.base.device.center.metadata.lang.Const#APP_INNER_TAG_TYPE_CODES}
     */
    private String tagTypeCode;
    /**
     * 使用设备中心提供的内置标签类型时，tagTypeNameOfOption可不传
     */
    private String tagTypeNameOfOption;

    private String tagCode;
    /**
     * 为空时，使用tagCode作为tagName
     */
    private String tagNameOfOption;

    private Set<Long> businessKeys;

    public static TagUnBindDeviceCmd of(String tagTypeCode, String tagCode, Set<Long> businessKeys) {
        TagUnBindDeviceCmd param = new TagUnBindDeviceCmd();
        param.setTagTypeCode(tagTypeCode);
        param.setTagCode(tagCode);
        param.setBusinessKeys(businessKeys);
        return param;
    }

    public static TagUnBindDeviceCmd of(String tagTypeCode, String tagCode, String tagNameOfOption, Set<Long> businessKeys) {
        TagUnBindDeviceCmd param = new TagUnBindDeviceCmd();
        param.setTagTypeCode(tagTypeCode);
        param.setTagCode(tagCode);
        param.setTagNameOfOption(tagNameOfOption);
        param.setBusinessKeys(businessKeys);
        return param;
    }
}
