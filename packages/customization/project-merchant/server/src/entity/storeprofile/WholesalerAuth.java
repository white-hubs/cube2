package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangliang
 * @since 2022-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_auth")
public class WholesalerAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String projectName;

    private String userId;

    private String projectId;

    private String companyName;

    private String companyId;

    private String enterpriseName;

    private String realName;

    private String identityCard;

    private String mobile;

    private String address;

    private String registrationNumber;

    private Integer status;

    private String loginAccount;

    private String postCode;

    private String reviewer;

    private LocalDateTime reviewTime;

    private String rejectReason;

    private String stallIds;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    private Long personnelPicId;
    private Long idCardPhotoId;
    private Long idCardNationId;
    private String licenseId;


}
