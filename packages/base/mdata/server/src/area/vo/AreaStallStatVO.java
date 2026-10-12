package com.guhecloud.cube.biz.mdata.server.area.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 区域档位信息
 *
 * @author lzy
 * @date 2021/9/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AreaStallStatVO extends Tree.Simple<Long, AreaStallStatVO> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 占地面积
     */
    private BigDecimal builtUpArea;

    /**
     * 档位数量
     */
    private Integer quantity;

    /**
     * 可出租
     */
    private Integer rentable;

    /**
     * 不可出租
     */
    private Integer notRentable;

    /**
     * 已出租
     */
    private Integer leased;

    /**
     * 未出租
     */
    private Integer unleased;

    /**
     * 空置率
     */
    @Decimal
    private Double vacantRate;

    /**
     * 档位集合
     */
    private Page<Stall> stalls;

}
