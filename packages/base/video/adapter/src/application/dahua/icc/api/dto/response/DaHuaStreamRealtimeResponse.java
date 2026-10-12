package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response;

import lombok.Data;

@Data
public class DaHuaStreamRealtimeResponse {

    /**
     * 拉流url，流地址需拼接?token=鉴权认证接口返回的access_token;
     * hls示例：http://172.7.2.110:7086/live/cameraid/1000000%240/substream/1.m3u8?token=1:2cfa8e9b-e6c7-44d1-a676-6ae1cb9971e0
     * flv示例：http://172.7.2.110:7886/live/cameraid/1000000%240/substream/1.flv?token=1:2cfa8e9b-e6c7-44d1-a676-6ae1cb9971e0
     * ws-flv示例：ws://172.7.2.110:7886/live/cameraid/1000000%240/substream/1.flv?token=1:2cfa8e9b-e6c7-44d1-a676-6ae1cb9971e0
     * rtmp示例：rtmp://10.56.24.100:1975/live/cameraid=1004715%240;substream=1;token=1:2cfa8e9b-e6c7-44d1-a676-6ae1cb9971e0
     */
    private String url;
}
