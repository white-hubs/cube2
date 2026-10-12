package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike;

import lombok.Data;

@Data
public class DoorEvent {
    private String personnel_name;//人员名称
    private String full_org_name;//完整组织架构
    //凭证类型：1开门卡，2保安卡，3胁迫卡，4配置卡，5按钮开门，6蓝牙卡门，7二维码，8密码，9人脸，10远程开门
    private String card_type;//卡类型
    private String card_no;//卡号
    private String access_time;//访问时间
    private Integer door_id;//门ID
    private String door_name;//门名称
    private Integer access_status;//放行状态：1：通行，0：不放行
    private String temperature;//温度
    private String remark;//备注

}
