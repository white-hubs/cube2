package com.guhecloud.cube.srv.gen.stall.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GenStallVo {
    private Long id;
    private String code;
    private String name;
    private String type;
    private String typeName;
    /**
     * 档位区域
     */
    private List<String> areaIds;

    /**
     * 档位区域名称
     */
    private List<String> areaNames;
    private String location;
    private String builtUpArea;
    private String practicalArea;
    private String deptId;
    private List<Long> deptIds;
    private List<String> deptNames;
    private Boolean leaseType;
    private String remark;
    private Boolean enabled;
    private Integer costRuleCount;
    private LocalDateTime updateTime;

}
