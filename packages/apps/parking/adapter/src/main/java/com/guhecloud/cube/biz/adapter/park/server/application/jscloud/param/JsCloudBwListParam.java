package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 黑白灰名单 - 我方接口入参
 * 严格对应文档：车场编号、车牌号(带横杠如粤-B88888)、车牌颜色、类型(0黑1白2灰)、开始/结束时间、是否删除、备注
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudBwListParam {

    /**
     * 车场编号，必填
     */
    @NotBlank(message = "车场编号不能为空")
    private String parkCode;

    /**
     * 车牌号，必填。统一格式带横杠，例如 粤-B88888；车牌+颜色唯一
     */
    @NotBlank(message = "车牌号不能为空")
    private String carNo;

    /**
     * 车牌颜色，必填。参照公司标准；车牌+颜色唯一
     */
    @NotBlank(message = "车牌颜色不能为空")
    private String carNoColor;

    /**
     * 类型，必填。0-黑名单，1-白名单，2-灰名单
     */
    @NotNull(message = "类型不能为空")
    private Integer type;

    /**
     * 开始时间，必填。格式 yyyy-MM-dd HH:mm:ss
     */
    @NotBlank(message = "开始时间不能为空")
    private String beginTime;

    /**
     * 结束时间，必填。格式 yyyy-MM-dd HH:mm:ss
     */
    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    /**
     * 是否删除，必填。false-正常，true-删除
     */
    @NotNull(message = "是否删除不能为空")
    private Boolean isDeleted;

    /**
     * 备注，可选
     */
    private String remark;
}
