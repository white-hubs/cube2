package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Data;

import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-20  15:18
 * @Description:
 * @Version: 1.0
 */
@Data
public class HiddendangerWordOrderStatisticsDTO {
    /**
     * 隐患类型
     */
    @ExcelProperty("隐患类型")
    private String hiddenTypeName;
    /**
     * 总数
     */
    @ExcelProperty("工单数")
    private Integer totalNumber = 0;
    /**
     * 待处理数
     */
    @ExcelProperty("待处理")
    private Integer waitNumber = 0;
    /**
     * 已处理数
     */
    @ExcelProperty("已处理")
    private Integer dealNumber = 0;
    /**
     * 处理率
     */
    @ExcelProperty("处理率")
    private String dealRate;
    /**
     * 平均处理时长
     */
    @ExcelProperty("平均处理时长")
    private String avgWaitTime;
    /**
     * 总时长(分钟)
     */
    @ExcelIgnore
    private Long totalWaitTime;

    public void getExtend() {
        if (totalNumber == null || totalNumber.equals(0)) {
            this.dealRate = "100%";
        } else if (dealNumber != null && dealNumber > 0) {
            double rate = ((double) dealNumber / (double) totalNumber) * 100;
            String s = String.format("%.2f", rate);
            this.dealRate = s + "%";
        } else {
            this.dealRate = "0%";
        }
        // 计算平均处理时长转为x小时xx分钟：总时长/总数*60
        if (totalWaitTime != null && totalNumber != 0) {
            Long avg = totalWaitTime / totalNumber;
            avgWaitTime = String.format("%d小时%d分钟", avg / 60, avg % 60);
        }
    }

    public static HiddendangerWordOrderStatisticsDTO getHiddendangerWordOrderStatisticsDTO(Map<Long, HiddenDangerType> hiddenTypeMap,
                                                                                           Long hiddenTypeId
                                                                                          ) {
        HiddendangerWordOrderStatisticsDTO hiddendangerWordOrderStatisticsDTO = null;
        HiddenDangerType hiddenDangerType = hiddenTypeMap.get(hiddenTypeId);
        if (ObjectUtils.isNotEmpty(hiddenDangerType)) {
            hiddendangerWordOrderStatisticsDTO = new HiddendangerWordOrderStatisticsDTO();
            hiddendangerWordOrderStatisticsDTO.setHiddenTypeName(hiddenDangerType.getName());
        }
        return hiddendangerWordOrderStatisticsDTO;
    }
}
