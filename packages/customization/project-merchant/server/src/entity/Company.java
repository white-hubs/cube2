package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("company")
public class Company {

    private String id;

    private String projectId;

    private String name;

    private String code;

    private String purpose;

    private String owner;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime endTime;

    private String remark;

    private String introduction;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted = false;
    /**
     * 安全协议文件ids
     */
    private String securityFileId;
    /**
     * 安全培训照片ids
     */
    private String trainingPhotoFileId;
    /**
     * 封面照片ids
     */
    private String companyPhotoFileId;
    private String companyFileId;
    /**
     * 商户营业期限是否长期
     */
    private Boolean longTerm;
    /**
     * 商户营业执照图片ids
     */
    private String businessLicenseFile;
    /**
     * 到期通知(true表示已经通知过
     */
    private Boolean expirationNotice;
    /**
     * 档位id
     */
    private Long stallId;
    /**
     * 档位名称
     */
    private String stallName;

}
