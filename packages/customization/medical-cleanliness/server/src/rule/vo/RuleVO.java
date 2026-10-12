package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.DeviceMonitoringVo;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-20  15:43
 * @Description:
 * @Version: 1.0
 */
@Data
public class RuleVO {
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 区域id
     */
    private String deviceIds;
    private List<DeviceMonitoringVo> deviceDTO;

    /**
     * 背景图
     */
    private FileVO file;
    /**
     * 指标设置
     */
    private List<RuleNormVO> ruleNormList;

    /**
     * 租户编号
     */
    private Integer tenant;
    /**
     * 下发联动规则
     */
    private Long iotRuleId;
    /**指标区域*/
    private String areaOptions;
}
