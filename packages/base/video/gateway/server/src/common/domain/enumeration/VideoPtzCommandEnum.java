package com.guhecloud.cube.base.videogw.server.common.domain.enumeration;

import lombok.Getter;

/**
 * 摄像机云台控制命令枚举
 *
 * @author lanchun
 * @date 2023/11/9 15:00
 */
@Getter
public enum VideoPtzCommandEnum {

    LEFT,//左转
    RIGHT,//右转
    UP,//上转
    DOWN,//下转
    ZOOM_IN,//焦距变大
    ZOOM_OUT,//焦距变小
    LEFT_UP,// 左上
    LEFT_DOWN,// 左下
    RIGHT_UP,// 右上
    RIGHT_DOWN,//右下
    FOCUS_NEAR,// 焦点前移
    FOCUS_FAR,// 焦点后移
    IRIS_ENLARGE,// 光圈扩大
    IRIS_REDUCE,// 光圈缩小
    WIPER_SWITCH,// 接通雨刷开关
    START_RECORD_TRACK,// 开始记录运行轨迹
    STOP_RECORD_TRACK,// 停止记录运行轨迹
    START_TRACK,// 开始运行轨迹
    STOP_TRACK,// 停止运行轨迹；

}
