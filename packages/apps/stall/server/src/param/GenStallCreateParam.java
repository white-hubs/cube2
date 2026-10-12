package com.guhecloud.cube.srv.gen.stall.server.param;

import lombok.Data;

import java.util.List;

/**
 * 档位新增信息
 */
@Data
public class GenStallCreateParam {

    /**
     * 档位编号
     */
    private String code;

    /**
     * 档位名称
     */
    private String name;

    /**
     * 档位类型
     */
    private String type;

    /**
     * 档位区域
     */
    private List<String> areaIds;

    /**
     * 具体位置
     */
    private String location;

    /**
     * 建筑面积
     */
    private String builtUpArea;

    /**
     * 实用面积
     */
    private String practicalArea;

    /**
     * 部门ID
     */
    private List<String> deptIds;

    /**
     * 出租类型
     */
    private Boolean leaseType;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
