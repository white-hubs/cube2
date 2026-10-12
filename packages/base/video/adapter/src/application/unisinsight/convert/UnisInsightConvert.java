package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.convert;

import com.guhecloud.platform.base.util.StringUtils;

import java.util.Objects;

public class UnisInsightConvert {

    private UnisInsightConvert() {

    }

    public static Integer convertStreamType(Integer streamType) {
        if (Objects.isNull(streamType) || streamType <= 0) {
            return 0;
        }
        return 1;
    }

    public static Integer convertStreamMode(String protocol) {
        if (StringUtils.isBlank(protocol)) {
            return 7;
        }
        switch (protocol) {
            case "NATIVE":
                return 7;
            case "RTMP":
                return 2;
            case "HLS":
                return 3;
            case "HLS_S":
                return 4;
            case "WS":
            case "WSS":
            case "RTC":
                return 7;
            case "RTSP":
                return 1;
            default:
                return 7;
        }
    }

    public static boolean isPanTiltCommand(String command) {
        switch (command) {
            case "LEFT":
            case "RIGHT":
            case "UP":
            case "DOWN":
            case "LEFT_UP":
            case "LEFT_DOWN":
            case "RIGHT_UP":
            case "RIGHT_DOWN":
                return true;
            default:
                return false;
        }
    }

    public static Integer convertDirection(String command) {
        switch (command) {
            case "UP":
                return 1;
            case "DOWN":
                return 2;
            case "LEFT":
                return 3;
            case "RIGHT":
                return 4;
            case "LEFT_UP":
                return 5;
            case "LEFT_DOWN":
                return 6;
            case "RIGHT_UP":
                return 7;
            case "RIGHT_DOWN":
                return 8;
            default:
                return null;
        }
    }

    public static Integer convertAdjustStartOperation(String command) {
        switch (command) {
            case "ZOOM_IN":
                return 1;
            case "FOCUS_FAR":
                return 2;
            case "IRIS_ENLARGE":
                return 3;
            case "ZOOM_OUT":
                return 4;
            case "FOCUS_NEAR":
                return 5;
            case "IRIS_REDUCE":
                return 6;
            default:
                return null;
        }
    }

    public static Integer convertAdjustStopOperation(String command) {
        switch (command) {
            case "ZOOM_IN":
            case "ZOOM_OUT":
                return 1;
            case "FOCUS_NEAR":
            case "FOCUS_FAR":
                return 2;
            case "IRIS_ENLARGE":
            case "IRIS_REDUCE":
                return 3;
            default:
                return null;
        }
    }

    public static Integer convertSpeed(Integer speed) {
        if (Objects.isNull(speed) || speed < 1) {
            return 1;
        }
        if (speed > 8) {
            return 8;
        }
        return speed;
    }
}
