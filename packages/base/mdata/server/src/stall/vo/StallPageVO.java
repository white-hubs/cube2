package com.guhecloud.cube.biz.mdata.server.stall.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 档位信息
 */
@Data
public class StallPageVO {

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

    /**
     * 档位区域名称
     */
    private String areaNames;

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
     * 是否出租
     */
    private Boolean leased;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 模型绑定
     */
    private Boolean bimBind;

    /**
     * 收费项目数量
     */
    private Integer costRuleCount;

    /**
     * 档主名称
     */
    private String masterName;

    /**
     * 档主手机号
     */
    private String masterPhone;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private List<FileVO> files;

    /**
     * 可视化编码
     */
    private String visualizationCode;

    /**
     * 是否上传附件
     */
    private Boolean uploadedFile;

    /**
     * 档位扩展信息
     */
    private StallExpandVO stallExpand;
}
