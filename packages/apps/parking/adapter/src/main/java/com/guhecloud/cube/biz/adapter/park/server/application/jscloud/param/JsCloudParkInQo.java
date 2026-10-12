package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 入场记录
 * 接口协议: 有车辆入场时，数据中心平台主动推送入场数据到第三方平台
 * 接收地址: http://serverUrl/park/parkIn
 *
 * @author appet
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkInQo {
    /**
     * 记录id，必填，长度32，id唯一
     */
    @NotBlank(message = "记录id不能为空")
    private String itemId;
    /**
     * 停车场名称，必填，长度30
     */
    @NotBlank(message = "停车场名称不能为空")
    private String parkName;
    /**
     * 停车场编号，必填，长度30
     */
    @NotBlank(message = "停车场编号不能为空")
    private String parkCode;
    /**
     * 入场时间，必填，长度20，格式yyyy-MM-dd HH:mm:ss
     */
    @NotBlank(message = "入场时间不能为空")
    private String inTime;
    /**
     * 操作员，可选，长度30
     */
    private String inOperator;
    /**
     * 车牌，必填，长度10，异常情况时无车牌
     */
    private String carNumber;
    /**
     * 未发行的卡号，如纸票号等，可选，长度20
     */
    private String idno;
    /**
     * 设备名称，必填，长度30
     */
    @NotBlank(message = "设备名称不能为空")
    private String equipName;
    /**
     * 设备编号，必填，长度30
     */
    @NotBlank(message = "设备编号不能为空")
    private String equipCode;
    /**
     * 图片路径，可选，长度100
     */
    private String inCarPhoto;
    /**
     * 车辆信息，可选，长度500，Json字符串
     */
    private String vehicleInfo;
    /**
     * 联系地址，可选，长度100
     */
    private String address;
    /**
     * 经度，可选
     */
    private Double longitude;
    /**
     * 纬度，可选
     */
    private Double latitude;
    /**
     * 车场事件，可选，长度50
     */
    private String eventType;
    /**
     * 卡类型，可选，长度50
     */
    private String cardType;
    /**
     * 备用字段，可选，长度100，json格式字符串
     */
    private String attach;
    /**
     * 路内车场标记，可选，=1表示路内车场，=0或字段不存在表示非路内车场
     */
    private Integer isRoad;
    /**
     * 泊位号，可选，长度50，城市停车提供
     */
    private String carportNo;
    /**
     * 停车系统编号，可选，长度50，城市停车提供
     */
    private String citySystemNo;
}
