package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.convert;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaPtzOperateCameraRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaPtzOperateDirectRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaSnapCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoPtzControlRequest;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Objects;

public class DaHuaConvert {

    private DaHuaConvert() {

    }

    /**
     * 协议转换
     *
     * @param protocol
     *
     * @return
     */
    public static String convertProtocol(String protocol) {
        if (StringUtils.isEmpty(protocol)) {
            return null;
        }
        String daHuaProtocol = null;
        switch (protocol) {
            case "NATIVE":
                daHuaProtocol = "UN_SUPPORT";
                break;
            case "WS":
                daHuaProtocol = "ws_flv";
                break;
            case "WSS":
                daHuaProtocol = "wss_flv";
                break;
            case "RTC":
                daHuaProtocol = "UN_SUPPORT";
                break;
            case "HLS":
                daHuaProtocol = "hls";
                break;
            case "RTMP":
                daHuaProtocol = "rtmp";
                break;
            case "HLS_S":
                daHuaProtocol = "hlss";
            default:
                daHuaProtocol = "ws_flv";
        }
        return daHuaProtocol;
    }

    /**
     * 码流转换
     *
     * @param streamType
     *
     * @return
     */
    public static String convertStreamType(Integer streamType) {
        if (Objects.isNull(streamType)) {
            return null;
        }
        String daHuaStreamType;
        switch (streamType) {
            case 0:
                daHuaStreamType = "1";
                break;
            case 1:
                daHuaStreamType = "2";
                break;
            case 2:
                daHuaStreamType = "3";
            default:
                daHuaStreamType = "1";
        }
        return daHuaStreamType;
    }

    /**
     * 云台镜头控制参数转换
     *
     * @param deviceId
     * @param param
     *
     * @return
     */
    public static DaHuaPtzOperateCameraRequest convertPtzLensCommand(String deviceId,
                                                                     VideoPtzControlRequest param
                                                                    ) {
        if (StringUtils.isEmpty(param.getCommand())) {
            return null;
        }
        DaHuaPtzOperateCameraRequest request = new DaHuaPtzOperateCameraRequest();
        request.setChannelId(deviceId);
        if ("STOP".equals(param.getAction())) {
            request.setCommand("0");
        } else {
            request.setCommand("1");
        }
        String daHuaPtzOperateType;
        String daHuaPtzDirect;
        switch (param.getCommand()) {
            case "ZOOM_IN":
                daHuaPtzOperateType = "2";
                daHuaPtzDirect = "1";
                break;
            case "ZOOM_OUT":
                daHuaPtzOperateType = "2";
                daHuaPtzDirect = "2";
                break;
            default:
                daHuaPtzOperateType = "un_support";
                daHuaPtzDirect = "-1";
        }
        request.setOperateType(daHuaPtzOperateType);
        request.setDirect(daHuaPtzDirect);
        request.setStep(param.getSpeed().toString());
        return request;
    }

    /**
     * 云台方向控制参数转换
     *
     * @param deviceId
     * @param param
     *
     * @return
     */
    public static DaHuaPtzOperateDirectRequest convertPtzDirectionCommand(String deviceId,
                                                                          VideoPtzControlRequest param
                                                                         ) {
        if (StringUtils.isEmpty(param.getCommand())) {
            return null;
        }
        DaHuaPtzOperateDirectRequest request = new DaHuaPtzOperateDirectRequest();
        request.setChannelId(deviceId);
        if ("STOP".equals(param.getAction())) {
            request.setCommand("0");
        } else {
            request.setCommand("1");
        }
        String daHuaPtzDirect;
        String daHuaPtzStepX = "0";
        String daHuaPtzStepY = "0";
        switch (param.getCommand()) {
            case "LEFT":
                daHuaPtzDirect = "3";
                daHuaPtzStepX = param.getSpeed().toString();
                break;
            case "RIGHT":
                daHuaPtzDirect = "4";
                daHuaPtzStepX = param.getSpeed().toString();
                break;
            case "UP":
                daHuaPtzDirect = "1";
                daHuaPtzStepY = param.getSpeed().toString();
                break;
            case "DOWN":
                daHuaPtzDirect = "2";
                daHuaPtzStepY = param.getSpeed().toString();
                break;
            case "LEFT_UP":
                daHuaPtzDirect = "5";
                daHuaPtzStepX = param.getSpeed().toString();
                daHuaPtzStepY = param.getSpeed().toString();
                break;
            case "LEFT_DOWN":
                daHuaPtzDirect = "6";
                daHuaPtzStepX = param.getSpeed().toString();
                daHuaPtzStepY = param.getSpeed().toString();
                break;
            case "RIGHT_UP":
                daHuaPtzDirect = "7";
                daHuaPtzStepX = param.getSpeed().toString();
                daHuaPtzStepY = param.getSpeed().toString();
                break;
            case "RIGHT_DOWN":
                daHuaPtzDirect = "8";
                daHuaPtzStepX = param.getSpeed().toString();
                daHuaPtzStepY = param.getSpeed().toString();
                break;
            default:
                daHuaPtzDirect = "un_support";
        }
        request.setDirect(daHuaPtzDirect);
        request.setStepX(daHuaPtzStepX);
        request.setStepY(daHuaPtzStepY);
        return request;
    }

    /**
     * 抓拍参数转换
     *
     * @param channelCode
     * @param deviceCode
     *
     * @return
     */
    public static DaHuaSnapCaptureRequest convertSnapCaptureCommand(String channelCode, String deviceCode) {
        DaHuaSnapCaptureRequest.Device device = new DaHuaSnapCaptureRequest.Device();
        device.setDevID(deviceCode);
        device.setDevChannel(Integer.valueOf(channelCode.substring(channelCode.lastIndexOf("$") + 1)));

        DaHuaSnapCaptureRequest.Operation operation = new DaHuaSnapCaptureRequest.Operation();
        operation.setId(System.currentTimeMillis());
        operation.setParams(device);

        DaHuaSnapCaptureRequest request = new DaHuaSnapCaptureRequest();
        request.setDeviceCode(deviceCode);
        request.setOperationObj(operation);
        request.setParams(JsonUtils.serialize(operation));
        return request;
    }
}
