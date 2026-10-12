package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wangliang
 * @author 2022/01/04
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("company")
public class Wholesaler implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    @NotBlank
    @NotNull
    private String projectId;

    /**
     * 编号
     */
    private String code;
    /**
     * 供应商名称
     */
    private String name;
    /**
     * 所属人
     */
    private String owner;
    /**
     * mobile
     */
    private String mobile;
    /**
     * 经营信息
     */
    private String introduction;
    /**
     * 备注
     */
    private String remark;

    private Integer deleted;

    private LocalDateTime createTime;
    private String createBy;
}
