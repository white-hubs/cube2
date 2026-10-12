package com.guhecloud.cube.biz.mdata.client.dto;

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
     * 收费项目数量
     */
    private Integer costRuleCount;

    /**
     * 扩展信息
     */
    private StallExpandDTO stallExpand;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Boolean deleted;

    /**
     * 标签ID
     */
    private List<Long> tagIds;
    /**
     * 标签名称
     */
    private String tagNames;
    private String typeId;
    private String areaId;
    private List<Long> departmentIds;
    private String departmentNames;

}
