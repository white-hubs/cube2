package com.guhecloud.cube.base.device.center.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOrUpdateDeviceDTO {
    //base info
    private String id;
    private String deviceName;
    private String deviceCode;
    private String departmentId;
    private String departmentName;//回显提供
    private String areaIds;//区域ID,-分割
    private List<String> areaIdList;//区域ID
    private List<String> areaNameList;//区域名称
    private String managerIds;//管理人员ID,-分割
    private List<String> managerIdList;//管理人员ID
    private List<String> managerNameList;//管理人员名称
    private String thirdPartyId;//第三方设备编码
    private String thirdpartyCfg;//第三方设备类型

    //extra
    private Integer usefulLife;
    private Integer dailyWorkTime;
    private String lntAndLat;
    private String unitPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDate purchaseDate;
    //安装图片
    private Long picture;
    private List<FileVO> pictureList;

    //iot info
    private String productId;
}