package com.guhecloud.cube.base.device.center.metadata.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceInstanceStatusEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class DeviceInstanceDetailVO {
    private Long id;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 品牌code
     */
    private String brandCode;

    private String brandName;

    /**
     * 物联网设备产品ID
     */
    private Long iotDeviceProductId;

    /**
     * 物联网设备产品名称
     */
    private String iotDeviceProductName;

    /**
     * 设备型号名称
     */
    private String iotDeviceModelName;

    /**
     * 设备型号编码
     */
    private String iotDeviceNumberCode;

    /**
     * 类型id
     */
    private Long genreId;

    private String genreName;
    //类型标识
    private String genreCode;

    /**
     * 采购日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime purchaseDate;

    /**
     * 采购单价
     */
    private BigDecimal purchasePrice;

    /**
     * 区域id
     */
    private String areaId;
    private String areaName;
    private Long areaLastId;
    private List<Long> areaIdList;
    private List<Pair<String, String>> areaListPairs;

    private String departmentId;
    private String departmentName;
    private List<Long> departmentIdList;

    /**
     * 安装单位
     */
    private String installUnit;

    /**
     * 安装电弧
     */
    private String installPhone;

    /**
     * 安装日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime installDate;

    /**
     * true 入账 false:不入账
     */
    //private Boolean credited;

    /**
     * 扩展信息
     */
    //private String expandInfo;

    /**
     * 关联的设备ID
     */
    private List<Long> associatedDeviceId;

    private String managerJson;

    /**
     * 设备管理员
     */
    private Set<Long> manager;
    private String managerUserName;
    /**
     * 使用寿命
     */
    private Integer usefulLife;

    /**
     * 每日开机时长
     */
    private Integer dailyWorkTime;

    private LocalDateTime createTime;

    //    private Integer faultThreshold;
    //    private Integer hiddenDangerThreshold;
    /**
     * 设备图片ID
     */
    private Long images;
    /**
     * 设备图片
     */
    private FileVO fileVO;
    /**
     * 经纬度
     */
    private String lntAndLat;
    /**
     * 第三方设备标识
     */
    private String thirdPartyId;
    /**
     * 三方设备配置
     */
    private String thirdpartyCfg;

    /**
     * 设备状态 0 离线 1在线
     */
    private int deviceState;

    /**
     * 租户号
     */
    private int tenant;
    /**
     * 设备状态
     */
    private DeviceInstanceStatusEnum deviceStatus;

    //通信状态,中文描述
    private String communicationStatus;
}
