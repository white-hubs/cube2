package com.guhecloud.cube.biz.dispatch.server.vo.extend;

import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceDetailVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupVO;
import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 调度组-扩展
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchGroupExtendVO extends DispatchGroupVO {
    private Boolean isRecord;
    private Boolean isVideo;
    private Integer groupLevel;
    //对讲空闲超时
    private Integer intercomIdleTimeOut;
    //话权方总超时
    private Integer intercomPowerTimeCount;
    //排队人数限制
    private Integer queueMaxNum;
    //对讲状态通知时间间隔
    private Integer intercomStateSpace;
    //组下的设备
    List<DispatchDeviceDetailVO> devices;
}