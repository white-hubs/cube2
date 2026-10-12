package com.guhecloud.cube.biz.mdata.server.scene.vo;

import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessSceneConfigTypeEnum;
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
     * 所属项目id
     */
    private Long projectId;

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
    private Long businessId;
}
