package com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2023/11/1 14:21
 * @description
 */
@Data
public class SecurityDeviceInfoDTO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceId;
    private String deviceCode;
    private String deviceName;
    private String deviceType;
    private String deviceModel;
    private Boolean deviceStatus;
    private String areaId;
    private String areaName;

    private String brandName;

    private List<FileVO> images;

    /**
     * 产品图片
     */
    private FileVO picture;
    /**
     * 产品图标
     */
    private FileVO icon;

    private Boolean collection = false;

    private String LntAndLat;

    private String communicationStatus;

    private List<DevicePropWithTsDTO> devicePropWithTsDTOS;

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
