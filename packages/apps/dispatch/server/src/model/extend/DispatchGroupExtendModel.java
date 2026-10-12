package com.guhecloud.cube.biz.dispatch.server.model.extend;

import com.guhecloud.cube.biz.dispatch.server.model.DispatchGroupModel;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备-分组-扩展信息
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchGroupExtendModel extends DispatchGroupModel {
    private Boolean isRecord;//是否录音：0-否 1-是(默认1)
    private Integer groupLevel;//组等级
    private Integer intercomIdleTimeOut;//对讲空闲超时
    private Integer intercomPowerTimeCount;//话权方总超时
    private Integer queueMaxNum;//排队人数限制
    private Integer intercomStateSpace;//对讲状态通知时间间隔

}