package com.guhecloud.cube.base.device.center.metadata.std.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * 设备信息<br/>
 * 物联中心对外暴露的概念
 */
@Data
public class DeviceInfoVO {
    /**
     * 物联网设备ID
     */
    private Long id;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 归属部门
     */
    private Long departmentId;

    /**
     * 归属部门名称
     */
    private String departmentName;

    /**
     * 区域ID,-分割
     */
    private String areaId;

    /**
     * 区域ID名称,--分割
     */
    private String areaName;

    /**
     * 最末级的区域ID
     */
    private Long areaLastId;

    /**
     * 区域级联
     */
    private List<Long> areaIdList;

    /**
     * 0 离线 1在线
     */
    private Integer deviceState;

    /**
     * 管理人员ids,-分割
     */
    private String managerIds;

    /**
     * 管理人员名称,--分割
     */
    private String managerNames;

    /**
     * 管理人员ids,--分割
     */
    private List<Long> managerIdList;

    /**
     * 三方设备ID
     */
    private String thirdPartyId;

    /**
     * 三方设备配置
     */
    private String thirdpartyCfg;

    /**
     * 设备扩展信息
     */
    private DeviceExpandVO deviceExpandVO;

    /**
     * 物联网设备产品ID
     */
    private Long iotProductId;

    /**
     * 物联网设备产品名称
     */
    private String iotProductName;

    /**
     * 物联网设备型号名称
     */
    private String iotModelName;

    /**
     * 设备品牌名称
     */
    private String iotBrandName;

    /**
     * 产品图片主键
     */
    private Long iotProductPicture;

    private FileVO iotProductPictureFileVO;

    /**
     * 产品图标主键
     */
    private Long iotProductIcon;

    private FileVO iotProductIconFileVO;

    /**
     * 物联网设备产品品类名称
     */
    private String iotProductCategoryName;

    private String iotProductCategoryNameConcat;

    /**
     * 物联网设备产品品类CODE
     */
    private String iotProductCategoryCode;

    /**
     * 物联网设备产品品类CODE级联
     */
    private String iotProductCategoryCodeConcat;

    /**
     * 设备标签信息
     */
    private List<DeviceTagFullVO> deviceTagFullVOs;
}
