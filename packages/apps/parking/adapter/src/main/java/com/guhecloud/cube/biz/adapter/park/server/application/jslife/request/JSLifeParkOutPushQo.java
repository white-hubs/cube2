package com.guhecloud.cube.biz.adapter.park.server.application.jslife.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 捷顺数据推送请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JSLifeParkOutPushQo {
    /**
     * {
     * “pno”:”平台编号”,
     * “tn”:”令牌”,
     * “ts”:”请求时间”
     * “sn”:”签名”,
     * “ve”:”版本号”,
     * “dataItems”:”业务参数”
     * }
     */
    /**
     * 平台编号
     */
    private String pno;
    /**
     * 令牌
     */
    private String tn;
    /**
     * 请求时间
     */
    private String ts;
    /**
     * 签名
     */
    private String sn;
    /**
     * 版本号
     */
    private String ve;
    /**
     * 业务参数
     */
    private List<JSLifeParkOutQo> dataItems;
}
