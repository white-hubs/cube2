package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author wql
 * @describe 现场点列表查询参数
 * @date 2023/6/5 18:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InspectScenePointQueryParam extends Paging {
    private Set<Long> ids;
    /**
     * 现场点名称
     */
    private String pointName;
    /**
     * 现场点类型id
     */
    private Long typeId;
    /**
     * 部门id
     */
    private Long receiveDepId;
    /**
     * 区域id
     */
    private String areaId;
}
