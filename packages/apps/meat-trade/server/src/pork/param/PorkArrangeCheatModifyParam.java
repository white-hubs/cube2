package com.guhecloud.cube.biz.meattrade.server.pork.param;

import lombok.Data;

/**
 * 理货数据修改作弊嫌疑
 *
 * @author lanchun
 * @date 2023/7/31 11:48
 */
@Data
public class PorkArrangeCheatModifyParam {

    /**
     * 理货详情主键
     */
    private Long porkArrangeDetailId;

    /**
     * 作弊状态
     */
    private Boolean cheat;
}
