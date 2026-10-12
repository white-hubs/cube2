package com.guhecloud.cube.srv.gen.stall.server.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 档位更新信息
 */
@Data
public class GenStallUpdateParam {

    /**
     * 主键
     */
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

    /**
     * 档位编号
     */
    @Size(max = 100, message = "编号不能大于100")
    private String code;

    /**
     * 档位名称
     */
    @Size(max = 100, message = "名称不能大于100")
    private String name;

    /**
     * 档位类型
     */
    @Size(max = 20, message = "类型不能大于20")
    private String type;

    /**
     * 档位区域
     */
    private List<String> areaIds;

    /**
     * 具体位置
     */
    @Size(max = 100, message = "具体位置不能大于100")
    private String location;

    /**
     * 建筑面积
     */
    @Size(max = 20, message = "建筑面积不能大于20")
    private String builtUpArea;

    /**
     * 实用面积
     */
    @Size(max = 20, message = "实用面积不能大于20")
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
    @Size(max = 200, message = "备注说明不能大于200")
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
