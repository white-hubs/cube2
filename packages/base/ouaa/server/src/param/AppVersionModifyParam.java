package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.AppTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * app版本新增参数
 *
 * @author lanchun
 * @date 2022/8/15 15:16
 */
@Data
public class AppVersionModifyParam {

    /**
     * 版本名称
     */
    private String name;

    /**
     * 终端编号
     */
    @NotEmpty(message = "终端编号不能为空")
    @Size(max = 100, message = "终端编号不能大于100")
    private String terminal;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private AppTypeEnum type;

    /**
     * 版本
     */
    @NotNull(message = "版本不能为空")
    private Integer version;

    /**
     * 版本序号
     */
    private Integer sort;

    /**
     * 是否强制更新
     */
    @NotNull(message = "是否强制更新不能为空")
    private Boolean forceUpdated;

    /**
     * 版本备注
     */
    @Size(max = 200, message = "备注不能大于200")
    private String remark;

    /**
     * 附件主键
     */
    @NotNull(message = "附件主键不能为空")
    private Long fileId;
}
