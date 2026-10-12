package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class CreateTagCmd {
    /**
     * 内置标签
     * {@link com.guhecloud.cube.base.device.center.metadata.lang.Const#APP_INNER_TAG_TYPE_CODES}
     */
    private String tagTypeCode;
    /**
     * 使用设备中心提供的内置标签类型时，tagTypeName可不传<br/>
     *  TODO 需要删除掉
     */
    private String tagTypeName;

    /**
     * 父级标签
     * 如果不传表示一级标签
     */
    private String parentTagCode;
    private Integer level;

    @NotNull(message = "编号不能为空")
    private String tagCode;
    @NotNull(message = "名称不能为空")
    private String tagName;

    private String description;

    @Deprecated
    public CreateTagCmd() {}

    public static CreateTagCmd createTopTag(String tagTypeCode, String tagCode, String tagName) {
        CreateTagCmd createTagCmd = new CreateTagCmd();
        createTagCmd.tagCode = tagCode;
        createTagCmd.tagTypeCode = tagTypeCode;
        createTagCmd.tagName = tagName;
        createTagCmd.level = 1;
        return createTagCmd;
    }

}
