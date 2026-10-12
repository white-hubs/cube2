package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 理货机理货片数
 */
@Data
public class Tally {

    /**
     * 挂钩号
     */
    private String rfid1;

    /**
     * 脚标号
     */
    private String rfid2;

    /**
     * 重量
     */
    @Decimal
    private BigDecimal weight;

    /**
     * 图片路径
     */
    private List<String> picPaths;

    /**
     * 理货时间
     */
    private Long time;

    /**
     * 作弊状态
     */
    private Boolean cheat;
}
