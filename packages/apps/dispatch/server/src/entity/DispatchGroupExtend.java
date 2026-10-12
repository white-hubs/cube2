package com.guhecloud.cube.biz.dispatch.server.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 调度设备-分组-扩展信息
 * @date 2022-07-26 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DispatchGroupExtend extends BasicTenantEntity<String> {

    //
    private String id;
    //组ID
    private String groupId;
    //是否录音
    private Boolean isRecord;
    //是否录像
    private Boolean isVideo;
    //组级别
    private Integer groupLevel;
    //对讲空闲超时
    private Integer intercomIdleTimeOut;
    //话权方总超时
    private Integer intercomPowerTimeCount;
    //排队人数限制
    private Integer queueMaxNum;
    //对讲状态通知时间间隔
    private Integer intercomStateSpace;

}