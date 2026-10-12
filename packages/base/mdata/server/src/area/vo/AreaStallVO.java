package com.guhecloud.cube.biz.mdata.server.area.vo;

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
public class AreaStallVO extends Tree.Simple<Long, AreaStallVO> {

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

}
