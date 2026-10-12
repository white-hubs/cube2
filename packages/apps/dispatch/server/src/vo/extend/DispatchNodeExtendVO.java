package com.guhecloud.cube.biz.dispatch.server.vo.extend;

import com.guhecloud.cube.biz.dispatch.server.vo.DispatchNodeVO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Appet
 * @Description 节点模型
 * @date 2022-07-26 10:08:36
 */
@Data
@SuperBuilder
public class DispatchNodeExtendVO extends DispatchNodeVO {
    private Boolean isRecord;
    private Boolean isVideo;
    private Integer groupLevel;
    private Integer deviceLevel;
    private String password;
    private String remark;
    //对讲空闲超时
    private Integer intercomIdleTimeOut;
    //话权方总超时
    private Integer intercomPowerTimeCount;
    //排队人数限制
    private Integer queueMaxNum;
    //对讲状态通知时间间隔
    private Integer intercomStateSpace;
}