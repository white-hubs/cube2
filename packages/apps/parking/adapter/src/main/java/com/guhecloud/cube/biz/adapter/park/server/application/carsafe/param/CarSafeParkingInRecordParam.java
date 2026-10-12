package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class CarSafeParkingInRecordParam {

    private static final String HMAC_ALGORITHM = "HmacSHA1";
    /**
     * 用户标识
     */
    private String from;
    /**
     * 时间戳 格式：yyyy-MM-dd hh:mm:ss
     */
    private String timestamp;
    /**
     * 随机数 生成 0 到 99 的随机整数
     */
    private String nonce;
    /**
     * 数据
     */
    private List<CarSafeParkingInRecordReportParam> data;
    /**
     * 签名
     */
    private String sign;

    private String branchno = "1";
    private String queryFormat = null;

}
