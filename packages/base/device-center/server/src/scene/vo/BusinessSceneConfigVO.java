package com.guhecloud.cube.base.device.center.scene.vo;

import com.guhecloud.cube.base.device.center.scene.enumeration.BusinessSceneConfigTypeEnum;
import lombok.Data;

/**
 * 业务场景配置返回参数
 *
 * @author lanchun
 * @date 2021/12/2 15:00
 */
@Data
public class BusinessSceneConfigVO {

    private Long id;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 场景类型序列号
     */
    private String sceneTypeSequence;

    /**
     * 业务类型
     */
    private BusinessSceneConfigTypeEnum businessType;

    /**
     * 业务主键
     */
    private String businessId;
}
