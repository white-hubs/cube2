package com.guhecloud.cube.base.device.center.metadata.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.device.center.metadata.lang.AttrCtlExecuteResultEnum;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DevicePropTranslateVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/7/12 16:08
 * @description
 */
@Data
public class DeviceInstanceExecuteCommandVO {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    private String code;
    private String name;
    private String attributeName;
    private DevicePropTranslateVO originalValue;
    private DevicePropTranslateVO expectedValue;
    private AttrCtlExecuteResultEnum executeResult;
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
}
