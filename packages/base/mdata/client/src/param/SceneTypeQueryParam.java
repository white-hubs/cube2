package com.guhecloud.cube.biz.mdata.client.param;

import lombok.Data;

import java.util.List;

/**
 * 场景类型信息
 *
 * @author lanchun
 * @date 2021/12/2 17:42
 */
@Data
public class SceneTypeQueryParam {

    /**
     * 场景主键集合
     */
    private List<Long> ids;
}
