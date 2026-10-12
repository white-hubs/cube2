package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework;

import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.ProjectRecordVO;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/18 10:51
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LooseWorksReformVO {

    /**
     * 工程整改总数
     */
    private Integer reformNum;

    /**
     * 环比率
     */
    @Decimal
    private BigDecimal reformChainRatio;

    /**
     * 总数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 各市场整改数据排名
     */
    private List<ProjectRecordVO> records;
}
