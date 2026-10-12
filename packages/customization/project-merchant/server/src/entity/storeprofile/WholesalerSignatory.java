package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangliang
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_signatory")
public class WholesalerSignatory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 甲方ID
     */
    private Long firstPartId;

    /**
     * 签约人ID
     */
    private String userId;

    /**
     * 签约人姓名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;


}
