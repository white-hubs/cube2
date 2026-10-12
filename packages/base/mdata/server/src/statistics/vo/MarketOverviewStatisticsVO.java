package com.guhecloud.cube.biz.mdata.server.statistics.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketOverviewStatisticsVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 租户编号
     */
    private Integer code;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 占地面积
     */
    private BigDecimal areaCovered;

    /**
     * 占地总建筑面积
     */
    private BigDecimal floorArea;

    /**
     * 租户备注
     */
    private String remark;

    /**
     * 企业宣传图信息
     */
    private List<FileVO> files;

    /**
     * 档位总数
     */
    private Long stallNum;

    /**
     * 商户总数
     */
    private Long supplierNum;
}
