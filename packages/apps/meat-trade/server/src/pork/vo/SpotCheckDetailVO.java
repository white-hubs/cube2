package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import lombok.Data;

/**
 * @author lzy
 * @date 2021/12/8
 */
@Data
public class SpotCheckDetailVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 食安检测主键
     */
    private Long spotCheckId;

    /**
     * 检测项字典主键
     */
    private Long spotCheckItemId;

    /**
     * 检测项字典名称
     */
    private String spotCheckItemName;

    /**
     * 食安抽检枚举
     */
    private SpotCheckEnum passed;

    /**
     * 是否选中
     */
    private SpotCheckEnum selected;

}
