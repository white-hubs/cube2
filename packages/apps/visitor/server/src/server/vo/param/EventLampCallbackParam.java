package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * 大华订阅回调通用入参
 */
@Data
public class EventLampCallbackParam {
    /**
     * 事件大类
     */
    private String category;
    /**
     * 方法名
     */
    private String method;
    /**
     * 序号
     */
    private Long id;
    /**
     * 子系统名称
     */
    private String subsystem;
    /**
     * 域ID
     */
    private String domainId;
    /**
     * 业务消息内容
     */
    private JSONObject info;
}
