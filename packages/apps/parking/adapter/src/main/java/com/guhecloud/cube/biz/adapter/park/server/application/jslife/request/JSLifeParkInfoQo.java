package com.guhecloud.cube.biz.adapter.park.server.application.jslife.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 停车场入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JSLifeParkInfoQo {
    /**
     * {
     *   “pno”:”test”,
     *   “tn”:”757C3F0B0291425E99E0F034DE49855F”,
     *   “ts”:”20171124125469456”,
     *   “sn”:”14B50BBC30E9471C8F8AC4ADA0D8DA51”,
     *   “ve”:”1.0”,
     *   "dataItems": [{
     *     \"itemId\": \"12345\",
     *     \"parkName\": \"天安门广场停车场\",
     *     \"parkCode\": \"PA12345\",
     *     \"totalSpace\": 200,
     *     \"remainSpace\": 50,
     *     \"remainBookSpace\": 10,
     *     \"totalBookSpace\": 20,
     *     \"attach\": \"{\"key\":\"value\"}"
     *   },{
     *     "itemId": "123456",
     *     "parkName": "天安门广场1号停车场",
     *     "parkCode": "PA123456",
     *     "totalSpace": 200,
     *     "remainSpace": 50,
     *     "remainBookSpace": 100,
     *     "totalBookSpace": 200,
     *     "attach": "{\"key\":\"value\"}"
     *   }]
     * }
     */

    /**
     * 停车场ID
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
     * 总车位数
     */
    private Integer totalSpace;
    /**
     * 剩余车位数
     */
    private Integer remainSpace;
    /**
     * 剩余预定车位数
     */
    private Integer remainBookSpace;
    /**
     * 总预定车位数
     */
    private Integer totalBookSpace;
    /**
     * 备用字段 json格式字符串：{“key”,“value”}
     */
    private String attach;

}
