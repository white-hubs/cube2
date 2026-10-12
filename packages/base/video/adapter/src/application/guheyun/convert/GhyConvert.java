package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.convert;

import com.guhecloud.platform.base.util.StringUtils;

public class GhyConvert {

    private GhyConvert() {

    }

    public static String convertProtocol(String protocol) {
        if (StringUtils.isEmpty(protocol)) {
            return null;
        }
        String ghyProtocol = null;
        switch (protocol) {
            case "NATIVE":
                ghyProtocol = "0";
                break;
            case "RTSP":
                ghyProtocol = "1";
                break;
            case "WS":
                ghyProtocol = "2";
                break;
            case "WSS":
                ghyProtocol = "3";
                break;
            case "RTC":
                ghyProtocol = "4";
                break;
            case "HLS":
                ghyProtocol = "5";
                break;
            case "RTMP":
                ghyProtocol = "6";
                break;
            case "HLS_S":
                ghyProtocol = "7";
                break;
            default:
                ghyProtocol = "2";
        }
        return ghyProtocol;
    }

    public static String convertPtzCommand(String command, String action) {
        if (StringUtils.isEmpty(command)) {
            return null;
        }
        if ("STOP".equals(action)) {
            command = "STOP";
        }
        String ghyPtzCommand = "";
        switch (command) {
            case "LEFT":
                ghyPtzCommand = "left";
                break;
            case "RIGHT":
                ghyPtzCommand = "right";
                break;
            case "UP":
                ghyPtzCommand = "up";
                break;
            case "DOWN":
                ghyPtzCommand = "down";
                break;
            case "ZOOM_IN":
                ghyPtzCommand = "zoomin";
                break;
            case "ZOOM_OUT":
                ghyPtzCommand = "zoomout";
                break;
            case "LEFT_UP":
                ghyPtzCommand = "upleft";
                break;
            case "LEFT_DOWN":
                ghyPtzCommand = "downleft";
                break;
            case "RIGHT_UP":
                ghyPtzCommand = "upright";
                break;
            case "RIGHT_DOWN":
                ghyPtzCommand = "downright";
                break;
            case "STOP":
                ghyPtzCommand = "stop";
                break;
            default:
                ghyPtzCommand = "";
        }
        return ghyPtzCommand;
    }
}
