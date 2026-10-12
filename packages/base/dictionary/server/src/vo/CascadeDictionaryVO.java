package com.guhecloud.cube.base.dictionary.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class CascadeDictionaryVO {
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
     * 字典项变量
     */
    private String itemCode;
    /**
     * 数据权限
     */
    private String permissionState;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 父id
     */
    private Long superiorsId;
    private List<CascadeDictionaryVO> nextList;
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
