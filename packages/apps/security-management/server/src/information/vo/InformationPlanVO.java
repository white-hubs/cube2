package com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InformationPlanVO {
    /**
     * 计划主键id
     */
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 计划开始时间
     */
    private LocalDateTime planStartTime;
    /**
     * 计划结束时间
     */
    private LocalDateTime planEndTime;
    /**
     * 开屏时间（精确到分）
     */
    private String openTime;
    /**
     * 关屏时间（精确到分）
     */
    private String closeTime;
    /**
     * 是否启用 0：禁用 1：启用
     */
    private Integer isEnable;
    /**
     * 执行周期集合 1：周一 2：周二 3：周三 4：周四 5：周五 6：周六 7：周日 (逗号分割)
     */
    private List<String> executionCycle;
    /**
     * 开屏的xxljob的任务id
     */
    private String openJobTaskId;
    /**
     * 关屏的xxljob的任务id
     */
    private String closeJobTaskId;

    /**
     * 控制设备总数
     */
    private Integer deviceTotal;
    /**
     * 关联设备集合
     */
    private List<SecurityDeviceInfoVO> securityDevices;
}
