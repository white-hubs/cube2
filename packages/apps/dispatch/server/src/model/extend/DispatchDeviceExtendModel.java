package com.guhecloud.cube.biz.dispatch.server.model.extend;

import com.guhecloud.cube.biz.dispatch.server.model.DispatchDeviceModel;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备-扩展信息
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchDeviceExtendModel extends DispatchDeviceModel {
    //是否录音
    private Boolean isRecord;
    //是否录像
    private Boolean isVideo;
    //设备级别
    private Integer deviceLevel;
    //备注
    private String remark;

}