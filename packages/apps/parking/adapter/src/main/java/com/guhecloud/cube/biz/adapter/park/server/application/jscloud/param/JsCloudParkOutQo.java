package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 出场记录
 * 接口协议: 有车辆出场时，数据中心平台主动推送出场数据到第三方平台
 * 接收地址: http://serverUrl/park/parkOut
 *
 * @author appet
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkOutQo {
    /**
     * 记录id，必填，长度32
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
     * 车牌，必填，长度10，异常情况时无车牌
     */
    private String carNumber;
    /**
     * 出场时间，必填，长度20，格式yyyy-MM-dd HH:mm:ss
     */
    @NotBlank(message = "出场时间不能为空")
    private String outTime;
    /**
     * 应收金额，必填，单位元
     */
    @NotNull(message = "应收金额不能为空")
    private Double ysMoney;
    /**
     * 优惠金额，可选，单位元
     */
    private Double yhMoney;
    /**
     * 实收金额，必填，单位元
     */
    @NotNull(message = "实收金额不能为空")
    private Double ssMoney;
    /**
     * 免费金额，可选，单位元
     */
    private Double freeMoney;
    /**
     * 回滚减免金额，可选，单位元
     */
    private Double hgMoney;
    /**
     * 停车时长，单位：秒
     */
    private Integer serviceFeeTime;
    /**
     * 入场时间，必填，长度20，格式yyyy-MM-dd HH:mm:ss，异常情况时无值
     */
    private String inTime;
    /**
     * 入场设备编号，必填，长度30，异常情况时无值
     */
    private String inEquipCode;
    /**
     * 入场设备名称，必填，长度30，异常情况时无值
     */
    private String inEquipName;
    /**
     * 入场记录id，必填，长度32，对应入场信息的itemId，异常情况时无值
     */
    private String inRecordId;
    /**
     * 车辆出场方式，可选，长度20
     */
    private String outMode;
    /**
     * 车场事件，可选，长度50
     */
    private String eventType;
    /**
     * 卡类型，可选，长度50
     */
    private String cardType;
    /**
     * 操作员，必填，长度30
     */
    private String outOperator;
    /**
     * 出场设备编号，必填，长度30
     */
    @NotBlank(message = "出场设备编号不能为空")
    private String outEquipCode;
    /**
     * 出场设备名称，必填，长度30
     */
    @NotBlank(message = "出场设备名称不能为空")
    private String outEquipName;
    /**
     * 付款方式，可选，长度30，文档字段名 payTypeName（WX/ZFB/YL/JYF/YFB/YZF/CASH）
     */
    private String payTypeName;
    /**
     * 未发行的卡号，如纸票号等，可选，长度30
     */
    private String idno;
    /**
     * 是否是收费记录，必填，0=出场记录，1=收费记录
     */
    private Integer isReal;
    /**
     * 图片路径，可选，长度100
     */
    private String outCarPhoto;
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
