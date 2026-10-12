package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class KeyPlacePageVO {

    /**
     * 主键ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 项目ID
     */
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
     * 场所分类ID
     */
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
     * 场所图片
     */
    private String imgs;
    /**
     * 图片文件信息
     */
    private List<FileVO> files;
    /**
     * 场所说明
     */
    private String placeDescription;
}
