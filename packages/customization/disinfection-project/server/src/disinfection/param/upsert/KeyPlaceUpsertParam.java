package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KeyPlaceUpsertParam {

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 场所名称
     */
    @NotBlank(message = "场所名称不能为空")
    private String placeName;

    /**
     * 场所分类ID
     */
    @NotNull(message = "场所分类ID不能为空")
    private Long placeTypeId;

    /**
     * 场所分类名称
     */
    private String placeTypeName;

    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String region;

    /**
     * 场所地址
     */
    private String placeAddress;
    /**
     * 重点场所详细位置
     */
    private String placeDetail;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 场所图片，文件id，逗号分割
     */
    private String imgs;

    /**
     * b备注
     */
    private String placeDescription;

    /**
     * 场所图标
     */
    private Long icon;
}
