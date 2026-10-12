package com.guhecloud.cube.biz.mdata.server.scene.vo;

import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelConfigTypeEnum;
import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelPositionEnum;
import lombok.Data;

/**
 * 设施面板配置返回参数
 *
 * @author lanchun
 * @date 2022/4/1 16:05
 */
@Data
public class BusinessPanelConfigVO {

    private Long id;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 设施类型
     */
    private BusinessPanelConfigTypeEnum businessType;

    /**
     * 设施主键
     */
    private Long businessId;

    /**
     * 面板定位
     */
    private BusinessPanelPositionEnum panelPosition;

    /**
     * 面板配置
     */
    private String panelConfig;

    private UniversalPanelVo universalPanelVo;
}
