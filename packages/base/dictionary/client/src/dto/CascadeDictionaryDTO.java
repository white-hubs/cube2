package com.guhecloud.cube.base.dictionary.dto;

import lombok.Data;

import java.util.List;

@Data
public class CascadeDictionaryDTO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编号
     */
    private String code;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 版本号
     */
    private String version;
    /**
     * 变量
     */
    private String variable;
    /**
     * 数据权限
     */
    private String permissionState;
    private List<CascadeDictionaryDTO> nextList;
    /**
     * 所有父级id
     */
    private String superiorsAllId;
    /**
     * 所有父级名称
     */
    private String superiorsAllName;
    /**
     * 所有父级编码
     */
    private String superiorsAllCode;

}
