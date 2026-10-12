package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheanDoorStatus {
    //控制板编号
    @JsonProperty("controllerno")
    private String controllerNo;
    @JsonProperty("controllername")
    private String controllerName;
    //门号
    @JsonProperty("doorno")
    private String doorNo;
    //门名称
    @JsonProperty("roomname")
    private String roomName;
    //连接状态(1连接,0:掉线)
    @JsonProperty("connected")
    private String connected;
    @JsonProperty("connectedname")
    private String connectedName;
    //撬设备状态(-1未知 0:正常 1:撬设备)
    @JsonProperty("broken")
    private String broken;
    @JsonProperty("brokenname")
    private String brokenName;

    //超时未关门状态(-1未知 0:正常 1:超时未关门)
    @JsonProperty("openedtimeout")
    private String openedTimeout;
    @JsonProperty("openedtimeoutname")
    private String openedTimeoutName;
    //门状态(-1 未知 0:关门  1:开门 2:强制开门)
    @JsonProperty("opened")
    private String opened;
    @JsonProperty("openedname")
    private String openedName;
    //地址
    @JsonProperty("addr")
    private String addr;

    public String getDoorNo() {
        return controllerNo + "_" + doorNo;
    }
}
