package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商铺-用户关联搜索
 * </p>
 *
 * @author bq
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyUserQueryDTO {


    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 关键字(商户名称)
     */
    private String search;

    /**
     * 区域ID
     */
    private String areaId;

    /**
     * 是否档主
     */
    private Boolean owner;


}
