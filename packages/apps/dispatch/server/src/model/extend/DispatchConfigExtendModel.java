package com.guhecloud.cube.biz.dispatch.server.model.extend;

import lombok.Data;

@Data
public class DispatchConfigExtendModel {
    private String rootNode;
    private Center center;
    private String fileUrl;
    private Server server;

    @Data
    public static class Center {
        private String lng;
        private String lat;
    }

    @Data
    public static class Server {
        private String ip;
        private String port;
    }

}
