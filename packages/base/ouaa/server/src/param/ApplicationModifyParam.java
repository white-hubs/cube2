package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ApplicationModifyParam {

    /**
     * 应用分类主键
     */
    @NotNull(message = "应用分类主键不能为空")
    @Min(value = 1, message = "应用分类主键不能小于1")
    private Long applicationGroupId;

    /**
     * 应用编号
     */
    @NotEmpty(message = "编号不能为空")
    @Size(max = 30, message = "编号长度不能大于30")
    private String code;

    /**
     * 应用名称
     */
    @NotEmpty(message = "应用名称不能为空")
    @Size(max = 100, message = "应用名称长度不能大于100")
    private String name;

    /**
     * s
     * 应用图标
     */
    @NotEmpty(message = "应用图标不能为空")
    @Size(max = 100, message = "应用图标长度不能大于100")
    private String icon;

    /**
     * 依赖应用集合
     */
    private String dependAppIds;

    /**
     * 应用端
     */
    private List<Long> terminalIdList;
    /**
     * 应用备注
     */
    private String remark;
    /**
     * 最新版本
     */
    private String version;
    /**
     * 是否展示
     */
    private Boolean enabled = true;

    /**
     * 是否公开访问
     */
    private Boolean publicAccess = true;

    /**
     * 浏览次数
     */
    private Integer viewCount = 0;

    /**
     * 扩展信息
     */
    private ApplicationExtendInfo extendParam;
}
