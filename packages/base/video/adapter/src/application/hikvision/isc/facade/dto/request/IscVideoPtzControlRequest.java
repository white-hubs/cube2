package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 海康isc摄像机云台控制参数
 *
 * @author lanchun
 * @date 2023/11/10 10:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IscVideoPtzControlRequest extends BasicRequest {

    /**
     * 监控点编号
     */
    private String cameraIndexCode;

    /**
     * 0-开始 ，1-停止
     * 注：GOTO_PRESET命令下填任意值均可转到预置点,建议填0即可
     */
    private Integer action;

    /**
     * LEFT 左转
     * RIGHT右转
     * UP 上转
     * DOWN 下转
     * ZOOM_IN 焦距变大
     * ZOOM_OUT 焦距变小
     * LEFT_UP 左上
     * LEFT_DOWN 左下
     * RIGHT_UP 右上
     * RIGHT_DOWN 右下
     * FOCUS_NEAR 焦点前移
     * FOCUS_FAR 焦点后移
     * IRIS_ENLARGE 光圈扩大
     * IRIS_REDUCE 光圈缩小
     * WIPER_SWITCH 接通雨刷开关
     * START_RECORD_TRACK 开始记录运行轨迹
     * STOP_RECORD_TRACK 停止记录运行轨迹
     * START_TRACK 开始运行轨迹
     * STOP_TRACK 停止运行轨迹；
     * 以下命令presetIndex不可为空：
     * GOTO_PRESET到预置点
     */
    private String command;

    /**
     * 云台速度，取值范围为1-100，默认50
     */
    private Integer speed;

    /**
     * 预置点编号，可通过查询预置点信息接口获取整数，通常在300以内
     */
    private Integer presetIndex;

    @Override
    public Map<String, String> getUrl() {
        return this.getUrl("/api/video/v1/ptzs/controlling").build();
    }
}
