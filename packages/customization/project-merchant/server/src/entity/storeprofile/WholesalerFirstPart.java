package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import cn.hutool.core.util.StrUtil;
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
@TableName("wholesaler_first_part")
public class WholesalerFirstPart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 甲方名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String postCode;

    /**
     * 电话
     */
    private String mobile;
    private String sealFile;

}
