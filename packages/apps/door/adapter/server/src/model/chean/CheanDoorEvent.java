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
public class CheanDoorEvent {
    //卡编号
    @JsonProperty("CardNo")
    private String cardNo;
    //物理卡号
    @JsonProperty("CardID")
    private String cardId;
    //卡表面号
    @JsonProperty("NumNo")
    private String numNo;
    //控制板编号
    @JsonProperty("ControllerNo")
    private String controllerNo;
    //门号
    @JsonProperty("DoorNo")
    private String doorNo;
    //门名称
    @JsonProperty("RoomName")
    private String roomName;
    //开门类型
    @JsonProperty("IOType")
    private String ioType;
    //开门类型名称
    @JsonProperty("IOTypeName")
    private String ioTypeName;
    //进出时间
    @JsonProperty("IOTime")
    private String ioTime;

    public String getDoorNo() {
        return controllerNo + "_" + doorNo;
    }
}
