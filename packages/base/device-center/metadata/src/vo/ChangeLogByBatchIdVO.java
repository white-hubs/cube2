package com.guhecloud.cube.base.device.center.metadata.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.device.center.metadata.lang.AttrCtlExecuteResultEnum;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DevicePropTranslateVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/7/12 17:48
 * @description
 */
@Data
public class ChangeLogByBatchIdVO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    private String attributeName;
    private DevicePropTranslateVO originalValue;
    private DevicePropTranslateVO expectedValue;
    private AttrCtlExecuteResultEnum step;
}
