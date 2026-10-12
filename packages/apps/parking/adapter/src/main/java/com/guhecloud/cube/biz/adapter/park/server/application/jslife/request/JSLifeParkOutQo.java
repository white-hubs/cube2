package com.guhecloud.cube.biz.adapter.park.server.application.jslife.request;

import lombok.Data;

/**
 * 捷顺停车场上报车场入场记录 入参
 */
@Data
public class JSLifeParkOutQo {
    /**
     * {
     *   "app_id": 910978440435326977,
     *   "method": "3c.pay.queryorder",
     *   "timestamp": 1640161882671,
     *   "projectCode": "p210232374",
     *   "abilityCode": "kfpt_syfw",
     *   "sign": "LL48H7JFS/UsZONVRVUOzRsbZFOj3w+2UQficwShHX8wRif7NsrUrMtnxR820rjZJ4yQ1PZ3XEeitVsfNq53bw8HbPRzWj6kw5N7jAZ1fCLMkD2KCC9Veg3hMgmVVqzcK2cgLitGcNjiXfRxShSa1SNB5jXiQbZV479kKtSfmMU=",
     *   "sign_type": "md5",
     *   "format": "form",
     *   "charset": "utf-8",
     *   "biz_content": [{
     *     "itemId": "123456",
     *     "parkName": "天安门广场1号停车场",
     *     "parkCode": "PA123456",
     *     "carNumber": "京A12345",
     *     "outTime": "2023-03-01 14:00:00",
     *     "ysMoney": 20.0,
     *     "yhMoney": 5.0,
     *     "ssMoney": 15.0,
     *     "freeMoney": 0.0,
     *     "hgMoney": 0.0,
     *     "inTime": "2023-03-01 12:00:00",
     *     "inEquipCode": "EQ123456",
     *     "inEquipName": "入口设备1",
     *     "inRecordId": 123456,
     *     "outMode": "自动离场",
     *     "eventType": "出场",
     *     "cardType": "临时卡",
     *     "outOperator": 1,
     *     "outEquipCode": "EQ654321",
     *     "outEquipName": "出口设备1",
     *     "payTypeName": "微信支付",
     *     "idno": "PAPER123456",
     *     "isReal": 1,
     *     "outCarPhoto": "/path/to/outCarPhoto.jpg",
     *     "vehicleInfo": "小轿车",
     *     "address": "北京市东城区天安门广场",
     *     "longitude": 116.404,
     *     "latitude": 39.915,
     *     "attach": "{\"key\":\"value\"}",
     *     "isRoad": 0,
     *     "carportNo": "CP123456",
     *     "citySystemNo": "CS123456"
     *   }]
     * }
     */

    /**
     * 记录id
     */
    private String itemId;
    /**
     * 停车场名称
     */
    private String parkName;
    /**
     * 停车场编号
     */
    private String parkCode;
    /**
     * 车牌
     */
    private String carNumber;
    /**
     * 出场时间 格式yyyy-MM-dd HH:mm:ss
     */
    private String outTime;
    /**
     * 应收金额
     */
    private Double ysMoney;
    /**
     * 优惠金额
     */
    private Double yhMoney;
    /**
     * 实收金额
     */
    private Double ssMoney;
    /**
     * 免费金额
     */
    private Double freeMoney;
    /**
     * 回滚减免金额
     */
    private Double hgMoney;
    /**
     * 入场时间
     */
    private String inTime;
    /**
     * 入场设备编号
     */
    private String inEquipCode;
    /**
     * 入场设备名称
     */
    private String inEquipName;
    /**
     * 入场记录id
     */
    private Double inRecordId;
    /**
     * 车辆出场方式
     */
    private String outMode;
    /**
     * 车场事件
     */
    private String eventType;
    /**
     * 卡类型
     */
    private String cardType;
    /**
     * 操作员
     */
    private String outOperator;
    /**
     * 出场设备编号
     */
    private String outEquipCode;
    /**
     * 车场设备名称
     */
    private String outEquipName;
    /**
     * 付款方式
     */
    private String payTypeName;
    /**
     * 未发行的卡号，如纸票号等
     */
    private String idno;
    /**
     * 是否是收费记录
     */
    private Integer isReal;
    /**
     * 图片路径
     */
    private String outCarPhoto;
    /**
     * 车辆信息
     */
    private String vehicleInfo;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 备用字段
     */
    private String attach;
    /**
     * 路内车场标记
     */
    private Integer isRoad;
    /**
     * 泊位号
     */
    private String carportNo;
    /**
     * 停车系统编号
     */
    private String citySystemNo;
}
