package com.guhecloud.cube.biz.adapter.park.server.application.jslife.request;

import lombok.Data;

/**
 * 捷顺停车场上报车场入场记录 入参
 */
@Data
public class JSLifeParkInQo {
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
     *   "biz_content": {
     *     "itemId": "123456",
     *     "parkName": "天安门广场1号停车场",
     *     "parkCode": "PA123456",
     *     "inTime": "2023-03-01 12:00:00",
     *     "inOperator": "张三",
     *     "carNumber": "京A12345",
     *     "idno": "PAPER123456",
     *     "equipName": "入口设备1",
     *     "equipCode": "EQ123456",
     *     "inCarPhoto": "/path/to/inCarPhoto.jpg",
     *     "vehicleInfo": "小轿车",
     *     "address": "北京市东城区天安门广场",
     *     "longitude": 116.404,
     *     "latitude": 39.915,
     *     "eventType": "入场",
     *     "cardType": "临时卡",
     *     "attach": "{\"key\":\"value\"}",
     *     "isRoad": 0,
     *     "carportNo": "CP123456",
     *     "citySystemNo": "CS123456"
     *   }
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
     * 入场时间	格式yyyy-MM-dd HH:mm:ss
     */
    private String inTime;
    /**
     * 操作员
     */
    private String inOperator;
    /**
     * 车牌
     */
    private String carNumber;
    /**
     * 未发行的卡号，如纸票号等
     */
    private String idno;
    /**
     * 设备名称
     */
    private String equipName;
    /**
     * 设备编号
     */
    private String equipCode;
    /**
     * 图片路径
     */
    private String inCarPhoto;
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
     * 车场事件
     */
    private String eventType;
    /**
     * 卡类型
     */
    private String cardType;
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
