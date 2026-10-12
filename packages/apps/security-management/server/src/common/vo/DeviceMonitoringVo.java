package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

@Data
public class DeviceMonitoringVo {
    //设备id
    private Long deviceId;
    //设备
    private String deviceName;
    //设备编码
    private String code;
    //区域
    private String areaId;
    //区域
    private String areaName;

    //是否收藏
    private Boolean collection;

    //状态
    private String deviceStatus;

    //品牌
    private String brandName;

    //设备图片
    private FileVO picture;

    //图标
    private FileVO icon;

    private String communicationStatus;

    /**
     * 第三方设备id
     */
    private String thirdPartyId;
    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 产品品类编号序列号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

}
