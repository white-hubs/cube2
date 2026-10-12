package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author lzy
 * @date 2021/12/6
 */
@Data
public class SpotCheckModifyParam {

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 抽检员主键
     */
    private Long inspectorId;

    /**
     * 实际抽检片数
     */
    private Integer actualCheckSlices;

    /**
     * 瘦肉精
     */
    private SpotCheckEnum clenbuterol;

    /**
     * 含水量
     */
    private SpotCheckEnum watered;

    /**
     * 备注
     */
    private String remark;

    /**
     * 检测项集合
     */
    @NotEmpty(message = "抽检项集合不能为空")
    private List<SpotCheckDetail> details;

}
