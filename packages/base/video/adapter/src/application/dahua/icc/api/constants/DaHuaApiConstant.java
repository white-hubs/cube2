package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants;

public class DaHuaApiConstant {

    /**
     * 获取公钥
     */
    public static final String PUBLIC_TOKEN = "/evo-apigw/evo-oauth/1.0.0/oauth/public-key";

    /**
     * 获取token
     */
    public static final String GET_TOKEN = "/evo-apigw/evo-oauth/1.0.0/oauth/extend/token";

    /**
     * 刷新token
     */
    public static final String REFRESH_TOKEN = "/evo-apigw/evo-oauth/1.0.0/oauth/extend/refresh/token";

    /**
     * rtsp实时预览
     */
    public static final String VIDEO_RTSP = "/evo-apigw/admin/API/MTS/Video/StartVideo";

    /**
     * HLS、FLV、RTMP实时预览接口方式
     */
    public static final String VIDEO_STREAM_REAL_TIME = "/evo-apigw/admin/API/video/stream/realtime";

    /**
     * 云台镜头控制
     */
    public static final String PTZ_OPERATE_CAMERA = "/evo-apigw/admin/API/DMS/Ptz/OperateCamera";

    /**
     * 云台方向控制
     */
    public static final String PTZ_OPERATE_DIRECT = "/evo-apigw/admin/API/DMS/Ptz/OperateDirect";

    /**
     * 设备远程抓图
     */
    public static final String SNAP_CAPTURE = "/evo-apigw/admin/API/EVO/invoke/DMS";

    /**
     * 获取设备通道列表（分页）
     */
    public static final String EVP_API_DEVICE_CHANNEL_PAGE = "/evo-apigw/evo-brm/1.2.0/device/channel/subsystem/page";

    private DaHuaApiConstant() {

    }
}
