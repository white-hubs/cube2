package com.guhecloud.cube.biz.mdata.server.stall.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

/**
 * 档位更新信息
 */
@Data
public class StallUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

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
    @Size(max = 20, message = "实用面积不能大于20")
    private String practicalArea;

    /**
     * 部门ID
     */
    @NotNull(message = "部门不能为空")
    private List<String> deptIds;

    /**
     * 出租类型
     */
    private Boolean leaseType;

    /**
     * 是否出租
     */
    private Boolean leased;

    /**
     * 档主名称
     */
    @Size(max = 50, message = "档主名称不能大于50个字符")
    private String masterName;

    /**
     * 档主手机号
     */
    @Size(max = 20, message = "档主手机号不能大于20")
    private String masterPhone;

    /**
     * 附件主键集合
     */
    private Set<Long> files;

    /**
     * 备注说明
     */
    @Size(max = 200, message = "备注说明不能大于200")
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 可视化编码
     */
    @Size(max = 50, message = "档位模型编码不能大于50")
    private String visualizationCode;

    /**
     * 档位扩展信息
     */
    private StallExpandModifyParam stallExpand;
}
