package com.guhecloud.cube.biz.mdata.server.stall.dto;

import com.guhecloud.cube.biz.mdata.server.stall.vo.StallExpandVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/10/19
 */
@Data
public class StallDTO {
    /**
     * 主键
     */
    private Long id;

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
     * 档位类型名称
     */
    private String typeName;

    /**
     * 档位区域
     */
    private String area;

    private String areaName;

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
    private String deptId;

    /**
     * 部门主键集合
     */
    private List<Long> deptIds;

    /**
     * 部门名称
     */
    private List<String> deptNames;

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

    /**
     * 档主名称
     */
    private String masterName;

    /**
     * 档主手机号
     */
    private String masterPhone;

    /**
     * 可视化编码
     */
    private String visualizationCode;

    /**
     * 收费项目数量
     */
    private Integer costRuleCount;

    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Boolean deleted;

    /**
     * 扩展信息
     */
    private StallExpandVO stallExpand;
}
