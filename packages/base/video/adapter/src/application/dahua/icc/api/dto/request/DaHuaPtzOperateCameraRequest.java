package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants.DaHuaApiConstant.PTZ_OPERATE_CAMERA;

/**
 * 云台镜头控制请求
 *
 * @author lanchun
 * @date 2025/5/16 16:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaPtzOperateCameraRequest extends AbstractDaHuaRequest {

    /**
     * 视频通道编码，第一个$后数字代表通道类型，必须是1;
     */
    private String channelId;

    /**
     * 操作类型：1=变倍，2=变焦，3=光圈
     */
    private String operateType;

    /**
     * 方向：1=增加，2=减小
     */
    private String direct;

    /**
     * 转动速度（1-8的整数） ，越大代表越快
     */
    private String step;

    /**
     * 命令：0=停止动作，1=开启动作；注意事项：停止动作需与开启动作时的其他参数需保持一致，否则无法停止
     */
    private String command;

    /**
     * 扩展数据
     */
    private String extend;

    @Override
    public String getRequestUrl() {
        return getRequestUrl(PTZ_OPERATE_CAMERA).build();
    }
}
