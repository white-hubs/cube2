package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 商铺-用户关联
 * </p>
 *
 * @author bq
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("company_user")
public class CompanyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 商户ID
     */
    private String companyId;

    /**
     * 档位ID
     */
    private String stallId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 是否档主
     */
    private Boolean owner;


}
