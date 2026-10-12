package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result;

import lombok.Data;

import java.util.List;

/**
 * 实时pm2.5统计返回对象
 */
@Data
public class PmTwoPointFiveStatResult {
    /**
     * 空气质量
     */
    private String quality;
    /**
     * 最大数据
     */
    private String max;
    /**
     * 平均数据
     */
    private String avg;

    /**
     * top10
     */
    List<PmTwoPointFiveTopResult> list;
}
