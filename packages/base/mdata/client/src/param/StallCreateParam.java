package com.guhecloud.cube.biz.mdata.client.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 档位新增信息
 */
@Data
public class StallCreateParam {

    /**
     * 档位编号
     */
    @NotEmpty(message = "编号不能为空")
    @Size(max = 100, message = "编号不能大于100")
    private String code;

    /**
     * 档位名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String name;

    /**
     * 档位类型
     */
    @NotEmpty(message = "类型不能为空")
    @Size(max = 20, message = "类型不能大于20")
    private String type;

    /**
     * 档位区域
     */
    @NotEmpty(message = "区域不能为空")
    private List<String> areaIds;

    /**
     * 具体位置
     */
    @NotEmpty(message = "具体位置不能为空")
    @Size(max = 100, message = "具体位置不能大于100")
    private String location;

    /**
     * 建筑面积
     */
    @NotEmpty(message = "建筑面积不能为空")
    @Size(max = 20, message = "建筑面积不能大于20")
    private String builtUpArea;

    /**
     * 实用面积
     */
    @NotEmpty(message = "实用面积不能为空")
    @Size(max = 20, message = "实用面积不能大于20")
    private String practicalArea;

    /**
     * 部门ID
     */
    @NotEmpty(message = "部门不能为空")
    private List<String> deptIds;

    /**
     * 出租类型
     */
    private Boolean leaseType;

    /**
     * 备注说明
     */
    @Size(max = 200, message = "备注说明不能大于200")
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
