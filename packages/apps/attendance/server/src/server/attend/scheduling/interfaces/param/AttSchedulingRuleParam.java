package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 排班规则入参对象
 */
@Data
public class AttSchedulingRuleParam {
    private Long id;
    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    private String name;
    /**
     * 排班方式(固定班，三班倒。枚举AttSchedulingWaysEnum)
     */
    @NotBlank(message = "排班方式不能为空")
    private String ways;
    /**
     * 法定节假日是否休息
     */
    private Boolean legalHolidaysRest;
    /**
     * 一个月允许最大连续排班天数
     */
    private Integer continuousDays;
    /**
     * 一天两个班次最小间隔小时
     */
    private Integer intervalHour;
    /**
     * 一个月相同班次数据json
     */
    private String sameShift;

    /**
     * 排班规则班次集合
     */
    private List<AttSchedulingRuleClassesParam> rulesClassesList;

    public void validBusiness() {
        if (legalHolidaysRest == null) {
            this.legalHolidaysRest = false;
        }

        if (continuousDays == null || continuousDays < 0 || continuousDays > 31) {
            continuousDays = 0;
        }
        if (intervalHour == null || intervalHour < 0 || intervalHour > 24) {
            intervalHour = 0;
        }
    }

}
