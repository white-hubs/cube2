package com.guhecloud.cube.biz.mdata.server.supplier.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批发商认证信息
 *
 * @author lzy
 * @date 2021/12/4
 */
@Data
public class SupplierAuthenticationVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 主体名称
     */
    private String supplierName;

    /**
     * 联系人名称
     */
    private String liaison;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 认证状态
     */
    private SupplierAuthenticatedEnum status;

    /**
     * 认证类型
     */
    private PurchaserTypeEnum type;

    /**
     * 认证类型名称
     */
    private String typeName;

    /**
     * 认证类型编码
     */
    private String typeCode;

    /**
     * 代表人姓名
     */
    private String delegateName;

    /**
     * 代表人身份证号码
     */
    private String delegateIdentityCard;

    /**
     * 营业执照文件主键
     */
    private FileVO licenseFile;

    /**
     * 身份证正面文件主键
     */
    private FileVO frontIdentityCardFile;

    /**
     * 身份证反面文件主键
     */
    private FileVO backIdentityCardFile;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 备注
     */
    private String remark;

}
