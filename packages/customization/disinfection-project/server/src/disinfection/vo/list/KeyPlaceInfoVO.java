package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class KeyPlaceInfoVO {
    /**
     * Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 项目Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 场所名称
     */
    private String placeName;
    /**
     * 场所分类Id
     */
    private Long placeTypeId;
    /**
     * 场所分类名称
     */
    private String placeTypeName;
    /**
     * 场所地址
     */
    private String placeAddress;

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
     * 备注
     */
    private String placeDescription;

    /**
     * 场景类型图片
     */
    private FileVO placeTypeIcon;

    /**
     * 图片列表
     */
    private List<FileVO> files;

}