package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.convert;

import com.guhecloud.platform.base.util.StringUtils;

public class HikvisionIscConvert {

    private HikvisionIscConvert() {

    }

    public static Integer convertTransMode(String transMode) {
        if (StringUtils.isEmpty(transMode)) {
            return null;
        }
        Integer hikvisionIscTransMode = null;
        switch (transMode) {
            case "UDP":
                hikvisionIscTransMode = 0;
                break;
            case "TCP":
                hikvisionIscTransMode = 1;
                break;
            default:
                hikvisionIscTransMode = 1;
        }
        return hikvisionIscTransMode;
    }

    public static String convertProtocol(String protocol) {
        if (StringUtils.isEmpty(protocol)) {
            return null;
        }
        String hikvisionIscProtocol = null;
        switch (protocol) {
            case "RTSP":
                hikvisionIscProtocol = "rtsp";
                break;
            case "HLS":
                hikvisionIscProtocol = "hik";
                break;
            case "HLS_S":
                hikvisionIscProtocol = "hlss";
            case "RTMP":
                hikvisionIscProtocol = "rtmp";
                break;
            case "WSS":
                hikvisionIscProtocol = "wss";
                break;
            default:
                hikvisionIscProtocol = "ws";
        }
        return hikvisionIscProtocol;
    }

    public static String convertStreamForm(String streamForm) {
        if (StringUtils.isEmpty(streamForm)) {
            return null;
        }
        String hikvisionStreamForm = null;
        switch (streamForm) {
            case "PS":
                hikvisionStreamForm = "ps";
                break;
            case "RTP":
                hikvisionStreamForm = "rtp";
                break;
            default:
                hikvisionStreamForm = "ps";
        }
        return hikvisionStreamForm;
    }

    public static Integer convertPtzAction(String action) {
        if (StringUtils.isEmpty(action)) {
            return null;
        }
        int hikvisionPtzAction = -1;
        switch (action) {
            case "START":
                hikvisionPtzAction = 0;
                break;
            case "STOP":
                hikvisionPtzAction = 1;
                break;
            default:
                hikvisionPtzAction = 1;
        }
        return hikvisionPtzAction;
    }
}
