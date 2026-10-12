package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author appet
 * @Description 设备详情
 * @date 2022-07-04 15:12:36
 */
@Data
@Builder
public class MeterDeviceDetailVO {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String deviceCode;
    private String deviceAreaId;
    private String deviceArea;//设备区域名称
    private String modelName;//设备型号
    private String brandName;
    private Boolean deviceStatus;
    private BigDecimal initValue;//初始读数
    private BigDecimal readeValue;//当前读数
    //文件
    private List<FileVO> fileVOList;
}