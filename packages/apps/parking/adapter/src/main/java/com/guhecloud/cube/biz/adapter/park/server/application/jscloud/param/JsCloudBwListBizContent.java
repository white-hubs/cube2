package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 黑白灰名单接口 biz_content 内容，严格对应文档
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudBwListBizContent {

    private String parkCode;
    private String carNo;

    @JsonProperty("carNoColor")
    private String carNoColor;

    private Integer type;
    private String beginTime;
    private String endTime;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    private String remark;
}
